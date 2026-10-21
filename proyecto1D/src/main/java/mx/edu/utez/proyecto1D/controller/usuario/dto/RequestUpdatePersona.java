package mx.edu.utez.proyecto1D.controller.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdatePersona {
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 6, max = 20, message = "Username debe tener entre 6 y 20 caracteres")
    private String username;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
}
