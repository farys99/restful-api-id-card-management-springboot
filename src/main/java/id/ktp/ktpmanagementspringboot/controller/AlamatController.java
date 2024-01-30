package id.ktp.ktpmanagementspringboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.AlamatResponse;
import id.ktp.ktpmanagementspringboot.model.CreateAlamatRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateAlamatRequest;
import id.ktp.ktpmanagementspringboot.model.WebResponse;
import id.ktp.ktpmanagementspringboot.service.AlamatService;

@RestController
public class AlamatController {

    @Autowired
    private AlamatService alamatService;

    @PostMapping(path = "/api/biodatas/{idBiodata}/alamats", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AlamatResponse> create(User user, @RequestBody CreateAlamatRequest request,
            @PathVariable("idBiodata") String id) {

        request.setBiodataId(id);

        AlamatResponse alamatResponse = alamatService.create(user, request);
        return WebResponse.<AlamatResponse>builder().data(alamatResponse).build();

    }

    @GetMapping(path = "/api/biodatas/{idBiodata}/alamats/{idAlamat}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AlamatResponse> get(
            User user,
            @PathVariable("idBiodata") String biodataId,
            @PathVariable("idAlamat") String alamatId) {

        AlamatResponse alamatResponse = alamatService.get(user, biodataId, alamatId);
        return WebResponse.<AlamatResponse>builder().data(alamatResponse).build();
    }

    @PutMapping(path = "/api/biodatas/{idBiodata}/alamats/{idAlamat}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AlamatResponse> update(
            User user,
            @RequestBody UpdateAlamatRequest request,
            @PathVariable("idBiodata") String IdBiodata,
            @PathVariable("idAlamat") String IdAlamat) {
        request.setBiodataId(IdBiodata);
        request.setAlamatId(IdAlamat);

        AlamatResponse alamatResponse = alamatService.update(user, request);
        return WebResponse.<AlamatResponse>builder().data(alamatResponse).build();
    }

    @DeleteMapping(path = "/api/biodatas/{idBiodata}/alamats/{idAlamat}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> remove(
            User user,
            @PathVariable("idBiodata") String biodataId,
            @PathVariable("idAlamat") String alamatId) {
        alamatService.remove(user, biodataId, alamatId);
        return WebResponse.<String>builder().data("OK").build();

    }

    @GetMapping(path = "/api/biodatas/{idBiodata}/alamats", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<AlamatResponse>> list(
            User user,
            @PathVariable("idBiodata") String biodataId) {

        List<AlamatResponse> alamatResponse = alamatService.list(user, biodataId);

        return WebResponse.<List<AlamatResponse>>builder().data(alamatResponse).build();
    }

}
