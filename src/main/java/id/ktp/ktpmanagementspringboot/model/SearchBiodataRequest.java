package id.ktp.ktpmanagementspringboot.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBiodataRequest {

    private String nik;

    private String name;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

}
