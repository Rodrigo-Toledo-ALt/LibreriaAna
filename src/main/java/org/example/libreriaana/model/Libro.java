package org.example.libreriaana.model;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "libros", schema="biblioteca_digital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "ano_publicacion")
    private Integer anioPublicacion;

    @Column(nullable = false)
    private String editorial;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(name = "imagen_portada")
    private String imagenPortada;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Prestamo> prestamos;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Resena> resenas;
}