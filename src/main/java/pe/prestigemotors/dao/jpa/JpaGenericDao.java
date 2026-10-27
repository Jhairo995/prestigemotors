package pe.prestigemotors.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.prestigemotors.dao.GenericDao;

/** Implementación base: delega el CRUD en el repositorio de Spring Data. Aquí no hay lógica de negocio. */
abstract class JpaGenericDao<T, ID> implements GenericDao<T, ID> {

    private final JpaRepository<T, ID> repositorio;

    protected JpaGenericDao(JpaRepository<T, ID> repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public T guardar(T entidad) {
        return repositorio.save(entidad);
    }

    @Override
    public List<T> guardarTodos(Iterable<T> entidades) {
        return repositorio.saveAll(entidades);
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return repositorio.findById(id);
    }

    @Override
    public List<T> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    public long contar() {
        return repositorio.count();
    }

    @Override
    public void eliminar(T entidad) {
        repositorio.delete(entidad);
    }
}
