package com.alura.challenge.literatura.repository;

import com.alura.challenge.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Métodos personalizados de consulta si son necesarios
}
