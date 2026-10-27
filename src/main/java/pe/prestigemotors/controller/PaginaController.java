package pe.prestigemotors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de navegacion MVC.
 * Recibe las rutas de las paginas y delega la presentacion a las vistas estaticas.
 * La logica de negocio permanece en los servicios y el acceso a datos en los DAO.
 */
@Controller
public class PaginaController {

    @GetMapping("/")
    public String inicio() {
        return "forward:/index.html";
    }

    @GetMapping("/autos")
    public String autos() {
        return "forward:/AutoSubasta.html";
    }

    @GetMapping("/detalle")
    public String detalle() {
        return "forward:/detalle.html";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "forward:/contacto.html";
    }

    @GetMapping("/test-drive")
    public String testDrive() {
        return "forward:/test-drive.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "forward:/registro.html";
    }
}
