package pe.prestigemotors.dao.jpa;

import org.springframework.stereotype.Repository;

import pe.prestigemotors.dao.PruebaManejoDao;
import pe.prestigemotors.model.PruebaManejo;
import pe.prestigemotors.repository.PruebaManejoRepository;

@Repository
public class PruebaManejoDaoJpa extends JpaGenericDao<PruebaManejo, Long> implements PruebaManejoDao {

    public PruebaManejoDaoJpa(PruebaManejoRepository repositorio) {
        super(repositorio);
    }
}
