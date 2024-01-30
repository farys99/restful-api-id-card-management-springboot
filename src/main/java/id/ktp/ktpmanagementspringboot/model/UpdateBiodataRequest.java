package id.ktp.ktpmanagementspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBiodataRequest {

    @JsonIgnore
    @NotBlank
    private String id;
    // saya memerlukan id didalam reqBody dikarenakan parameter untuk melakukan
    // pencarian update harus menggunakan id

    @Size(max = 100)
    private String nik;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String middleName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String tempatLahir;

    @Size(max = 6)
    private String tanggalLahir;

}
