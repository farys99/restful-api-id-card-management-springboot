package id.ktp.ktpmanagementspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiodataResponse {

    private String id;

    private String nik;

    private String firstName;

    private String middleName;

    private String lastName;

    private String tempatLahir;

    private String tanggalLahir;

}
