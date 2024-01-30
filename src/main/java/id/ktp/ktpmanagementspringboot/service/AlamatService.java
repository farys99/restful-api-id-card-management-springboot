package id.ktp.ktpmanagementspringboot.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import id.ktp.ktpmanagementspringboot.entity.Alamat;
import id.ktp.ktpmanagementspringboot.entity.Biodata;
import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.AlamatResponse;
import id.ktp.ktpmanagementspringboot.model.CreateAlamatRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateAlamatRequest;
import id.ktp.ktpmanagementspringboot.repository.AlamatRepository;
import id.ktp.ktpmanagementspringboot.repository.BiodataRepository;

@Service
public class AlamatService {

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private AlamatRepository alamatRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AlamatResponse create(User user, CreateAlamatRequest request) {
        validationService.validate(request);

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, request.getBiodataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        Alamat alamat = new Alamat();
        alamat.setId(UUID.randomUUID().toString());
        alamat.setBiodata(biodata);
        alamat.setAlamat(request.getAlamat());
        alamat.setRtRw(request.getRtRw());
        alamat.setKelDesa(request.getKelDesa());
        alamat.setKecamatan(request.getKecamatan());
        alamatRepository.save(alamat);

        return toAlamatResponse(alamat);
    }

    @Transactional(readOnly = true)
    public AlamatResponse get(User user, String biodataId, String alamatId) {

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, biodataId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        Alamat alamat = alamatRepository.findFirstByBiodataAndId(biodata, alamatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alamat Not Found"));

        return toAlamatResponse(alamat);
    }

    @Transactional
    public AlamatResponse update(User user, UpdateAlamatRequest request) {
        validationService.validate(request);

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, request.getBiodataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        Alamat alamat = alamatRepository.findFirstByBiodataAndId(biodata, request.getAlamatId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alamat Not Found"));

        alamat.setAlamat(request.getAlamat());
        alamat.setRtRw(request.getRtRw());
        alamat.setKelDesa(request.getKelDesa());
        alamat.setKecamatan(request.getKecamatan());
        alamatRepository.save(alamat);

        return toAlamatResponse(alamat);
    }

    @Transactional
    public void remove(User user, String biodataId, String alamatId) {

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, biodataId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        Alamat alamat = alamatRepository.findFirstByBiodataAndId(biodata, alamatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alamat Not Found"));

        alamatRepository.delete(alamat);
    }

    @Transactional(readOnly = true)
    public List<AlamatResponse> list(User user, String idBiodata) {

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, idBiodata)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        List<Alamat> alamats = alamatRepository.findAllByBiodata(biodata);
        return alamats.stream().map(this::toAlamatResponse).toList();
    }

    private AlamatResponse toAlamatResponse(Alamat alamat) {

        return AlamatResponse.builder()
                .id(alamat.getId())
                .alamat(alamat.getAlamat())
                .rtrw(alamat.getRtRw())
                .kelDesa(alamat.getKelDesa())
                .kecamatan(alamat.getKecamatan())
                .build();

    }

}
