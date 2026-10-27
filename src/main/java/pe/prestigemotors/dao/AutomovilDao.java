package pe.prestigemotors.dao;

import java.util.List;
import java.util.Optional;

import pe.prestigemotors.model.Automovil;

public interface AutomovilDao extends GenericDao<Automovil, Long> {

    Optional<Automovil> buscarPorSlug(String slug);

    /** Vehículos en estado DISPONIBLE que cumplen el filtro (sin ordenar). */
    List<Automovil> buscarDisponibles(FiltroAutomovil filtro);
}
