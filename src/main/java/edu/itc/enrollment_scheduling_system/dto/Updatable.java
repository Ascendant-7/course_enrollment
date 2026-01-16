package edu.itc.enrollment_scheduling_system.dto;

/**
 * Marker interface for DTOs/Forms that can update an entity.
 * Enforces type-safe update patterns across the domain model.
 *
 * @param <T> The entity type this form updates
 */
public interface Updatable<T> {
    
    /**
     * Applies the form data to the given entity.
     * 
     * @param entity The entity to update
     */
    void applyTo(T entity);
}
