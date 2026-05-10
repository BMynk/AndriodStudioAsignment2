package com.example.cswbooks.models;

/**
 * DuplicateListingException — thrown when a seller tries to post
 * a listing with the same title + author + seller that already exists.
 *
 * Demonstrates: Custom exception handling.
 */
public class DuplicateListingException extends Exception {

    private final Book existingBook;

    public DuplicateListingException(Book existingBook) {
        super("A listing for \""
                + existingBook.getTitle()
                + "\" by " + existingBook.getAuthor()
                + " already exists under seller: "
                + existingBook.getSellerName());
        this.existingBook = existingBook;
    }

    public Book getExistingBook() {
        return existingBook;
    }
}