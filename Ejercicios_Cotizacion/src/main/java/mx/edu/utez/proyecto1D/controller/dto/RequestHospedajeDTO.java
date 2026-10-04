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
public class RequestHospedajeDTO {

    @NotBlank(message = "El nombre del huésped es obligatorio")
    private String nombreHuesped;

    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Pattern(regexp = "^(INDIVIDUAL|DOBLE|SUITE)$", message = "El tipo de habitación permitido es: INDIVIDUAL, DOBLE, SUITE")
    private String tipoHabitacion;

    @NotNull(message = "El número de noches es obligatorio")
    @Positive(message = "El número de noches debe ser mayor a 0")
    private Integer numeroNoches;

    @NotNull(message = "El número de huéspedes es obligatorio")
    @Positive(message = "El número de huéspedes debe ser mayor a 0")
    private Integer numeroHuespedes;

    @NotBlank(message = "La temporada es obligatoria")
    @Pattern(regexp = "^(BAJA|REGULAR|ALTA)$", message = "La temporada permitida es: BAJA, REGULAR, ALTA")
    private String temporada;

    @NotNull(message = "La indicación de desayuno es obligatoria")
    private Boolean incluyeDesayuno;

    @NotNull(message = "La indicación de estacionamiento es obligatoria")
    private Boolean incluyeEstacionamiento;
}