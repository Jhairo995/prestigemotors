package pe.prestigemotors.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import pe.prestigemotors.dto.PruebaManejoRequest;
import pe.prestigemotors.dto.PruebaManejoResponse;
import pe.prestigemotors.service.PruebaManejoService;

@RestController
@RequestMapping("/api/pruebas-manejo")
public class PruebaManejoController {

    private final PruebaManejoService service;

    public PruebaManejoController(PruebaManejoService service) {
        this.service = service;
    }

    /** Lo usa test-drive.html (multipart: "datos" = JSON, "licencia" = archivo). Responde 201 con el código. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PruebaManejoResponse> registrar(
            @Valid @RequestPart("datos") PruebaManejoRequest datos,
            @RequestPart("licencia") MultipartFile licencia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(datos, licencia));
    }
}
