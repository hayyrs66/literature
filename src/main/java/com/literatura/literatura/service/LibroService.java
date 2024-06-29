package com.literatura.literatura.service;

import com.google.gson.Gson;
import com.literatura.literatura.model.Autor;
import com.literatura.literatura.model.Libro;
import com.literatura.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private static final String API_URL = "https://gutendex.com/books?search=";

    @Autowired
    private LibroRepository libroRepository;

    private final HttpClient httpClient;
    private final Gson gson;

    public LibroService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        try {
            List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);
            if (libros.isEmpty()) {
                System.out.println("No se encontró en la base de datos local. Buscando en Gutendex...");
                libros = fetchFromApi("title=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8));
                if (!libros.isEmpty()) {
                    libroRepository.saveAll(libros);
                } else {
                    System.out.println("No se encontraron libros con el título: " + titulo);
                }
            } else {
                System.out.println("Se encontraron los siguientes libros en la base de datos local:");
                libros.forEach(libro -> System.out.println(libro.toString()));
            }
            return libros;
        } catch (Exception e) {
            System.out.println("Error al buscar libros por título: " + e.getMessage());
            return Collections.emptyList(); // Retorna una lista vacía en caso de error
        }
    }

    public List<Libro> buscarPorAutor(String autor) {
        try {
            List<Libro> libros = libroRepository.findByAutoresNombreContainingIgnoreCase(autor);
            if (libros.isEmpty()) {
                System.out.println("No se encontró en la base de datos local. Buscando en Gutendex...");
                libros = fetchFromApi("author=" + URLEncoder.encode(autor, StandardCharsets.UTF_8));
                if (!libros.isEmpty()) {
                    libroRepository.saveAll(libros);
                } else {
                    System.out.println("No se encontraron libros con el autor: " + autor);
                }
            } else {
                System.out.println("Se encontraron los siguientes libros en la base de datos local:");
                libros.forEach(libro -> System.out.println(libro.toString()));
            }
            return libros;
        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor: " + e.getMessage());
            return Collections.emptyList(); // Retorna una lista vacía en caso de error
        }
    }

    public List<Libro> mostrarTodos() {
        return libroRepository.findAll();
    }

    private List<Libro> fetchFromApi(String query) {
        try {
            String url = API_URL + URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200) {
                ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
                List<Libro> libros = apiResponse.toLibros();
                System.out.println("Se encontraron los siguientes libros en Gutendex:");
                libros.forEach(libro -> System.out.println(libro.toString()));
                return libros;
            } else if (statusCode == 301 || statusCode == 302) {
                String newLocation = response.headers().firstValue("Location").orElseThrow(() ->
                        new RuntimeException("Received redirect response without location header"));

                System.out.println("Received " + statusCode + " redirect to: " + newLocation);
                return fetchFromApiFromNewLocation(newLocation);
            } else {
                throw new RuntimeException("Failed to fetch data from API: " + statusCode);
            }
        } catch (Exception e) {
            System.out.println("Error while fetching data from API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Libro> fetchFromApiFromNewLocation(String newLocation) {
        try {
            String url = "https://gutendex.com" + newLocation; // Construct the full URL from the new location

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
                List<Libro> libros = apiResponse.toLibros();
                System.out.println("Se encontraron los siguientes libros en Gutendex:");
                libros.forEach(libro -> System.out.println(libro.toString()));
                return libros;
            } else {
                throw new RuntimeException("Failed to fetch data from API: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error while fetching data from API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static class ApiResponse {
        private int count;
        private String next;
        private String previous;
        private List<Book> results;

        public List<Libro> toLibros() {
            return results.stream().map(Book::toLibro).collect(Collectors.toList());
        }
    }

    private static class Book {
        private int id;
        private String title;
        private List<Person> authors;

        public Libro toLibro() {
            Libro libro = new Libro();
            libro.setTitulo(title);
            libro.setAutores(authors.stream().map(Person::toAutor).collect(Collectors.toList()));
            return libro;
        }
    }

    private static class Person {
        private String name;

        public Autor toAutor() {
            Autor autor = new Autor();
            autor.setNombre(name);
            return autor;
        }
    }
}
