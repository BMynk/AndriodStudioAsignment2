package com.example.cswbooks.models;

/**
 * Searchable — interface that any item displayed in Browse or Search
 * must implement. Allows SearchFragment to filter without knowing
 * the concrete type it is filtering.
 *
 * Demonstrates: Interface, polymorphism.
 */
public interface Searchable {
    boolean matchesQuery(String query);
    boolean matchesCategory(String category);
}