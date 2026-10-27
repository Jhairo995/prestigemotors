package pe.prestigemotors.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Parte JSON "datos" de test-drive.html. La licencia viaja aparte como archivo ("licencia").
 * Los nombres coinciden con los name="" del formulario.
 */
public record PruebaManejoRequest(

        @NotBlank(message = "La identificación fiscal es obligatoria")
        @Pattern(regexp = "^(\\d{8}|\\d{11})$", message = "Ingresa un DNI (8 dígitos) o un RUC (11 dígitos)")
        String identificacion,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "El nombre es demasiado largo")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 60, message = "El apellido es demasiado largo")
        String apellido,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 120, message = "El correo es demasiado largo")
        String correo,

        @NotBlank(message = "Indica el modelo que quieres probar")
        @Size(max = 120, message = "El modelo es demasiado largo")
        String vehiculo,

        @NotBlank(message = "Indica el tiempo deseado")
        @Size(max = 40, message = "El tiempo deseado es demasiado largo")
        String tiempoDeseado,

        @AssertTrue(message = "Debes aceptar los términos y condiciones")
        boolean aceptaTerminos,

        /** Imagen PNG de la firma en formato data URL (data:image/png;base64,...). */
        @NotBlank(message = "La firma digital es obligatoria")
        String firma) {
}
