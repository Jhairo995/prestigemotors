package pe.prestigemotors.service;

import java.time.Year;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.prestigemotors.dao.ClienteDao;
import pe.prestigemotors.dao.MantenimientoDao;
import pe.prestigemotors.dto.MantenimientoRequest;
import pe.prestigemotors.dto.MantenimientoResponse;
import pe.prestigemotors.model.Cliente;
import pe.prestigemotors.model.Mantenimiento;

@Service
public class MantenimientoService {

    private final ClienteDao clienteDao;
    private final MantenimientoDao mantenimientoDao;

    public MantenimientoService(ClienteDao clienteDao, MantenimientoDao mantenimientoDao) {
        this.clienteDao = clienteDao;
        this.mantenimientoDao = mantenimientoDao;
    }

    /** Registra la solicitud de servicio. Si el correo ya existe reutiliza al cliente; si no, lo crea. */
    @Transactional
    public MantenimientoResponse registrar(MantenimientoRequest req) {
        String correo = req.correo().trim().toLowerCase();

        Cliente cliente = clienteDao.buscarPorCorreo(correo).orElseGet(() -> {
            Cliente nuevo = new Cliente();
            nuevo.setNombre(req.nombre().trim());
            nuevo.setApellido(req.apellido().trim());
            nuevo.setCorreo(correo);
            return clienteDao.guardar(nuevo);
        });

        Mantenimiento m = new Mantenimiento();
        m.setCliente(cliente);
        m.setPropietario(req.propietario().trim());
        m.setVehiculo(req.modelo().trim());
        m.setPlaca(req.placa().trim().toUpperCase());
        m.setServicio(req.servicio());
        m.setObservaciones(req.observaciones() == null ? null : req.observaciones().trim());

        m = mantenimientoDao.guardar(m);
        m.setCodigo(String.format("MT-%d-%05d", Year.now().getValue(), m.getId()));
        return MantenimientoResponse.from(m);
    }
}
