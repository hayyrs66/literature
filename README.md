
# LibroService - Aplicación de Búsqueda de Libros
LibroService es una aplicación Java que permite buscar libros por título o autor en una base de datos local y, si no se encuentran, realiza consultas a la API de Gutendex para obtener resultados adicionales.




## Funcionalidades

- Buscar por Título: Permite buscar libros en la base de datos local por título. Si no se encuentran resultados, se consulta la API de Gutendex.

- Buscar por Autor: Busca libros en la base de datos local por autor. Si no se encuentran resultados, realiza una consulta a la API de Gutendex.

- Mostrar Todos: Muestra todos los libros almacenados en la base de datos local.

## Requisitos Previos

- Java 8 o superior
- Maven
- Acceso a Internet para consultar la API de Gutendex

```bash
  git clone <url-del-repositorio>
  cd LibroService
```

Asegúrate de tener una base de datos configurada y accesible. La configuración de la base de datos se encuentra en application.properties en el proyecto.

Compilar y Ejecutar:

```bash
   mvn clean package
   java -jar target/LibroService-1.0.jar

```
    
