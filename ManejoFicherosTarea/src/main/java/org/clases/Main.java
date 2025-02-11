package org.clases;

import org.clases.Coche;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String FICHERO_DAT = "coches.dat";
    private static final String FICHERO_CSV = "coches.csv";
    private static ArrayList<Coche> coches = new ArrayList<>();

    public static void main(String[] args) {
        cargarCoches();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(scanner);

            switch (opcion) {
                case 1 -> anadirCoche(scanner);
                case 2 -> borrarCoche(scanner);
                case 3 -> consultarCoche(scanner);
                case 4 -> listarCoches();
                case 5 -> {guardarCoches();
                System.out.println("Datos guardados en " + FICHERO_DAT);
                }
                case 6 -> exportarCochesACSV();
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);

        System.out.println("Programa terminado.");
    }

    private static void mostrarMenu() {
        System.out.println("""
                1. Añadir nuevo coche
                2. Borrar coche por id
                3. Consulta coche por id
                4. Listado de coches
                5. Terminar el programa
                6. Exportar coches a archivo CSV
                """);
    }

    private static int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Valor no válido
        }
    }

    private static void cargarCoches() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHERO_DAT))) {
            coches = (ArrayList<Coche>) ois.readObject();
            System.out.println("Coches cargados correctamente.");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado. Se iniciará con una lista vacía.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    private static void guardarCoches() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHERO_DAT))) {
            oos.writeObject(coches);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    private static void anadirCoche(Scanner scanner) {
        System.out.print("Ingrese el ID: ");
        int id = leerEntero(scanner);
        // Validar que el ID no esté duplicado
        if (coches.stream().anyMatch(c -> c.getId() == id)) {
            System.out.println("Error: Ya existe un coche con el ID " + id + ".");
            return;
        }
        System.out.print("Ingrese la matrícula: ");
        String matricula = scanner.nextLine();
        // Validar que la matrícula no esté duplicada
        if (coches.stream().anyMatch(c -> c.getMatricula().equalsIgnoreCase(matricula))) {
            System.out.println("Error: Ya existe un coche con la matrícula " + matricula + ".");
            return;
        }
        System.out.print("Ingrese la marca: ");
        String marca = scanner.nextLine();

        System.out.print("Ingrese el modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Ingrese el color: ");
        String color = scanner.nextLine();

        coches.add(new Coche(id, matricula, marca, modelo, color));
        System.out.println("Coche añadido correctamente.");
    }

    private static void borrarCoche(Scanner scanner) {
        System.out.print("Ingrese el ID del coche a borrar: ");
        int id = leerEntero(scanner);

        if (coches.removeIf(c -> c.getId() == id)) {
            System.out.println("Coche eliminado correctamente.");
        } else {
            System.out.println("Coche con ID " + id + " no encontrado.");
        }
    }

    private static void consultarCoche(Scanner scanner) {
        System.out.print("Ingrese el ID del coche a consultar: ");
        int id = leerEntero(scanner);

        coches.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Coche con ID " + id + " no encontrado.")
                );
    }

    private static void listarCoches() {
        if (coches.isEmpty()) {
            System.out.println("No hay coches registrados.");
        } else {
            coches.forEach(System.out::println);
        }

    }
    private static void exportarCochesACSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHERO_CSV))) {
            writer.println("ID;Matricula;Marca;Modelo;Color"); // Encabezado del CSV
            for (Coche coche : coches) {
                writer.println(coche.getId() + ";" +
                        coche.getMatricula() + ";" +
                        coche.getMarca() + ";" +
                        coche.getModelo() + ";" +
                        coche.getColor());
            }
            System.out.println("Coches exportados correctamente a " + FICHERO_CSV);
        } catch (IOException e) {
            System.out.println("Error al exportar los datos: " + e.getMessage());
        }
    }
}
