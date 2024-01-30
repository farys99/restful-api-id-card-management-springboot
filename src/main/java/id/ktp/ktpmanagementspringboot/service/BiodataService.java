package id.ktp.ktpmanagementspringboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import id.ktp.ktpmanagementspringboot.entity.Biodata;
import id.ktp.ktpmanagementspringboot.entity.User;
import id.ktp.ktpmanagementspringboot.model.BiodataResponse;
import id.ktp.ktpmanagementspringboot.model.CreateBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.SearchBiodataRequest;
import id.ktp.ktpmanagementspringboot.model.UpdateBiodataRequest;
import id.ktp.ktpmanagementspringboot.repository.BiodataRepository;
import jakarta.persistence.criteria.Predicate;

@Service
public class BiodataService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private BiodataRepository biodataRepository;

    @Transactional
    public BiodataResponse create(User user, CreateBiodataRequest request) {
        validationService.validate(request);

        Biodata biodata = new Biodata();
        biodata.setId(UUID.randomUUID().toString());
        biodata.setNik(request.getNik());
        biodata.setFirstName(request.getFirstName());
        biodata.setMiddleName(request.getMiddleName());
        biodata.setLastName(request.getLastName());
        biodata.setTempatLahir(request.getTempatLahir());
        biodata.setTanggalLahir(request.getTanggalLahir());
        biodata.setUser(user);
        biodataRepository.save(biodata);

        return toBiodataResponse(biodata);

    }

    @Transactional(readOnly = true)
    public BiodataResponse get(User user, String id) {

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        return toBiodataResponse(biodata);
    }

    @Transactional
    public BiodataResponse update(User user, UpdateBiodataRequest request) {
        validationService.validate(request);

        // mencari biodata dari id yang mau diedit
        Biodata biodata = biodataRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));

        biodata.setNik(request.getNik());
        biodata.setFirstName(request.getFirstName());
        biodata.setMiddleName(request.getMiddleName());
        biodata.setLastName(request.getLastName());
        biodata.setTempatLahir(request.getTempatLahir());
        biodata.setTanggalLahir(request.getTanggalLahir());

        biodataRepository.save(biodata);

        return toBiodataResponse(biodata);

    }

    @Transactional
    public void delete(User user, String id) {

        Biodata biodata = biodataRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biodata Not Found"));
        biodataRepository.delete(biodata);
    }

    @Transactional(readOnly = true)
    public Page<BiodataResponse> search(User user, SearchBiodataRequest request) {
        Specification<Biodata> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));

            if (Objects.nonNull(request.getNik())) {
                predicates.add(builder.like(root.get("nik"), "%" + request.getNik() + "%"));
            }

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + request.getName() + "%"),
                        builder.like(root.get("middleName"), "%" + request.getName() + "%"),
                        builder.like(root.get("lastName"), "%" + request.getName() + "%")

                ));
            }
            return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Biodata> biodata = biodataRepository.findAll(specification, pageable);
        List<BiodataResponse> biodataResponses = biodata.getContent().stream().map(this::toBiodataResponse).toList();
        return new PageImpl<>(biodataResponses, pageable, biodata.getTotalElements());

    }

    private BiodataResponse toBiodataResponse(Biodata biodata) {

        return BiodataResponse.builder()
                .id(biodata.getId())
                .nik(biodata.getNik())
                .firstName(biodata.getFirstName())
                .middleName(biodata.getMiddleName())
                .lastName(biodata.getLastName())
                .tempatLahir(biodata.getTempatLahir())
                .tanggalLahir(biodata.getTanggalLahir())
                .build();

    }

}
