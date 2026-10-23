package com.example.actividaddeusuario.dto;

import com.example.actividaddeusuario.model.PrioridadTicket;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReasignarPrioridadDTO {
    @NotNull(message = "La nueva prioridad es obligatoria")
    private PrioridadTicket prioridad;
}