package pe.prestigemotors.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Solicitud de atención de un asesor (formulario de contacto.html). */
@Entity
@Table(name = "contacto")
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código para mostrar al cliente, p. ej. CT-2026-00012 */
    @Column(unique = true, length = 20)
    private String codigo;

    /** Solo si el visitante dejó correo: se enlaza (o crea) su Cliente. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String apellido;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 120)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MotivoContacto motivo;

    @Column(name = "vehiculo_interes", length = 120)
    private String vehiculoInteres;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoContacto estado = EstadoContacto.NUEVO;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public MotivoContacto getMotivo() { return motivo; }
    public void setMotivo(MotivoContacto motivo) { this.motivo = motivo; }

    public String getVehiculoInteres() { return vehiculoInteres; }
    public void setVehiculoInteres(String vehiculoInteres) { this.vehiculoInteres = vehiculoInteres; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public EstadoContacto getEstado() { return estado; }
    public void setEstado(EstadoContacto estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}
