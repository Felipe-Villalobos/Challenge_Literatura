package com.alura.challenge.literatura.Principal;

import com.alura.challenge.literatura.config.ConsumoApiGutendex;
import com.alura.challenge.literatura.config.ConvertirDatos;
import com.alura.challenge.literatura.models.Autor;
import com.alura.challenge.literatura.models.Libro;
import com.alura.challenge.literatura.models.LibrosRespuestaApi;
import com.alura.challenge.literatura.models.records.DatosLibro;
import com.alura.challenge.literatura.repository.iAutorRepository;
import com.alura.challenge.literatura.repository.iLibroRepository;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApiGutendex consumoApi = new ConsumoApiGutendex();
    private ConvertirDatos convertir = new ConvertirDatos();
    private static final String API_BASE = "https://gutendex.com/books/?search=";
    private iLibroRepository libroRepository;
    private iAutorRepository autorRepository;

    public Principal(iLibroRepository libroRepository, iAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void consumo() {
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar el buffer de entrada
                procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.out.println("\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17"+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17 "+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17"+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17");
                System.out.println("\uD83E\uDD17Por favor ingresa uno de los numeros que aparecen en la lista.\uD83E\uDD17");
                System.out.println("\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17"+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17 "+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17"+
                        "\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17");
                sc.nextLine(); // Limpiar el buffer de entrada
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n|***************************************************|");
        System.out.println("|*****       BIENVENID@ TU LIBRERIA ONLINE       ******|");
        System.out.println("|***************************************************|");
        System.out.println("                                                     ");
        System.out.println("Selecciona tu opcción digitando el número y presiona la tecla Enter:");
        System.out.println("                                                     ");
        System.out.println("1 - Agregar libro por nombre");
        System.out.println("2 - Mostrar libros buscados");
        System.out.println("3 - Buscar libros almacenados por nombre");
        System.out.println("4 - Buscar todos los autores de libros buscados");
        System.out.println("5 - Buscar un autor en especifico por nombre");
        System.out.println("6 - Buscar autores por año");
        System.out.println("7 - Buscar libros por Idioma");
        System.out.println("8 - Top 10 libros más descargados");
        System.out.println("0 - Salir \n");

    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                buscarLibroEnLaWeb();
                break;
            case 2:
                librosBuscados();
                break;
            case 3:
                buscarLibroPorNombre();
                break;
            case 4:
                buscarAutoresDeLibros();
                break;
            case 5:
                buscarAutorPorNombre();
                break;
            case 6:
                buscarAutoresPorAnio();
                break;
            case 7:
                buscarLibrosPorIdioma();
                break;
            case 8:
                top10LibrosMasDescargados();
                break;

            case 0:
                String emoji = EmojiParser.parseToUnicode(":smile:");
                System.out.println("   Haz seleccionado la opcion 0, ");
                System.out.println("    El programa se ha cerrado.    \n");
                System.out.println("    ¡Vuelve cuando quieras" + emoji + "! \n");
                break;
            default:
                String emoji2 = EmojiParser.parseToUnicode(":think:");
                System.out.println("   Haz seleccionado una opcion incorrecta "+ emoji2);
                System.out.println("    Verifica y vuelve a intarlo\n");
                break;
        }
    }

    private void buscarLibroEnLaWeb() {
        Libro libro = getDatosLibro();

        if (libro != null) {
            try {
                if (libroRepository.existsByTitulo(libro.getTitulo())) {
                    System.out.println("El libro ya existe en la base de datos.");
                } else {
                    libroRepository.save(libro);
                    System.out.println("Libro guardado en la base de datos:\n" + libro.toString());
                }
            } catch (InvalidDataAccessApiUsageException e) {
                System.out.println("No se puede persistir el libro buscado.");
            }
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private Libro getDatosLibro() {
        System.out.println("Ingrese el nombre del libro: ");
        String nombreLibro = sc.nextLine().trim().toLowerCase();
        String json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));
        LibrosRespuestaApi datos = convertir.convertirDatosJsonAJava(json, LibrosRespuestaApi.class);

        if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
            DatosLibro primerLibro = datos.getResultadoLibros().get(0);
            return new Libro(primerLibro);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            libros.forEach(libro -> System.out.println(libro.toString()));
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Ingrese el título del libro que quiere buscar: ");
        String titulo = sc.nextLine().trim();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null) {
            System.out.println("El libro encontrado es:\n" + libroBuscado.toString());
        } else {
            System.out.println("No se encontró ningún libro con el título '" + titulo + "'.");
        }
    }

    private void buscarAutoresDeLibros() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores en la base de datos.");
        } else {
            Set<String> autoresUnicos = new HashSet<>();
            System.out.println("Autores de libros encontrados:");
            autores.forEach(autor -> autoresUnicos.add(autor.getNombre()));
            autoresUnicos.forEach(System.out::println);
        }
    }

    private void buscarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma en el que quiere buscar (es/en): ");
        String idioma = sc.nextLine().trim().toLowerCase();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            System.out.println("Libros encontrados en el idioma " + idioma + ":");
            librosPorIdioma.forEach(libro -> System.out.println(libro.toString()));
        }
    }

    private void buscarAutoresPorAnio() {
        System.out.println("Ingrese el año para buscar autores vivos: ");
        int anio = sc.nextInt();
        sc.nextLine(); // Limpiar el buffer de entrada
        List<Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anio, anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(autor -> System.out.println(autor.getNombre()));
        }
    }

    private void top10LibrosMasDescargados() {
        List<Libro> top10Libros = libroRepository.findTop10ByTituloByCantidadDescargas();
        if (!top10Libros.isEmpty()) {
            System.out.println("Top 10 Libros más descargados:");
            for (int i = 0; i < top10Libros.size(); i++) {
                Libro libro = top10Libros.get(i);
                System.out.printf("%d. %s, Autor: %s, Descargas: %d\n", i + 1, libro.getTitulo(),
                        libro.getAutores().getNombre(), libro.getCantidadDescargas());
            }
        } else {
            System.out.println("No hay libros disponibles en el top 10.");
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que quiere buscar: ");
        String nombreAutor = sc.nextLine().trim();
        Optional<Autor> autorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(nombreAutor);
        if (autorBuscado.isPresent()) {
            System.out.println("Autor encontrado:\n" + autorBuscado.get().getNombre());
        } else {
            System.out.println("No se encontró ningún autor con el nombre '" + nombreAutor + "'.");
        }
    }
}
