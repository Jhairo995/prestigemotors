package pe.prestigemotors.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.prestigemotors.dao.AutomovilDao;
import pe.prestigemotors.dao.FiltroAutomovil;
import pe.prestigemotors.dto.AutomovilResponse;
import pe.prestigemotors.exception.RecursoNoEncontradoException;
import pe.prestigemotors.model.Automovil;

@Service
public class AutomovilService {

    private final AutomovilDao automovilDao;

    public AutomovilService(AutomovilDao automovilDao) {
        this.automovilDao = automovilDao;
    }

    /** Catálogo con los filtros del formulario de búsqueda de AutoSubasta. Todos son opcionales. */
    @Transactional(readOnly = true)
    public List<AutomovilResponse> buscar(String q, String tipo, String marca, String modelo,
                                          Integer anio, BigDecimal min, BigDecimal max, String orden) {

        FiltroAutomovil filtro = new FiltroAutomovil(q, tipo, marca, modelo, anio, min, max);

        List<Automovil> resultado = new ArrayList<>(automovilDao.buscarDisponibles(filtro));
        resultado.sort(comparadorPara(orden));
        return resultado.stream().map(AutomovilResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AutomovilResponse obtenerPorSlug(String slug) {
        return automovilDao.buscarPorSlug(slug)
                .map(AutomovilResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo '" + slug + "'"));
    }

    /** Valores del select "Filtrar por": fecha_desc (por defecto), fecha_asc, precio_asc, precio_desc. */
    private Comparator<Automovil> comparadorPara(String orden) {
        Comparator<Automovil> porFecha = Comparator.comparing(Automovil::getFechaIngreso);
        // Los autos sin precio ("Consultar precio") siempre quedan al final
        Comparator<Automovil> porPrecio = Comparator.comparing(Automovil::getPrecio,
                Comparator.nullsLast(Comparator.<BigDecimal>naturalOrder()));
        Comparator<Automovil> porPrecioDesc = Comparator.comparing(Automovil::getPrecio,
                Comparator.nullsLast(Comparator.<BigDecimal>reverseOrder()));

        return switch (orden == null ? "" : orden) {
            case "fecha_asc" -> porFecha;
            case "precio_asc" -> porPrecio;
            case "precio_desc" -> porPrecioDesc;
            default -> porFecha.reversed();
        };
    }
}
