package id.ktp.ktpmanagementspringboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import id.ktp.ktpmanagementspringboot.entity.Biodata;
import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.BiodataResponse;
import id.ktp.ktpmanagementspringboot.model.CreateBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.WebResponse;
import id.ktp.ktpmanagementspringboot.repository.BiodataRepository;
import id.ktp.ktpmanagementspringboot.repository.UserRepository;
import id.ktp.ktpmanagementspringboot.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class BiodataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        biodataRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("iniusername");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("initoken");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);
    }

    @Test
    void testCreateBiodataFailed() throws Exception {

        mockMvc.perform(
                post("/api/biodatas")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateBiodataSuccess() throws Exception {

        CreateBiodataRequest request = new CreateBiodataRequest();
        request.setNik("111");
        request.setFirstName("farys");
        request.setMiddleName("lagi");
        request.setLastName("ngetest");
        request.setTempatLahir("korean");
        request.setTanggalLahir("010198");

        mockMvc.perform(
                post("/api/biodatas")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<BiodataResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(request.getNik(), response.getData().getNik());
                    assertEquals(request.getFirstName(), response.getData().getFirstName());
                    assertEquals(request.getMiddleName(), response.getData().getMiddleName());
                    assertEquals(request.getLastName(), response.getData().getLastName());
                    assertEquals(request.getTempatLahir(), response.getData().getTempatLahir());
                    assertEquals(request.getTanggalLahir(), response.getData().getTanggalLahir());

                    assertTrue(biodataRepository.existsById(response.getData().getId()));

                });
    }

    @Test
    void testCreateBiodataUnauthorized() throws Exception {

        CreateBiodataRequest request = new CreateBiodataRequest();
        request.setNik("111");
        request.setFirstName("farys");
        request.setMiddleName("lagi");
        request.setLastName("ngetest");
        request.setTempatLahir("korean");
        request.setTanggalLahir("010198");

        mockMvc.perform(
                post("/api/biodatas")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateBiodataBadRequest() throws Exception {

        CreateBiodataRequest request = new CreateBiodataRequest();
        request.setNik("111");
        request.setFirstName("");
        request.setMiddleName("");
        request.setLastName("ngetest");
        request.setTempatLahir("korean");
        request.setTanggalLahir("010198");

        mockMvc.perform(
                post("/api/biodatas")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testGetBiodataNotFound() throws Exception {

        mockMvc.perform(
                get("/api/biodatas/1234321")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<BiodataResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testGetBiodataSuccess() throws Exception {
        User user = userRepository.findById("iniusername").orElseThrow(); // parameter id disini adalah username

        Biodata biodata = new Biodata();
        biodata.setId(UUID.randomUUID().toString()); // id disini adalah actual id pada nama databasenya
        biodata.setUser(user);
        biodata.setNik("112233");
        biodata.setFirstName("Sholeh");
        biodata.setMiddleName("Al");
        biodata.setLastName("Farys");
        biodata.setTempatLahir("Rembang");
        biodata.setTanggalLahir("110198");
        biodataRepository.save(biodata);

        mockMvc.perform(
                get("/api/biodatas/" + biodata.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<BiodataResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());

                    assertEquals(biodata.getId(), response.getData().getId());
                    assertEquals(biodata.getNik(), response.getData().getNik());
                    assertEquals(biodata.getFirstName(), response.getData().getFirstName());
                    assertEquals(biodata.getMiddleName(), response.getData().getMiddleName());
                    assertEquals(biodata.getLastName(), response.getData().getLastName());
                    assertEquals(biodata.getTempatLahir(), response.getData().getTempatLahir());
                    assertEquals(biodata.getTanggalLahir(), response.getData().getTanggalLahir());
                });
    }

    @Test
    void testUpdateBiodataSuccess() throws Exception {

        User user = userRepository.findById("iniusername").orElseThrow(); // parameter id disini adalah username

        Biodata biodata = new Biodata();
        biodata.setId(UUID.randomUUID().toString()); // id disini adalah actual id pada nama databasenya
        biodata.setUser(user);
        biodata.setNik("112233");
        biodata.setFirstName("Sholeh");
        biodata.setMiddleName("Al");
        biodata.setLastName("Farys");
        biodata.setTempatLahir("Rembang");
        biodata.setTanggalLahir("110198");
        biodataRepository.save(biodata);

        UpdateBiodataRequest request = new UpdateBiodataRequest();

        request.setNik("9999");
        request.setFirstName("Farys");
        request.setMiddleName("SHOL");
        request.setLastName("Chawnima");
        request.setTempatLahir("Korean");
        request.setTanggalLahir("111498");

        mockMvc.perform(
                put("/api/biodatas/" + biodata.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<BiodataResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());

                    assertEquals(request.getNik(), response.getData().getNik());
                    assertEquals(request.getFirstName(), response.getData().getFirstName());
                    assertEquals(request.getMiddleName(), response.getData().getMiddleName());
                    assertEquals(request.getLastName(), response.getData().getLastName());
                    assertEquals(request.getTempatLahir(), response.getData().getTempatLahir());
                    assertEquals(request.getTanggalLahir(), response.getData().getTanggalLahir());

                    assertTrue(biodataRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void testUpdateBiodataBadRequest() throws Exception {

        UpdateBiodataRequest request = new UpdateBiodataRequest();

        request.setNik("");
        request.setFirstName("wrrrrr");
        request.setTanggalLahir("356646547"); // bad request cause this size more than 6 character

        mockMvc.perform(
                put("/api/biodatas/1234")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<BiodataResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testDeleteBiodataFailed() throws Exception {
        mockMvc.perform(
                delete("/api/biodatas/11223")
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
    void testDeleteBiodataSuccess() throws Exception {
        User user = userRepository.findById("iniusername").orElseThrow(); // parameter id disini adalah username

        Biodata biodata = new Biodata();
        biodata.setId(UUID.randomUUID().toString()); // id disini adalah actual id pada nama databasenya
        biodata.setUser(user);
        biodata.setNik("112233");
        biodata.setFirstName("Sholeh");
        biodata.setMiddleName("Al");
        biodata.setLastName("Farys");
        biodata.setTempatLahir("Rembang");
        biodata.setTanggalLahir("110198");
        biodataRepository.save(biodata);

        mockMvc.perform(
                delete("/api/biodatas/" + biodata.getId())
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
                });
    }

    @Test
    void searchNotFound() throws Exception {
        mockMvc.perform(
                get("/api/biodatas")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPagingResponse().getTotalPage());
                    assertEquals(0, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });
    }

    @Test
    void searchSuccess() throws Exception {
        User user = userRepository.findById("iniusername").orElseThrow();

        for (int i = 0; i < 100; i++) {

            Biodata biodata = new Biodata();
            biodata.setId(UUID.randomUUID().toString());
            biodata.setUser(user);
            biodata.setNik("112233" + i);
            biodata.setFirstName("Sholeh " + i);
            biodata.setMiddleName("Al");
            biodata.setLastName("Farys");
            biodata.setTempatLahir("Rembang");
            biodata.setTanggalLahir("110198");
            biodataRepository.save(biodata);
        }

        mockMvc.perform(
                get("/api/biodatas")
                        .queryParam("name", "Sholeh")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPagingResponse().getTotalPage());
                    assertEquals(0, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });

        mockMvc.perform(
                get("/api/biodatas")
                        .queryParam("name", "Al")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPagingResponse().getTotalPage());
                    assertEquals(0, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });

        mockMvc.perform(
                get("/api/biodatas")
                        .queryParam("name", "Farys")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPagingResponse().getTotalPage());
                    assertEquals(0, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });

        mockMvc.perform(
                get("/api/biodatas")
                        .queryParam("nik", "1223")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPagingResponse().getTotalPage());
                    assertEquals(0, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });

        mockMvc.perform(
                get("/api/biodatas")
                        .queryParam("nik", "1223")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "initoken"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<BiodataResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(10, response.getPagingResponse().getTotalPage());
                    assertEquals(1000, response.getPagingResponse().getCurrentPage());
                    assertEquals(10, response.getPagingResponse().getSize());
                });
    }

    @AfterEach
    void afterSetUp() {
        biodataRepository.deleteAll();
        userRepository.deleteAll();
    }

}
