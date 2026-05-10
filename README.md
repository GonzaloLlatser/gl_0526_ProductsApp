# Products App

API REST para gestionar productos y sus precios historicos.

Version actual: `1.4.0-SNAPSHOT`.

## Funcionalidad

La aplicacion implementa los requisitos base de la prueba tecnica y los bonus documentados en la seccion de versiones:

- Crear productos.
- Agregar precios historicos a un producto.
- Validar que no existan precios solapados para el mismo producto.
- Consultar el precio vigente de un producto para una fecha.
- Consultar el historial completo de precios de un producto.
- Gestionar errores de validacion, producto no encontrado y precio no encontrado.
- Soportar moneda por precio.
- Actualizar precios existentes manteniendo reglas de validacion y solape.

Los rangos de precio se tratan como intervalos cerrados: `initDate` y `endDate` son inclusivos. Cuando `endDate` es `null`, el precio queda vigente indefinidamente desde `initDate`.

## Stack

- Java 21
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- H2
- Lombok
- Spring Validation
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- Mockito
- RestAssured

## Justificacion Del Stack

- Java 21 y Spring Boot permiten construir una API REST sencilla, mantenible y con arranque rapido.
- Spring Web cubre la exposicion HTTP de los endpoints requeridos.
- Spring Data JPA simplifica el acceso a datos sin acoplar la aplicacion a consultas JDBC manuales.
- H2 permite ejecutar y revisar la prueba sin dependencias externas.
- Lombok reduce boilerplate en modelos y entidades.
- Spring Validation permite validar DTOs de entrada de forma declarativa.
- Springdoc expone Swagger UI y documentacion OpenAPI runtime.
- JUnit 5 y Mockito permiten tests unitarios aislados de los casos de uso.
- RestAssured permite tests funcionales automatizados consumiendo la API por HTTP.

## Arquitectura

El proyecto usa arquitectura hexagonal:

```text
src/main/java/com/gft/products
|-- application
|   |-- port
|   |   |-- in
|   |   `-- out
|   `-- service
|-- domain
|   `-- model
`-- infrastructure
    `-- adapter
        |-- in
        |   `-- rest
        |       |-- controller
        |       |-- dto
        |       `-- mapper
        `-- out
            `-- persistence
                |-- entity
                |-- jparepository
                `-- repositoryadapter
```

Los puertos son interfaces. Los servicios de `application/service` implementan los casos de uso. La capa `domain/model` contiene modelos simples. La infraestructura contiene los adaptadores REST y persistencia.

## Endpoints

### Crear producto

```http
POST /products
```

```json
{
  "name": "Zapatillas deportivas",
  "description": "Modelo 2025 edicion limitada"
}
```

### Agregar precio

```http
POST /products/{id}/prices
```

```json
{
  "value": 99.99,
  "currency": "EUR",
  "initDate": "2024-01-01",
  "endDate": "2024-06-30"
}
```

### Actualizar precio

```http
PUT /products/{productId}/prices/{priceId}
```

```json
{
  "value": 109.99,
  "currency": "USD",
  "initDate": "2024-01-01",
  "endDate": "2024-06-30"
}
```

### Consultar precio vigente

```http
GET /products/{id}/prices?date=2024-04-15
```

Respuesta:

```json
{
  "value": 99.99,
  "currency": "EUR"
}
```

### Consultar historial de precios

```http
GET /products/{id}/prices
```

Respuesta:

```json
{
  "name": "Zapatillas deportivas",
  "description": "Modelo 2025 edicion limitada",
  "prices": [
    {
      "value": 99.99,
      "currency": "EUR",
      "initDate": "2024-01-01",
      "endDate": "2024-06-30"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

El historial soporta paginacion y ordenacion:

```http
GET /products/{id}/prices?page=0&size=10&sort=initDate,asc
```

Campos de ordenacion soportados: `initDate`, `endDate`, `value`. Direcciones soportadas: `asc`, `desc`.

## Ejecutar

Compilar:

```powershell
mvn clean compile
```

Ejecutar:

```powershell
mvn spring-boot:run
```

La aplicacion queda disponible en:

```text
http://localhost:8080
```

Si `mvn spring-boot:run` falla por la ruta local con espacios o acentos, se puede arrancar con Java directo:

```powershell
mvn -DskipTests compile
mvn -q -DskipTests dependency:build-classpath "-Dmdep.outputFile=target\classpath.txt"
$cp = Get-Content target\classpath.txt
java -cp "target\classes;$cp" com.gft.products.ProductsApplication
```

Para usar otro puerto:

```powershell
java -cp "target\classes;$cp" com.gft.products.ProductsApplication --server.port=8081
```

## Tests

Ejecutar toda la suite:

```powershell
mvn test
```

Los tests son automatizados y se ejecutan dentro del ciclo Maven con `mvn test`.

La suite incluye:

- Tests unitarios de servicios de aplicacion.
- Tests unitarios del mapper REST.
- Tests funcionales/E2E con RestAssured, levantando Spring Boot en un puerto aleatorio y consumiendo la API por HTTP.

Los E2E cubren:

- Crear producto correctamente.
- Agregar precio correctamente.
- Rechazar precio con rango invalido.
- Rechazar precio solapado.
- Obtener precio vigente por fecha.
- Obtener `404` si no hay precio vigente.
- Obtener historial completo.
- Obtener historial paginado y ordenado.

## Base De Datos

Se usa H2 en memoria.

Consola H2:

```text
http://localhost:8080/h2-console
```

Datos de conexion:

```text
JDBC URL: jdbc:h2:mem:products
User Name: sa
Password:
```

Tambien se puede usar la URL completa:

```text
jdbc:h2:mem:products;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```

Las tablas se crean con `src/main/resources/schema.sql` y los datos iniciales se cargan con `src/main/resources/data.sql`.

La carga de datos de prueba es automatica al arrancar la aplicacion porque `spring.sql.init.mode=always` ejecuta ambos scripts. El seed incluye varios productos, historiales con rangos cerrados y precios vigentes sin fecha fin para facilitar la revision manual desde API o H2.

Los precios seed usan varias monedas (`EUR`, `USD`, `GBP`) para facilitar la revision del bonus de moneda.

Para probar paginacion manualmente se puede usar el producto seed `6`, que contiene 24 precios historicos:

```http
GET /products/6/prices?page=0&size=10&sort=initDate,asc
GET /products/6/prices?page=1&size=10&sort=initDate,asc
GET /products/6/prices?page=0&size=5&sort=value,desc
```

## Swagger / OpenAPI

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI runtime:

```text
http://localhost:8080/v3/api-docs
```

Contrato OpenAPI estatico:

```text
docs/openapi/products-api.yaml
```

Este YAML es el contrato API-first versionado del proyecto. El endpoint `/v3/api-docs` es la documentacion runtime generada por Springdoc a partir de los controllers.

## Decisiones Tecnicas

- Se usa arquitectura hexagonal para separar casos de uso, dominio e infraestructura.
- Se usa H2 por simplicidad de ejecucion local y para facilitar la revision.
- Se usa JPA con `schema.sql` y `data.sql`; Hibernate no crea el esquema automaticamente.
- Se usa mapper manual porque la transformacion DTO/domain es simple y no justifica una dependencia adicional.
- Los controllers se mantienen finos y delegan en puertos de entrada.
- Las excepciones de aplicacion se traducen a respuestas consistentes mediante `RestExceptionHandler`.
- No se implementan endpoints no requeridos para mantener el codigo minimo.

## Versiones

### 1.4.0

- Bonus: endpoint para actualizar precios existentes.
- Validacion de valor, moneda, rango de fechas y solape excluyendo el propio precio.

### 1.3.0

- Bonus: soporte de moneda por precio mediante codigo de 3 letras.
- No se implementa conversion de divisas ni tipos de cambio.

### 1.2.0

- Bonus: paginacion y ordenacion en el historial de precios.
- Producto seed con historial largo para revision manual.
- Tests E2E independientes para validar paginacion y ordenacion.

### 1.1.0

- Bonus: scripts automaticos de esquema y datos de prueba con `schema.sql` y `data.sql`.
- Documentacion explicita del seed inicial y de su ejecucion automatica.

### 1.0.0

- Requisitos funcionales base.
- Arquitectura hexagonal.
- Persistencia H2/JPA.
- Validaciones y gestion centralizada de errores.
- OpenAPI/Swagger UI.
- Tests unitarios y E2E.
