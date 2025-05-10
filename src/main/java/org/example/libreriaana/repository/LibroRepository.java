package org.example.libreriaana.repository;

import org.example.libreriaana.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    Optional<Libro> findByIsbn(String isbn);
    
    List<Libro> findByDisponibleTrue();
    
    @Query("SELECT l FROM Libro l WHERE (:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) " +
           "AND (:autor IS NULL OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%'))) " +
           "AND (:categoria IS NULL OR LOWER(l.categoria) = LOWER(:categoria))")
    List<Libro> buscarLibros(@Param("titulo") String titulo, 
                             @Param("autor") String autor, 
                             @Param("categoria") String categoria);
}