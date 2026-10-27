package pe.prestigemotors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.prestigemotors.dao.AutomovilDao;
import pe.prestigemotors.dto.AutomovilResponse;
import pe.prestigemotors.exception.RecursoNoEncontradoException;
import pe.prestigemotors.model.Automovil;
import pe.prestigemotors.model.EstadoAutomovil;

@ExtendWith(MockitoExtension.class)
class AutomovilServiceTest {

    @Mock
    private AutomovilDao automovilDao;

    @InjectMocks
    private AutomovilService service;

    private Automovil barato;
    private Automovil caro;
    private Automovil sinPrecio;

    @BeforeEach
    void setUp() {
        barato = auto("toyota-corolla", "Toyota Corolla", "15000.00", LocalDate.of(2024, 1, 10));
        caro = auto("porsche-911", "Porsche 911", "120000.00", LocalDate.of(2023, 1, 10));
        sinPrecio = auto("auto-consultar", "Auto por consultar", null, LocalDate.of(2022, 1, 10));
    }

    @Test
    void buscarOrdenaPorPrecioAscendenteYDejaLosPreciosNulosAlFinal() {
        when(automovilDao.buscarDisponibles(any())).thenReturn(List.of(caro, sinPrecio, barato));

        List<AutomovilResponse> resultado =
                service.buscar(null, null, null, null, null, null, null, "precio_asc");

        assertEquals(List.of("toyota-corolla", "porsche-911", "auto-consultar"),
                resultado.stream().map(AutomovilResponse::slug).toList());
    }

    @Test
    void buscarOrdenaPorPrecioDescendenteYDejaLosPreciosNulosAlFinal() {
        when(automovilDao.buscarDisponibles(any())).thenReturn(List.of(barato, sinPrecio, caro));

        List<AutomovilResponse> resultado =
                service.buscar(null, null, null, null, null, null, null, "precio_desc");

        assertEquals(List.of("porsche-911", "toyota-corolla", "auto-consultar"),
                resultado.stream().map(AutomovilResponse::slug).toList());
    }

    @Test
    void obtenerPorSlugDevuelveElVehiculoEncontrado() {
        when(automovilDao.buscarPorSlug("porsche-911")).thenReturn(Optional.of(caro));

        AutomovilResponse resultado = service.obtenerPorSlug("porsche-911");

        assertEquals("porsche-911", resultado.slug());
        assertEquals("Porsche 911", resultado.nombre());
        verify(automovilDao).buscarPorSlug("porsche-911");
    }

    @Test
    void obtenerPorSlugLanzaExcepcionCuandoNoExiste() {
        when(automovilDao.buscarPorSlug("inexistente")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.obtenerPorSlug("inexistente"));
    }

    private Automovil auto(String slug, String nombre, String precio, LocalDate fecha) {
        Automovil auto = new Automovil();
        auto.setSlug(slug);
        auto.setNombre(nombre);
        auto.setFechaIngreso(fecha);
        auto.setEstado(EstadoAutomovil.DISPONIBLE);
        auto.setPrecio(precio == null ? null : new BigDecimal(precio));
        return auto;
    }
}
