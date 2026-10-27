# Principios SOLID en Prestige Motors

Este documento identifica cómo se aplican los principios SOLID en el backend Java 17 con Spring Boot. Se incluyen cambios concretos en el código y oportunidades de extensión.

## 1. S — Responsabilidad única (Single Responsibility Principle)

Cada clase debe tener una responsabilidad principal.

- Los controladores (`controller/`) reciben las peticiones HTTP y devuelven respuestas.
- Los servicios (`service/`) coordinan las reglas del negocio y las transacciones.
- Los DAO (`dao/`) definen las operaciones de acceso a datos; sus implementaciones JPA realizan la persistencia.
- Los DTO (`dto/`) representan los datos de entrada y salida de la API.

**Beneficio:** un cambio en la presentación, las reglas de negocio o la persistencia afecta principalmente a su capa correspondiente.

## 2. O — Abierto/cerrado (Open/Closed Principle)

Las partes estables del sistema deben permitir agregar comportamientos sin tener que modificar código existente innecesariamente.

- Las interfaces DAO (`AutomovilDao`, `ClienteDao`, `MantenimientoDao`, entre otras) permiten incorporar nuevas implementaciones de persistencia.
- Para incorporar nuevos criterios de ordenamiento del catálogo, se recomienda extraer cada criterio a una estrategia independiente en vez de seguir ampliando el método `comparadorPara` de `AutomovilService`.

**Estado:** el acceso a datos ya permite extensiones mediante implementaciones; el ordenamiento del catálogo es un punto de mejora para aplicar OCP de forma más completa.

## 3. L — Sustitución de Liskov (Liskov Substitution Principle)

Una implementación debe poder sustituir a la interfaz que implementa sin romper el comportamiento esperado.

- `JpaGenericDao` y los DAO específicos implementan contratos de acceso a datos.
- `AlmacenArchivos` implementa `AlmacenArchivosPort`; por tanto, cualquier implementación alternativa debe respetar los contratos de `guardarLicencia` y `guardarFirma`, devolviendo la referencia del archivo guardado o comunicando el error correspondiente.

**Beneficio:** las implementaciones pueden sustituirse sin cambiar el servicio que las consume.

## 4. I — Segregación de interfaces (Interface Segregation Principle)

Las clases no deben depender de operaciones que no necesitan.

- `AlmacenArchivosPort` expone únicamente las dos operaciones de almacenamiento que necesita `PruebaManejoService`, en lugar de depender de una clase concreta con detalles internos de implementación.
- Los DAO específicos agregan consultas propias de cada entidad y heredan de `GenericDao` las operaciones CRUD comunes.

**Beneficio:** contratos pequeños y enfocados reducen el acoplamiento.

## 5. D — Inversión de dependencias (Dependency Inversion Principle)

Los módulos de alto nivel deben depender de abstracciones y no de implementaciones concretas.

- `PruebaManejoService` depende de `AlmacenArchivosPort`, no de `AlmacenArchivos`.
- Spring inyecta automáticamente la implementación `AlmacenArchivos`, ya que está anotada con `@Component` e implementa la interfaz.
- Los servicios dependen de interfaces DAO (`AutomovilDao`, `ClienteDao`, etc.), no de clases JPA ni de repositorios de Spring Data.

**Beneficio:** se puede reemplazar el almacenamiento local por almacenamiento en la nube o simularlo en pruebas sin cambiar la lógica de solicitudes de prueba de manejo.

## Ejemplo del cambio de dependencia

Antes, el servicio dependía directamente de una implementación:

```java
private final AlmacenArchivos almacen;

public PruebaManejoService(ClienteDao clienteDao,
                           PruebaManejoDao pruebaDao,
                           AlmacenArchivos almacen) {
    this.clienteDao = clienteDao;
    this.pruebaDao = pruebaDao;
    this.almacen = almacen;
}
```

Ahora depende de una abstracción:

```java
private final AlmacenArchivosPort almacen;

public PruebaManejoService(ClienteDao clienteDao,
                           PruebaManejoDao pruebaDao,
                           AlmacenArchivosPort almacen) {
    this.clienteDao = clienteDao;
    this.pruebaDao = pruebaDao;
    this.almacen = almacen;
}
```

La implementación actual continúa siendo `AlmacenArchivos`. Más adelante se puede crear otra implementación de `AlmacenArchivosPort` sin modificar `PruebaManejoService`.

## Verificación

- Compilar el proyecto con `mvn test`.
- Revisar que Spring detecte `AlmacenArchivos` como implementación de `AlmacenArchivosPort`.
- Para pruebas unitarias de `PruebaManejoService`, sustituir el almacenamiento real por un mock de `AlmacenArchivosPort`.

