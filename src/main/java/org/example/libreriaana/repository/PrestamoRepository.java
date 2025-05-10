package org.example.libreriaana.repository;

import org.example.libreriaana.model.Prestamo;
import org.example.libreriaana.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    List<Prestamo> findByUsuarioId(Long usuarioId);
    
    List<Prestamo> findByUsuario(Usuario usuario);
    
    List<Prestamo> findByEstado(Prestamo.Estado estado);
    
    List<Prestamo> findByUsuarioAndEstado(Usuario usuario, Prestamo.Estado estado);
    
    boolean existsByLibroIdAndEstado(Long libroId, Prestamo.Estado estado);
}