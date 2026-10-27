package pe.prestigemotors.dao.jpa;

import org.springframework.stereotype.Repository;

import pe.prestigemotors.dao.MantenimientoDao;
import pe.prestigemotors.model.Mantenimiento;
import pe.prestigemotors.repository.MantenimientoRepository;

@Repository
public class MantenimientoDaoJpa extends JpaGenericDao<Mantenimiento, Long> implements MantenimientoDao {

    public MantenimientoDaoJpa(MantenimientoRepository repositorio) {
        super(repositorio);
    }
}
