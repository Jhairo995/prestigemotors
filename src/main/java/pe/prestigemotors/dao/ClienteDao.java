package pe.prestigemotors.dao;

import java.util.Optional;

import pe.prestigemotors.model.Cliente;

public interface ClienteDao extends GenericDao<Cliente, Long> {

    Optional<Cliente> buscarPorCorreo(String correo);
}
