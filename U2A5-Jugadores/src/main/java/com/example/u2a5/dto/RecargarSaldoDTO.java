package com.example.u2a5.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecargarSaldoDTO {
    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto a recargar debe ser mayor a 0")
    private Double monto;
}