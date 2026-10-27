package pe.prestigemotors.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.Predicate;
import pe.prestigemotors.dao.AutomovilDao;
import pe.prestigemotors.dao.FiltroAutomovil;
import pe.prestigemotors.model.Automovil;
import pe.prestigemotors.model.EstadoAutomovil;
import pe.prestigemotors.repository.AutomovilRepository;

@Repository
public class AutomovilDaoJpa extends JpaGenericDao<Automovil, Long> implements AutomovilDao {

    private final AutomovilRepository repositorio;

    public AutomovilDaoJpa(AutomovilRepository repositorio) {
        super(repositorio);
        this.repositorio = repositorio;
    }

    @Override
    public Optional<Automovil> buscarPorSlug(String slug) {
        return repositorio.findBySlug(slug);
    }

    /** La construcción de la consulta (Criteria/Specification) vive aquí, no en el servicio. */
    @Override
    public List<Automovil> buscarDisponibles(FiltroAutomovil f) {
        Specification<Automovil> filtros = (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();
            condiciones.add(cb.equal(root.get("estado"), EstadoAutomovil.DISPONIBLE));

            if (hayTexto(f.q())) {
                String patron = patron(f.q());
                condiciones.add(cb.or(
                        cb.like(cb.lower(root.get("nombre")), patron),
                        cb.like(cb.lower(root.get("marca")), patron),
                        cb.like(cb.lower(root.get("modelo")), patron)));
            }
            if (hayTexto(f.tipo())) {
                condiciones.add(cb.like(cb.lower(root.get("tipo")), patron(f.tipo())));
            }
            if (hayTexto(f.marca())) {
                condiciones.add(cb.like(cb.lower(root.get("marca")), patron(f.marca())));
            }
            if (hayTexto(f.modelo())) {
                condiciones.add(cb.like(cb.lower(root.get("modelo")), patron(f.modelo())));
            }
            if (f.anio() != null) {
                condiciones.add(cb.equal(root.get("anio"), f.anio()));
            }
            if (f.min() != null) {
                condiciones.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("precio"), f.min()));
            }
            if (f.max() != null) {
                condiciones.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("precio"), f.max()));
            }
            return cb.and(condiciones.toArray(new Predicate[0]));
        };
        return repositorio.findAll(filtros);
    }

    private static boolean hayTexto(String s) {
        return s != null && !s.isBlank();
    }

    private static String patron(String s) {
        return "%" + s.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
