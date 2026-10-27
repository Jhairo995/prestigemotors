package pe.prestigemotors.dao;

import java.math.BigDecimal;

/** Criterios de búsqueda del catálogo. Todos son opcionales (null o vacío = sin filtrar). */
public record FiltroAutomovil(String q, String tipo, String marca, String modelo,
                              Integer anio, BigDecimal min, BigDecimal max) {
}
