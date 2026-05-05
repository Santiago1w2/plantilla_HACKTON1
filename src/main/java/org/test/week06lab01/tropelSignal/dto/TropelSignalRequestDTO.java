package org.test.week06lab01.tropelSignal.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TropelSignalRequestDTO {
    @NotNull
    private Long tropelId;
    @NotNull
    private Long guardianId;
    private String senderTag;
    @NotBlank @Size(min = 10)
    private String rawContent;
}
