# Prestige Motors – Backend (Spring Boot + MySQL)

Frontend HTML + backend Java en un solo proyecto. Spring Boot sirve tus páginas desde
`src/main/resources/static/` y expone la API en `/api/...` (mismo servidor, sin CORS).

## 1. Cómo ejecutarlo

1. Abre **XAMPP** y arranca **MySQL** (no hace falta crear la base de datos: se crea sola).
2. Abre la carpeta como proyecto **Maven** en tu IDE (IntelliJ / Eclipse / NetBeans / VS Code). Requiere **Java 17**.
3. Si tu MySQL tiene clave, cámbiala en `src/main/resources/application.properties`
   (por defecto: usuario `root`, sin clave).
4. Ejecuta `PrestigeMotorsApplication` (o `mvn spring-boot:run`).
5. Abre **http://localhost:8080/index.html**

> Importante: abre las páginas desde `localhost:8080`, no con doble clic ni con Live Server,
> porque el HTML llama a `/api/...` en el mismo servidor.

La primera vez se crean las tablas y se cargan los 25 vehículos de tu `autos.js`
(desde `src/main/resources/seed/autos.json`).

## 2. Endpoints

| Método | Ruta | Para qué |
|---|---|---|
| GET | `/api/autos` | Catálogo (AutoSubasta). Filtros opcionales: `q`, `tipo`, `marca`, `modelo`, `anio`, `min`, `max`, `orden` |
| GET | `/api/autos/{slug}` | Detalle de un vehículo (`detalle.html?id=porsche-911`) |
| POST | `/api/mantenimientos` | Registrar solicitud de servicio (`registro.html`) |
| POST | `/api/contactos` | Solicitar atención de un asesor (`contacto.html`) |
| POST | `/api/pruebas-manejo` | Solicitar prueba de manejo (`test-drive.html`), multipart |

Ejemplos:

```
GET /api/autos?tipo=Camioneta&orden=precio_desc
GET /api/autos?q=porsche&anio=2021&min=100000&max=300000
GET /api/autos/porsche-911
```

`orden`: `fecha_desc` (por defecto), `fecha_asc`, `precio_asc`, `precio_desc`.

`POST /api/mantenimientos` (JSON):

```json
{
  "nombre": "Jane", "apellido": "Smitherton", "correo": "jane@correo.com",
  "propietario": "Jane Smitherton", "modelo": "Ford Ranger Raptor",
  "placa": "ABC-123", "servicio": "CAMBIO_ACEITE", "observaciones": "Ruido al frenar"
}
```

Respuesta `201`: `{ "codigo": "MT-2026-00001", "estado": "PENDIENTE", "fechaSolicitud": "..." }`
Si hay datos inválidos responde `400` con `{ "mensaje": "...", "errores": { "placa": "..." } }`.

## 3. Estructura

```
src/main/java/pe/prestigemotors/
 ├─ controller/   AutomovilController, MantenimientoController, ContactoController, PruebaManejoController
 ├─ service/      AutomovilService (filtros y orden), MantenimientoService, ContactoService, PruebaManejoService
 ├─ dao/          Interfaces DAO: GenericDao, AutomovilDao, ClienteDao, ContactoDao, MantenimientoDao, PruebaManejoDao + FiltroAutomovil
 │   └─ jpa/      Implementaciones JPA (@Repository): JpaGenericDao y una *DaoJpa por entidad
 ├─ repository/   Repositorios de Spring Data (detalle interno de los DAO JPA)
 ├─ model/        Automovil, Cliente, Usuario, Venta, Mantenimiento, Contacto, PruebaManejo + enums
 ├─ dto/          Request/Response de cada formulario y del catálogo
 ├─ exception/    ManejadorExcepciones (errores en JSON)
 └─ config/       DataLoader (carga inicial del catálogo)
```

### Principios de DAO aplicados

- **Abstracción:** los servicios dependen de interfaces (`ClienteDao`, `AutomovilDao`...), nunca de JPA ni de Spring Data.
- **Un DAO por entidad:** cada interfaz extiende `GenericDao<T, ID>` (CRUD común) y añade solo sus consultas propias (`buscarPorCorreo`, `buscarPorSlug`, `buscarDisponibles`).
- **Persistencia aislada:** la construcción de consultas (Criteria/Specification del catálogo) vive en `AutomovilDaoJpa`; el servicio solo arma un `FiltroAutomovil`.
- **Sin lógica de negocio en el DAO:** códigos (`MT-2026-00001`), normalización de datos y "reutilizar o crear cliente" siguen en los servicios.
- **Transacciones en el servicio:** `@Transactional` se queda en la capa de servicio; los DAO se unen a esa transacción.
- **Intercambiable:** para cambiar de tecnología basta una nueva implementación de la interfaz, sin tocar servicios ni controladores.
- **Flujo:** `Controller → Service → DAO (interfaz) → DAO JPA → Repository → MySQL`.

## Principios SOLID

Se incorporó `AlmacenArchivosPort` para que `PruebaManejoService` dependa de una abstracción y no de la implementación concreta de almacenamiento (DIP). La interfaz también mantiene un contrato pequeño (ISP), y permite sustituir el almacenamiento local por otra implementación (OCP/LSP).

La explicación de los cinco principios, su relación con la arquitectura y las oportunidades de mejora está en `SOLID.md`.

## 4. Qué cambió en el frontend

- `AutoSubasta.html`: la lista ya no está escrita a mano; se llena con `GET /api/autos`. Funcionan el buscador, el rango de precios, el orden y el contador de vehículos.
- `detalle.html`: lee el vehículo con `GET /api/autos/{slug}` (ya no usa `autos.js`, que se eliminó).
- `registro.html`: el formulario envía `POST /api/mantenimientos` y muestra el código de la solicitud o los errores.
- `css.css`: se añadieron estilos para los mensajes de estado al final del archivo.
- `index.html` no se tocó; sus enlaces a `detalle.html?id=...` ya funcionan con la API.

## 5. Para revisar

- **Columnas de las entidades:** `Cliente`, `Usuario`, `Venta` y `Mantenimiento` siguen las entidades de tu ER, pero los atributos exactos son supuestos. Ajústalos a tu diagrama.
- **Imágenes faltantes:** 7 vehículos de AutoSubasta (`mercedes-cla`, `tesla-model-s`, `bugatti-chiron`, `aston-martin-rapide`, `mercedes-s580`, `rolls-royce-wraith`, `mazda-6`) apuntan a imágenes que no están en `img/`; ya pasaba en tu HTML original.
- **Datos repetidos:** los precios, motores y kilometrajes de varios autos son los de relleno de tu `autos.js`.

## 6. Página de contacto (`contacto.html`)

- Todos los botones **Contactar asesor** apuntan a `contacto.html`; en `detalle.html` el botón envía el vehículo: `contacto.html?vehiculo=Porsche 911`.
- El formulario hace `POST /api/contactos` con `nombre, apellido, telefono, correo (opcional), motivo, vehiculo, mensaje`. `motivo`: `COMPRAR_VEHICULO, VENDER_VEHICULO, TEST_DRIVE, SUBASTA, SERVICIO_POSTVENTA, OTRO`.
- Respuesta `201`: `{ "codigo": "CT-2026-00001", "estado": "NUEVO", "fechaSolicitud": "..." }`. Errores `400` con el mismo formato que `/api/mantenimientos`.
- Si se deja correo, se reutiliza o crea el `Cliente` (igual que en mantenimientos). Tabla nueva: `contacto` (se crea sola).

## 7. Prueba de manejo (`test-drive.html`)

- Todos los enlaces **Test Drive** apuntan a `test-drive.html`; en `detalle.html` el botón **Probar este auto** envía el modelo (`?vehiculo=...`).
- `POST /api/pruebas-manejo` es **multipart**: parte `datos` (JSON: `identificacion, nombre, apellido, correo, vehiculo, tiempoDeseado, aceptaTerminos, firma`) y parte `licencia` (JPG/PNG/WEBP/PDF, máx. 5 MB). `firma` es el PNG del canvas en data URL.
- Respuesta `201`: `{ "codigo": "PM-2026-00001", "estado": "PENDIENTE", "fechaSolicitud": "..." }`. Errores `400` con el mismo formato de siempre.
- La licencia y la firma se guardan en la carpeta `uploads/` (`app.uploads.dir`), que **no** se sirve por HTTP. Tabla nueva: `prueba_manejo`.

## 8. Pruebas automatizadas y principios TDD

El proyecto incorpora una base de pruebas automatizadas con **JUnit 5**, **Mockito** y
`spring-boot-starter-test`. Las pruebas de `AutomovilService` verifican el ordenamiento
por precio ascendente/descendente, que los vehículos sin precio aparezcan al final,
la consulta de un vehículo por `slug` y el error cuando no existe.

### Principios TDD (Red – Green – Refactor)

1. **Red:** escribir primero una prueba que describa el comportamiento esperado y comprobar
   que falla cuando la funcionalidad aún no existe o no cumple el requisito.
2. **Green:** implementar el mínimo código necesario para que la prueba pase.
3. **Refactor:** mejorar el diseño y la legibilidad sin cambiar el comportamiento, ejecutando
   de nuevo las pruebas después de cada cambio.
4. **Pruebas pequeñas y enfocadas:** cada prueba comprueba un comportamiento concreto.
5. **Aislamiento:** Mockito simula `AutomovilDao`, de modo que las pruebas del servicio no
   necesitan conectarse a MySQL ni dependen de los datos reales.
6. **Repetibilidad:** las pruebas usan datos controlados y fechas fijas para producir resultados
   consistentes.

### Ejecutar las pruebas

Desde la carpeta raíz del proyecto, donde está `pom.xml`:

```bash
mvn test
```

En IntelliJ IDEA también puedes abrir `AutomovilServiceTest.java` y ejecutar la clase o cada
método de prueba con el icono de ejecución. Antes de integrar un cambio, ejecuta la suite y
confirma que todas las pruebas pasen.

> Nota: estas pruebas cubren la lógica del servicio de catálogo. Para ampliar la cobertura,
> aplica el mismo ciclo TDD a validaciones de formularios, solicitudes de mantenimiento,
> contactos y pruebas de manejo.
