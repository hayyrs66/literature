package com.literatura.literatura.controller;

import com.literatura.literatura.model.Libro;
import com.literatura.literatura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final LibroService libroService;
    private final Scanner scanner;

    @Autowired
    public Menu(LibroService libroService) {
        this.libroService = libroService;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        boolean salir = false;
        int opcion;

        while (!salir) {
            System.out.println("\n*** Menú de LiterAlura ***");
            System.out.println("1. Buscar libros por título");
            System.out.println("2. Buscar libros por autor");
            System.out.println("3. Mostrar todos los libros");
            System.out.println("4. Salir");
            System.out.print("Elija una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer de entrada

                switch (opcion) {
                    case 1:
                        buscarLibrosPorTitulo();
                        break;
                    case 2:
                        buscarLibrosPorAutor();
                        break;
                    case 3:
                        mostrarTodosLosLibros();
                        break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        System.out.println("Por favor, ingrese una opción válida (1-4).");
                }
            } catch (Exception e) {
                System.out.println("Error al leer la opción. Intente de nuevo.");
                scanner.nextLine(); // Limpiar el buffer de entrada en caso de error
            }
        }

        System.out.println("¡Gracias por usar LiterAlura!");
    }

    private void buscarLibrosPorTitulo() {
        System.out.print("Ingrese el título del libro a buscar: ");
        String titulo = scanner.nextLine();
        List<Libro> libros = libroService.buscarPorTitulo(titulo);
        mostrarLibros(libros);
    }

    private void buscarLibrosPorAutor() {
        System.out.print("Ingrese el nombre del autor: ");
        String autor = scanner.nextLine();
        List<Libro> libros = libroService.buscarPorAutor(autor);
        mostrarLibros(libros);
    }

    private void mostrarTodosLosLibros() {
        List<Libro> libros = libroService.mostrarTodos();
        mostrarLibros(libros);
    }

    private void mostrarLibros(List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros.");
        } else {
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        }
    }
}
