# Trabajo Práctico N2 Laboratorio Spring Boot

Administrador de empleados y sus jornales de trabajo.

## Descripción

Este proyecto es una aplicación de administración de empleados y sus jornales de trabajo desarrollada con Spring Boot. El objetivo principal es gestionar las operaciones CRUD para empleados, manejar conceptos laborales y registrar jornadas de trabajo. La aplicación utiliza una base de datos en memoria H2 para simplificar la configuración y facilitar el desarrollo.

## Instalación

### Requisitos Previos

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Web](https://spring.io/projects/spring-web)
- [Lombok](https://projectlombok.org/)
- [DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)
- [H2 Database](https://www.h2database.com/html/main.html)
- [JPA (Java Persistence API)](https://www.oracle.com/java/technologies/persistence-jsp.html)
- [Validation](https://beanvalidation.org/)

### Pasos para la Instalación

1. Clona el repositorio: `git clone https://github.com/usuario/proyecto.git`
2. Navega al directorio del proyecto: `cd proyecto`
3. Construye el proyecto: `./mvnw clean install` (o `./gradlew build` si usas Gradle)
4. Inicia la aplicación: `./mvnw spring-boot:run` (o `./gradlew bootRun` si usas Gradle)

La aplicación usa una base de datos en memoria H2, por lo que no requiere configuración adicional para la base de datos.

## Uso

### Entidades

1. **Empleado**: Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la tabla de empleados.
2. **Concepto Laboral**: Cargada desde un archivo `data.sql`. El controlador solo admite el método GET para recuperar los conceptos laborales.
3. **Jornada**: Permite crear jornadas relacionando empleados con conceptos laborales. También ofrece la posibilidad de buscar jornadas por parámetros como DNI o fechas específicas.

### Ejemplos de Uso

- **Crear un Empleado**:
  ```bash
  POST /empleados
  Content-Type: application/json
  
  {
    "nombre": "Juan Pérez",
    "dni": "12345678",
    "fechaIngreso": "2024-09-01"
  }