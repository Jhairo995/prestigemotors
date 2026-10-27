package pe.prestigemotors.service;

import java.time.Year;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pe.prestigemotors.dao.ClienteDao;
import pe.prestigemotors.dao.PruebaManejoDao;
import pe.prestigemotors.dto.PruebaManejoRequest;
import pe.prestigemotors.dto.PruebaManejoResponse;
import pe.prestigemotors.model.Cliente;
import pe.prestigemotors.model.PruebaManejo;

@Service
public class PruebaManejoService {

    private final ClienteDao clienteDao;
    private final PruebaManejoDao pruebaDao;
    private final AlmacenArchivosPort almacen;

    public PruebaManejoService(ClienteDao clienteDao,
                               PruebaManejoDao pruebaDao,
                               AlmacenArchivosPort almacen) {
        this.clienteDao = clienteDao;
        this.pruebaDao = pruebaDao;
        this.almacen = almacen;
    }

    /** Registra la solicitud. Si el correo ya existe reutiliza al cliente; si no, lo crea. */
    @Transactional
    public PruebaManejoResponse registrar(PruebaManejoRequest req, MultipartFile licencia) {
        String correo = req.correo().trim().toLowerCase();

        // Primero se validan/guardan los archivos: si fallan, no se crea nada en la BD
        String licenciaArchivo = almacen.guardarLicencia(licencia);
        String firmaArchivo = almacen.guardarFirma(req.firma());

        Cliente cliente = clienteDao.buscarPorCorreo(correo).orElseGet(() -> {
            Cliente nuevo = new Cliente();
            nuevo.setNombre(req.nombre().trim());
            nuevo.setApellido(req.apellido().trim());
            nuevo.setCorreo(correo);
            return clienteDao.guardar(nuevo);
        });

        PruebaManejo p = new PruebaManejo();
        p.setCliente(cliente);
        p.setIdentificacion(req.identificacion().trim());
        p.setNombre(req.nombre().trim());
        p.setApellido(req.apellido().trim());
        p.setLicenciaArchivo(licenciaArchivo);
        p.setFirmaArchivo(firmaArchivo);
        p.setVehiculo(req.vehiculo().trim());
        p.setTiempoDeseado(req.tiempoDeseado().trim());
        p.setAceptaTerminos(req.aceptaTerminos());

        p = pruebaDao.guardar(p);
        p.setCodigo(String.format("PM-%d-%05d", Year.now().getValue(), p.getId()));
        return PruebaManejoResponse.from(p);
    }
}
