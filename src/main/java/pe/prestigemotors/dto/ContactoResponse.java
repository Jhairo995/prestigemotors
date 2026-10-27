package pe.prestigemotors.dto;

import java.time.LocalDateTime;

import pe.prestigemotors.model.Contacto;

public record ContactoResponse(String codigo, String estado, LocalDateTime fechaSolicitud) {

    public static ContactoResponse from(Contacto c) {
        return new ContactoResponse(c.getCodigo(), c.getEstado().name(), c.getFechaSolicitud());
    }
}
