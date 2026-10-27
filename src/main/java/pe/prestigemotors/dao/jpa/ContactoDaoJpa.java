package pe.prestigemotors.dao.jpa;

import org.springframework.stereotype.Repository;

import pe.prestigemotors.dao.ContactoDao;
import pe.prestigemotors.model.Contacto;
import pe.prestigemotors.repository.ContactoRepository;

@Repository
public class ContactoDaoJpa extends JpaGenericDao<Contacto, Long> implements ContactoDao {

    public ContactoDaoJpa(ContactoRepository repositorio) {
        super(repositorio);
    }
}
