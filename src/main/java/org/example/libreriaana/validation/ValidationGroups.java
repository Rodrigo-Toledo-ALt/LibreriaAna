package org.example.libreriaana.validation;

/**
 * Grupos de validación para Bean Validation.
 */
public class ValidationGroups {

    /**
     * Grupo de validación para operaciones de creación (POST).
     */
    public interface CreateValidation {}

    /**
     * Grupo de validación para operaciones de actualización (PUT).
     */
    public interface UpdateValidation {}

    public interface AdminValidation {}
}