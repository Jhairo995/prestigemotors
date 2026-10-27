package pe.prestigemotors.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Solicitud de prueba de manejo (formulario de test-drive.html). */
@Entity
@Table(name = "prueba_manejo")
public class PruebaManejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código para mostrar al cliente, p. ej. PM-2026-00012 */
    @Column(unique = true, length = 20)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /** DNI (8 dígitos) o RUC (11 dígitos). */
    @Column(nullable = false, length = 11)
    private String identificacion;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String apellido;

    /** Nombre del archivo guardado en la carpeta de uploads (no es público). */
    @Column(name = "licencia_archivo", nullable = false, length = 80)
    private String licenciaArchivo;

    @Column(name = "firma_archivo", nullable = false, length = 80)
    private String firmaArchivo;

    @Column(nullable = false, length = 120)
    private String vehiculo;

    @Column(name = "tiempo_deseado", nullable = false, length = 40)
    private String tiempoDeseado;

    @Column(name = "acepta_terminos", nullable = false)
    private boolean aceptaTerminos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrueba estado = EstadoPrueba.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getLicenciaArchivo() { return licenciaArchivo; }
    public void setLicenciaArchivo(String licenciaArchivo) { this.licenciaArchivo = licenciaArchivo; }

    public String getFirmaArchivo() { return firmaArchivo; }
    public void setFirmaArchivo(String firmaArchivo) { this.firmaArchivo = firmaArchivo; }

    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }

    public String getTiempoDeseado() { return tiempoDeseado; }
    public void setTiempoDeseado(String tiempoDeseado) { this.tiempoDeseado = tiempoDeseado; }

    public boolean isAceptaTerminos() { return aceptaTerminos; }
    public void setAceptaTerminos(boolean aceptaTerminos) { this.aceptaTerminos = aceptaTerminos; }

    public EstadoPrueba getEstado() { return estado; }
    public void setEstado(EstadoPrueba estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}
