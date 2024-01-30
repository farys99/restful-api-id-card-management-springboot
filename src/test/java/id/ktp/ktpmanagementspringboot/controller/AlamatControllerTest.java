package id.ktp.ktpmanagementspringboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.ktp.ktpmanagementspringboot.entity.Alamat;
import id.ktp.ktpmanagementspringboot.entity.Biodata;
import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.AlamatResponse;
import id.ktp.ktpmanagementspringboot.model.CreateAlamatRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateAlamatRequest;
import id.ktp.ktpmanagementspringboot.model.WebResponse;
import id.ktp.ktpmanagementspringboot.repository.AlamatRepository;
import id.ktp.ktpmanagementspringboot.repository.BiodataRepository;
import id.ktp.ktpmanagementspringboot.repository.UserRepository;
import id.ktp.ktpmanagementspringboot.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class AlamatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlamatRepository alamatRepository;

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        alamatRepository.deleteAll();
        biodataRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("iniusername");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Sholeh Al Farys");
        user.setToken("initoken");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        Biodata biodata = new Biodata();
        biodata.setId("iniIdBiodata");
        biodata.setUser(user);
        biodata.setNik("112233");
        biodata.setFirstName("Sholeh");
        biodata.setMiddleName("Al");
        biodata.setLastName("Farys");
        biodata.setTempatLahir("Rembang");
        biodata.setTanggalLahir("110198");
        biodataRepository.save(biodata);
    }

    @Test
    void testCreateAlamatBadRequest() throws Exception {

        CreateAlamatRequest request = new CreateAlamatRequest();
        request.setAlamat("");

        mockMvc.perform(
                post("/api/biodatas/iniIdBiodata/alamats")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<AlamatResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateAlamatSuccess() throws Exception {

        CreateAlamatRequest request = new CreateAlamatRequest();
        request.setAlamat("jln.seoul");
        request.setRtRw("001001");
        request.setKelDesa("seoul");
        request.setKecamatan("daegu");

        mockMvc.perform(
                post("/api/biodatas/iniIdBiodata/alamats")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<AlamatResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(request.getAlamat(), response.getData().getAlamat());
                    assertEquals(request.getRtRw(), response.getData().getRtrw());
                    assertEquals(request.getKelDesa(), response.getData().getKelDesa());
                    assertEquals(request.getKecamatan(), response.getData().getKecamatan());

                    assertTrue(alamatRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void testGetAlamatNotFound() throws Exception {
        mockMvc.perform(
                get("/api/biodatas/iniIdBiodata/alamats/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testGetAlamatSuccess() throws Exception {

        Biodata biodata = biodataRepository.findById("iniIdBiodata").orElseThrow();

        Alamat alamat = new Alamat();

        alamat.setBiodata(biodata);
        alamat.setId("test");
        alamat.setAlamat("jln.seoul");
        alamat.setRtRw("001001");
        alamat.setKelDesa("seoul");
        alamat.setKecamatan("daegu");
        alamatRepository.save(alamat);

        mockMvc.perform(
                get("/api/biodatas/iniIdBiodata/alamats/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<AlamatResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(alamat.getAlamat(), response.getData().getAlamat());
                    assertEquals(alamat.getRtRw(), response.getData().getRtrw());
                    assertEquals(alamat.getKelDesa(), response.getData().getKelDesa());
                    assertEquals(alamat.getKecamatan(), response.getData().getKecamatan());

                    assertTrue(alamatRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void testUpdateAlamatBadRequest() throws Exception {

        UpdateAlamatRequest request = new UpdateAlamatRequest();

        request.setAlamat("");
        request.setRtRw("");

        mockMvc.perform(
                put("/api/biodatas/iniIdBiodata/alamats/1234")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<AlamatResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateAlamatSuccess() throws Exception {

        Biodata biodata = biodataRepository.findById("iniIdBiodata").orElseThrow();

        Alamat alamat = new Alamat();
        alamat.setId(UUID.randomUUID().toString());
        alamat.setBiodata(biodata);
        alamat.setAlamat("jln.seoul");
        alamat.setRtRw("001001");
        alamat.setKelDesa("seoul");
        alamat.setKecamatan("daegu");
        alamatRepository.save(alamat);

        UpdateAlamatRequest request = new UpdateAlamatRequest();
        request.setAlamat("jalan jalan");
        request.setRtRw("etre ertean");
        request.setKelDesa("pedesaan");
        request.setKecamatan("kuecakecamuatuan");

        mockMvc.perform(
                put("/api/biodatas/iniIdBiodata/alamats/" + alamat.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<AlamatResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(request.getAlamat(), response.getData().getAlamat());
                    assertEquals(request.getRtRw(), response.getData().getRtrw());
                    assertEquals(request.getKelDesa(), response.getData().getKelDesa());
                    assertEquals(request.getKecamatan(), response.getData().getKecamatan());

                    assertTrue(alamatRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void testDeleteAlamatFailed() throws Exception {
        mockMvc.perform(
                delete("/api/biodatas/iniIdBiodata/alamats/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(status().isNotFound()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });

    }

    @Test
    void testDeleteAlamatSuccess() throws Exception {

        Biodata biodata = biodataRepository.findById("iniIdBiodata").orElseThrow();

        Alamat alamat = new Alamat();
        alamat.setId("iniIdAlamat");
        alamat.setBiodata(biodata);
        alamat.setAlamat("jln.seoul");
        alamat.setRtRw("001001");
        alamat.setKelDesa("seoul");
        alamat.setKecamatan("daegu");
        alamatRepository.save(alamat);

        mockMvc.perform(
                delete("/api/biodatas/iniIdBiodata/alamats/iniIdAlamat")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());

                    assertFalse(alamatRepository.existsById("iniIdAlamat"));
                });

    }

    @Test
    void testListAlamatNotFound() throws Exception {
        mockMvc.perform(
                get("/api/biodatas/nggatau/alamats")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testListAlamatSuccess() throws Exception {

        Biodata biodata = biodataRepository.findById("iniIdBiodata").orElseThrow();

        for (int i = 0; i < 5; i++) {

            Alamat alamat = new Alamat();
            alamat.setBiodata(biodata);
            alamat.setId("test-" + i);
            alamat.setAlamat("jln.seoul");
            alamat.setRtRw("001001");
            alamat.setKelDesa("seoul");
            alamat.setKecamatan("daegu");
            alamatRepository.save(alamat);

        }

        mockMvc.perform(
                get("/api/biodatas/iniIdBiodata/alamats")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<AlamatResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(5, response.getData().size());

                });
    }

    @AfterEach
    void afterSetUp() {
        alamatRepository.deleteAll();
        biodataRepository.deleteAll();
        userRepository.deleteAll();
    }

}
