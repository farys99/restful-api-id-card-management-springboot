package id.ktp.ktpmanagementspringboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.BiodataResponse;
import id.ktp.ktpmanagementspringboot.model.CreateBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.PagingResponse;
import id.ktp.ktpmanagementspringboot.model.SearchBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.WebResponse;
import id.ktp.ktpmanagementspringboot.service.BiodataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class BiodataController {

    @Autowired
    private BiodataService biodataService;

    @PostMapping(path = "/api/biodatas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<BiodataResponse> create(User user, @RequestBody CreateBiodataRequest request) {

        BiodataResponse biodataResponse = biodataService.create(user, request);
        return WebResponse.<BiodataResponse>builder().data(biodataResponse).build();

    }

    @GetMapping(path = "/api/biodatas/{idBiodata}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<BiodataResponse> get(User user, @PathVariable("idBiodata") String id) {

        BiodataResponse biodataResponse = biodataService.get(user, id);
        return WebResponse.<BiodataResponse>builder().data(biodataResponse).build();
    }

    @PutMapping(path = "/api/biodatas/{idBiodata}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<BiodataResponse> update(User user, @RequestBody UpdateBiodataRequest request,
            @PathVariable("idBiodata") String id) {

        request.setId(id); // kode ini diperlukan karena req body memiliki variable id sedangkan kita ingin
                           // memasukkan id lewat path variable bukan reqBody, maka jadilah kode ini

        BiodataResponse biodataResponse = biodataService.update(user, request);
        return WebResponse.<BiodataResponse>builder().data(biodataResponse).build();
    }

    @DeleteMapping(path = "/api/biodatas/{idBiodata}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user, @PathVariable("idBiodata") String id) {
        biodataService.delete(user, id);
        return WebResponse.<String>builder().data("OK").build();

    }

    @GetMapping(path = "/api/biodatas", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<BiodataResponse>> search(
            User user,
            @RequestParam(value = "nik", required = false) String nik,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchBiodataRequest request = SearchBiodataRequest.builder()
                .page(page)
                .size(size)
                .nik(nik)
                .name(name)
                .build();

        Page<BiodataResponse> biodataResponse = biodataService.search(user, request);
        return WebResponse.<List<BiodataResponse>>builder()
                .data(biodataResponse.getContent())
                .pagingResponse(PagingResponse.builder()
                        .currentPage(biodataResponse.getNumber())
                        .totalPage(biodataResponse.getTotalPages())
                        .size(biodataResponse.getSize())
                        .build())
                .build();

    }

}
