package mx.edu.utez.proyecto1D.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRentaDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @NotNull(message = "La edad del conductor es obligatoria")
    @Positive(message = "La edad debe ser mayor a 0")
    private Integer edadConductor;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Pattern(regexp = "^(COMPACTO|SEDAN|SUV|CAMIONETA)$", message = "El tipo de vehículo permitido es: COMPACTO, SEDAN, SUV, CAMIONETA")
    private String tipoVehiculo;

    @NotNull(message = "Los días de renta son obligatorios")
    @Positive(message = "Los días de renta deben ser mayor a 0")
    private Integer diasRenta;

    @NotNull(message = "Los kilómetros estimados son obligatorios")
    @Min(value = 0, message = "Los kilómetros estimados no pueden ser negativos")
    private Integer kilometrosEstimados;

    @NotNull(message = "La indicación de seguro completo es obligatoria")
    private Boolean seguroCompleto;
}