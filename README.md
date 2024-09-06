# Trabajo Práctico N2 API Turnos Rotativos con Spring Boot

API para la administración de empleados y sus jornadas de trabajo.

## Descripción

Este proyecto es una aplicación sencilla desarrollada con Spring Boot para gestionar empleados, conceptos laborales y sus jornadas de trabajo. La app permite realizar operaciones CRUD sobre los empleados, manejar conceptos laborales predefinidos y registrar las horas trabajadas por cada empleado. Utiliza una base de datos en memoria H2, lo que simplifica la configuración y el uso.

### Dependencias Utilizadas

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Web](https://spring.io/projects/spring-web)
- [Lombok](https://projectlombok.org/)
- [DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)
- [H2 Database](https://www.h2database.com/html/main.html)
- [JPA (Java Persistence API)](https://www.oracle.com/java/technologies/persistence-jsp.html)
- [Validation](https://beanvalidation.org/)

### Pasos para la Instalación

1. Clona este repositorio: `git clone https://github.com/LucasVila02/tp-api-lab.git`
2. Ve al directorio del proyecto: `cd tp-api`
3. Inicia la aplicación: `./mvnw spring-boot:run` 

La aplicación usa una base de datos en memoria H2, por lo que no requiere configuración adicional para la base de datos.

## Uso

### Entidades

1. **Empleado**: Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la tabla de empleados.
2. **Concepto Laboral**: Cargada desde un archivo `data.sql`. El controlador solo admite el método GET para recuperar los conceptos laborales.
3. **Jornada**: Permite crear jornadas relacionando empleados con conceptos laborales. También ofrece la posibilidad de buscar jornadas por parámetros como DNI o fechas específicas.

## Colección Postman .Json

Dentro de la carpeta del proyecto encontrarás una colección de Postman en formato .json, que incluye todas las solicitudes necesarias para interactuar con la API. En esta colección podrás:

1. **Empleados:**
   * Crear dos empleados: empleadoUno y empleadoDos.
   * Obtener un listado de todos los empleados o filtrar por ID.
   * Actualizar la información de un empleado.
   * Eliminar un empleado.


2. **Conceptos Laborales:**
   * Obtener un listado de todos los conceptos, o filtrar por  ID o nombre.

3. **Jornadas:**
   * Crear jornadas de trabajo para empleadoUno y empleadoDos.
   * Obtener un listado de todas las jornadas, o filtrar por parámetros combinados como DNI, fechaDesde y fechaHasta.

## Test Junit & Mock
Se hizo test unitarios en todos los métodos de los services de las diferentes entidades.
