package pe.prestigemotors.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato CRUD común a todos los DAO. La capa de servicio solo conoce estas interfaces:
 * no sabe si por debajo hay JPA, JDBC u otra tecnología de persistencia.
 */
public interface GenericDao<T, ID> {

    T guardar(T entidad);

    List<T> guardarTodos(Iterable<T> entidades);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    long contar();

    void eliminar(T entidad);
}
