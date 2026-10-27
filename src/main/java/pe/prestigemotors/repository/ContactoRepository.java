package pe.prestigemotors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.prestigemotors.model.Contacto;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
}
