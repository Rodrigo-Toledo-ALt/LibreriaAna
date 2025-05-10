package org.example.libreriaana.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos", schema="biblioteca_digital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_devolucion_prevista", nullable = false)
    private LocalDateTime fechaDevolucionPrevista;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fechaDevolucionReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @PrePersist
    protected void onCreate() {
        fechaPrestamo = LocalDateTime.now();
        // Por defecto, fecha de devolución prevista a 15 días
        fechaDevolucionPrevista = fechaPrestamo.plusDays(15);
        if (estado == null) {
            estado = Estado.ACTIVO;
        }
    }

    public enum Estado {
        ACTIVO,
        DEVUELTO,
        RETRASADO
    }
}