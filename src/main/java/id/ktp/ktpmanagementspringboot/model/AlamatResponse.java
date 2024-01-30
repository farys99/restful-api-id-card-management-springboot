package id.ktp.ktpmanagementspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlamatResponse {

    private String id;

    private String alamat;

    private String rtrw;

    private String kelDesa;

    private String kecamatan;

}
