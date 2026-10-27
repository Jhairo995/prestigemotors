package pe.prestigemotors.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.prestigemotors.model.MotivoContacto;

/** Datos que llegan desde contacto.html (los nombres coinciden con los name="" del formulario). */
public record ContactoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "El nombre es demasiado largo")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 60, message = "El apellido es demasiado largo")
        String apellido,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?[0-9 ]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
        String telefono,

        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 120, message = "El correo es demasiado largo")
        String correo,

        @NotNull(message = "Selecciona en qué podemos ayudarte")
        MotivoContacto motivo,

        @Size(max = 120, message = "El vehículo de interés es demasiado largo")
        String vehiculo,

        @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres")
        String mensaje) {
}
