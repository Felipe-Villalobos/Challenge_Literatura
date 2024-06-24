package com.alura.challenge.literatura.model.clasesrecords;

import com.alura.challenge.literatura.model.Libro;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer cumpleanios;

    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "datosAutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@Transient
    private List<Libro> libros;


    public Autor() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCumpleanios() {
        return cumpleanios;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Autor(Libro.Autor autor) {
        this.nombre = datosAutor.nombre();
        this.cumpleanios = datosAutor.cumpleanios();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    @Override
    public String toString() {
        return
                "nombre='" + nombre + '\'' +
                        ", cumpleanios=" + cumpleanios +
                        ", fechaFallecimiento=" + fechaFallecimiento;
    }
}
