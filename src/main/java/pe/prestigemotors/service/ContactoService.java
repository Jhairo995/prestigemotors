package pe.prestigemotors.service;

import java.time.Year;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.prestigemotors.dao.ClienteDao;
import pe.prestigemotors.dao.ContactoDao;
import pe.prestigemotors.dto.ContactoRequest;
import pe.prestigemotors.dto.ContactoResponse;
import pe.prestigemotors.model.Cliente;
import pe.prestigemotors.model.Contacto;

@Service
public class ContactoService {

    private final ClienteDao clienteDao;
    private final ContactoDao contactoDao;

    public ContactoService(ClienteDao clienteDao, ContactoDao contactoDao) {
        this.clienteDao = clienteDao;
        this.contactoDao = contactoDao;
    }

    /** Registra la solicitud. Si dejó correo, reutiliza al cliente con ese correo o lo crea. */
    @Transactional
    public ContactoResponse registrar(ContactoRequest req) {
        String correo = limpiar(req.correo());
        correo = correo == null ? null : correo.toLowerCase();

        Cliente cliente = null;
        if (correo != null) {
            cliente = clienteDao.buscarPorCorreo(correo).orElseGet(() -> {
                Cliente nuevo = new Cliente();
                nuevo.setNombre(req.nombre().trim());
                nuevo.setApellido(req.apellido().trim());
                nuevo.setCorreo(req.correo().trim().toLowerCase());
                return clienteDao.guardar(nuevo);
            });
        }

        Contacto c = new Contacto();
        c.setCliente(cliente);
        c.setNombre(req.nombre().trim());
        c.setApellido(req.apellido().trim());
        c.setTelefono(req.telefono().trim());
        c.setCorreo(correo);
        c.setMotivo(req.motivo());
        c.setVehiculoInteres(limpiar(req.vehiculo()));
        c.setMensaje(limpiar(req.mensaje()));

        c = contactoDao.guardar(c);
        c.setCodigo(String.format("CT-%d-%05d", Year.now().getValue(), c.getId()));
        return ContactoResponse.from(c);
    }

    private static String limpiar(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
