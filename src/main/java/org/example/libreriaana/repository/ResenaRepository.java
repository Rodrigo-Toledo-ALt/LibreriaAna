package org.example.libreriaana.repository;

import org.example.libreriaana.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    
    List<Resena> findByLibroId(Long libroId);
    
    List<Resena> findByUsuarioId(Long usuarioId);
    
    Optional<Resena> findByUsuarioIdAndLibroId(Long usuarioId, Long libroId);
    
    @Query("SELECT r FROM Resena r WHERE r.libro.id = :libroId AND r.puntuacion >= :puntuacionMinima")
    List<Resena> findByLibroIdAndPuntuacionMinima(@Param("libroId") Long libroId, 
                                                @Param("puntuacionMinima") Integer puntuacionMinima);
}