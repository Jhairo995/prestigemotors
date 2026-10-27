package pe.prestigemotors.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.prestigemotors.dto.ContactoRequest;
import pe.prestigemotors.dto.ContactoResponse;
import pe.prestigemotors.service.ContactoService;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoService service;

    public ContactoController(ContactoService service) {
        this.service = service;
    }

    /** Lo usa el formulario de contacto.html. Responde 201 con el código de la solicitud. */
    @PostMapping
    public ResponseEntity<ContactoResponse> registrar(@Valid @RequestBody ContactoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }
}
