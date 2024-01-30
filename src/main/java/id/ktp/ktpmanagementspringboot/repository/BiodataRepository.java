package id.ktp.ktpmanagementspringboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import id.ktp.ktpmanagementspringboot.entity.Biodata;
import id.ktp.ktpmanagementspringboot.entity.User;

@Repository
public interface BiodataRepository extends JpaRepository<Biodata, String>, JpaSpecificationExecutor<Biodata> {

    Optional<Biodata> findFirstByUserAndId(User user, String id);

}
