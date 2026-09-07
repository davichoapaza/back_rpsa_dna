package bo.gob.dgac.rbs.dna.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

    private boolean exito;
    private String mensaje;
    private T datos;
}