package pe.prestigemotors.dto;

import java.time.LocalDateTime;

import pe.prestigemotors.model.PruebaManejo;

public record PruebaManejoResponse(String codigo, String estado, LocalDateTime fechaSolicitud) {

    public static PruebaManejoResponse from(PruebaManejo p) {
        return new PruebaManejoResponse(p.getCodigo(), p.getEstado().name(), p.getFechaSolicitud());
    }
}
