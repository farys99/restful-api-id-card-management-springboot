package id.ktp.ktpmanagementspringboot.model;

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
public class CreateBiodataRequest {

    @NotBlank
    @Size(max = 100)
    private String nik;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String middleName;

    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    private String tempatLahir;

    @NotBlank
    @Size(max = 6)
    private String tanggalLahir;

}
