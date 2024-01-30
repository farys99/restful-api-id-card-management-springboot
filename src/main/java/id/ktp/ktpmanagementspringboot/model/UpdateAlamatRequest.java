package id.ktp.ktpmanagementspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAlamatRequest {

    @JsonIgnore
    @NotBlank
    private String biodataId;

    @JsonIgnore
    @NotBlank
    private String AlamatId;

    @NotBlank
    @Size(max = 100)
    private String alamat;

    @Size(max = 100)
    private String rtRw;

    @Size(max = 100)
    private String kelDesa;

    @Size(max = 100)
    private String kecamatan;

}
