# EnviExpress - Sistema de Gestión Logística

Este proyecto es una solución de software escalable orientada a microservicios para la gestión logística (administración, usuarios, clientes y paquetes). Está diseñado con un enfoque reactivo para garantizar un alto rendimiento y un manejo eficiente de la concurrencia.

## Stack Tecnológico

El proyecto está dividido en dos capas principales, construidas con las siguientes tecnologías:

**Backend (Microservicios)**

* **Java 17**
* **Spring Boot**
* **Spring WebFlux & Project Reactor (RxJava):** Para el manejo de programación funcional y reactiva, asegurando flujos de datos no bloqueantes.
* **Maven:** Gestor de dependencias y construcción del proyecto.
* **Manejo Global de Excepciones:** Implementado transversalmente para respuestas de error estandarizadas.

**Frontend**

* **Angular v21**
* **TypeScript**
* **Tailwind CSS:** Para el diseño ágil y responsivo.
* **Arquitectura Standalone:** Componentes independientes sin `NgModules` para optimizar el peso de la aplicación y mejorar los tiempos de carga.

**Base de Datos e Infraestructura**

* **MongoDB:** Base de datos NoSQL.
* **VPS Propio:** Alojamiento de la base de datos en un servidor privado para un acceso dinámico, seguro y centralizado.

---

## Arquitectura de Microservicios

El backend está compuesto por 4 microservicios independientes. Cada uno expone su propia API y corre en un puerto específico de la máquina anfitriona:

| Microservicio      | Módulo           | Puerto | Descripción                                                                      |
| ---                | ---              | ---    | ---                                                                              |
| **Administración** | `admmodule`      | `8080` | Gestión de catálogos y configuraciones globales (ubicaciones, estados, menús).   |
| **Usuarios**       | `usermodule`     | `8081` | Gestión de identidades, autenticación y roles de acceso.                         |
| **Clientes**       | `clientmodule`   | `8082` | Administración del directorio de clientes remitentes.                            |
| **Paquetes**       | `packmodule`     | `8083` | Núcleo logístico: creación de guías, hojas de ruta y estados operativos.         |

---

## Estructura del Proyecto (Backend)

Todos los microservicios siguen un patrón de diseño estandarizado basado en capas limpias para garantizar el mantenimiento y la escalabilidad del código:

```text
module
 └── module
      ├── configuration    # Configuraciones globales (CORS, WebClient, etc.)
      ├── controller       # Endpoints REST (Controladores Reactivos)
      ├── model            # Entidades de base de datos y DTOs
      ├── repository       
      │    └── itf         # Interfaces de conexión a MongoDB (ReactiveMongoRepository)
      ├── service          
      │    ├── imp         # Lógica de negocio (Implementación)
      │    └── itf         # Contratos de los servicios (Interfaces)
      └── utils            
           ├── exception   # Manejo de errores personalizados y globales
           │    └── type
           ├── logging     # Trazabilidad y registros
           │    └── type
           └── properties  # Constantes y utilidades estáticas

```

---

## Configuración de Base de Datos

La configuración principal para la conexión a MongoDB y las propiedades del servidor se definen en el archivo `src/main/resources/application.properties` de cada microservicio:

```properties
# Server Port (Ejemplo para admmodule)
server.port=8080

# Configuracion de MongoDB
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=admin
spring.data.mongodb.password=TU_PASSWORD
spring.data.mongodb.host=191.96.31.136
spring.data.mongodb.port=27017
spring.data.mongodb.database=enviexpressdb
spring.data.mongodb.uri=mongodb://admin:TU_PASSWORD@191.96.31.136:27969/enviexpressdb?authSource=admin

# Propiedades de HttpClientConfiguration (Timeouts reactivos)
admmodule.httpClient.connectTimeout: 30000 
admmodule.httpClient.readTimeout: 50000   

```
NOTA 1: Para realizar una validación se seguridad no se trabajó en el puerto 27017 que por defecto es el de mongodb, sino que se habilitó un puerto especial, para este caso el 27969
NOTA 2: El password para la bd se dará a solicitud

---

##  Instalación y Despliegue

### Requisitos Previos

* Java Development Kit (JDK) 17
* Apache Maven
* Node.js (LTS recomendado)
* Angular CLI v21 (`npm install -g @angular/cli@21`)

### Entorno Frontend

1. Clonar el repositorio.
2. Navegar al directorio del frontend.
3. Instalar las dependencias de Node:
```bash
npm install

```


4. Levantar el servidor de desarrollo:
```bash
ng serve

```



### Entorno Backend

Para levantar los microservicios de Java, navega a la raíz de cada módulo (ej. `/admmodule`) y ejecuta:

```bash
mvn clean install
mvn spring-boot:run

```

*(Repetir el proceso para cada uno de los 4 microservicios de java).*

### Edwar Yeison Vidal - yedward960929@gmail.com
