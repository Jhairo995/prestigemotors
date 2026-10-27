# Arquitectura MVC en Prestige Motors

El proyecto aplica una separacion por responsabilidades inspirada en MVC:

- **Modelo (Model):** `model/` contiene las entidades y estados del dominio, por ejemplo `Automovil`, `Cliente`, `Mantenimiento` y `PruebaManejo`.
- **Vista (View):** `src/main/resources/static/` contiene las paginas HTML y sus recursos visuales. Los controladores REST entregan DTO como JSON para que el frontend los consuma.
- **Controlador (Controller):** `controller/` recibe las solicitudes HTTP. `PaginaController` enruta las paginas web y los controladores REST (`AutomovilController`, `ContactoController`, etc.) exponen los endpoints.
- **Servicio:** `service/` contiene reglas y operaciones de negocio; los controladores no deben implementar esa logica.
- **Acceso a datos:** `dao/`, `dao/jpa/` y `repository/` encapsulan las consultas y persistencia.

## Rutas de paginas

- `/` -> `index.html`
- `/autos` -> `AutoSubasta.html`
- `/detalle` -> `detalle.html`
- `/contacto` -> `contacto.html`
- `/test-drive` -> `test-drive.html`
- `/registro` -> `registro.html`

Los endpoints existentes bajo `/api/...` se mantienen para no romper las llamadas del frontend. El controlador de paginas usa `forward:` para servir los HTML existentes sin duplicarlos ni moverlos.

## Flujo de una solicitud de datos

`Vista (HTML/JS) -> Controlador REST -> Servicio -> DAO/Repositorio -> Modelo/Base de datos`

La respuesta vuelve como DTO/JSON al frontend. Esto mantiene separadas la presentacion, la logica de negocio y la persistencia.
