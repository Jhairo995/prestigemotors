package pe.prestigemotors.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import pe.prestigemotors.exception.ArchivoInvalidoException;

/** Guarda archivos subidos en disco (carpeta app.uploads.dir). Esa carpeta NO se publica por HTTP. */
@Component
public class AlmacenArchivos implements AlmacenArchivosPort {

    private static final Map<String, String> TIPOS_LICENCIA = Map.of(
            "image/jpeg", ".jpg", "image/png", ".png", "image/webp", ".webp", "application/pdf", ".pdf");
    private static final int MAX_FIRMA_BYTES = 500 * 1024;

    private final Path raiz;

    public AlmacenArchivos(@Value("${app.uploads.dir:uploads}") String dir) {
        this.raiz = Path.of(dir).toAbsolutePath().normalize();
    }

    public String guardarLicencia(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoInvalidoException("Sube la imagen de tu licencia de conducir");
        }
        String ext = TIPOS_LICENCIA.get(archivo.getContentType());
        if (ext == null) {
            throw new ArchivoInvalidoException("La licencia debe ser una imagen (JPG, PNG, WEBP) o un PDF");
        }
        try {
            return escribir("licencia-", ext, archivo.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar la licencia", e);
        }
    }

    /** Recibe un data URL "data:image/png;base64,...." y lo guarda como PNG. */
    public String guardarFirma(String dataUrl) {
        String prefijo = "data:image/png;base64,";
        if (dataUrl == null || !dataUrl.startsWith(prefijo)) {
            throw new ArchivoInvalidoException("La firma digital no es válida");
        }
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(dataUrl.substring(prefijo.length()));
        } catch (IllegalArgumentException e) {
            throw new ArchivoInvalidoException("La firma digital no es válida");
        }
        if (bytes.length == 0 || bytes.length > MAX_FIRMA_BYTES) {
            throw new ArchivoInvalidoException("La firma digital no es válida o es demasiado grande");
        }
        return escribir("firma-", ".png", bytes);
    }

    private String escribir(String prefijo, String ext, byte[] datos) {
        try {
            Files.createDirectories(raiz);
            String nombre = prefijo + UUID.randomUUID() + ext;
            Files.write(raiz.resolve(nombre), datos);
            return nombre;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo", e);
        }
    }
}
