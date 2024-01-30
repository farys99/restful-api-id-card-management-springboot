package id.ktp.ktpmanagementspringboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ktp.ktpmanagementspringboot.entity.Alamat;
import id.ktp.ktpmanagementspringboot.entity.Biodata;

@Repository
public interface AlamatRepository extends JpaRepository<Alamat, String> {

    Optional<Alamat> findFirstByBiodataAndId(Biodata biodata, String id);

    List<Alamat> findAllByBiodata(Biodata biodata);

}
