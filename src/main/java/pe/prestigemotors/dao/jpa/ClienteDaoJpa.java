package pe.prestigemotors.dao.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import pe.prestigemotors.dao.ClienteDao;
import pe.prestigemotors.model.Cliente;
import pe.prestigemotors.repository.ClienteRepository;

@Repository
public class ClienteDaoJpa extends JpaGenericDao<Cliente, Long> implements ClienteDao {

    private final ClienteRepository repositorio;

    public ClienteDaoJpa(ClienteRepository repositorio) {
        super(repositorio);
        this.repositorio = repositorio;
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return repositorio.findByCorreoIgnoreCase(correo);
    }
}
