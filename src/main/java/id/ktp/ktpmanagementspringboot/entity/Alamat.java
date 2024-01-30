package id.ktp.ktpmanagementspringboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alamats")
public class Alamat {

    @Id
    private String id;

    private String alamat;

    @Column(name = "rt_rw")
    private String rtRw;

    @Column(name = "kel_desa")
    private String kelDesa;

    private String kecamatan;

    @ManyToOne
    @JoinColumn(name = "alamat_id", referencedColumnName = "id")
    private Biodata biodata;

}
