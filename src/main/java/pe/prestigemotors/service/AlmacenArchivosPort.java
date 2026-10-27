package pe.prestigemotors.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Contrato para almacenar los archivos recibidos por la aplicacion.
 * Permite cambiar la estrategia de almacenamiento sin modificar los servicios.
 */
public interface AlmacenArchivosPort {

    String guardarLicencia(MultipartFile archivo);

    String guardarFirma(String dataUrl);
}
