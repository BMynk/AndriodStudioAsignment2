package com.example.cswbooks.models;

/**
 * TextbookListing — concrete listing wrapping a Book.
 *
 * Demonstrates: Inheritance (extends Listing),
 *               Interface (implements Searchable),
 *               Polymorphism.
 */
public class TextbookListing extends Listing implements Searchable {

    private final Book book;

    public TextbookListing(String listingId, Book book) {
        super(listingId,
                book.getSellerName(),
                book.getPrice(),
                book.getCopiesAvailable());
        this.book = book;
    }

    // ── Listing abstract implementations ──────────────────────────────────────

    @Override
    public String getDisplayTitle() {
        return book.getTitle();
    }

    @Override
    public String getSummary() {
        return book.getTitle()
                + " by " + book.getAuthor()
                + " | " + book.getCondition()
                + " | " + getFormattedPrice()
                + " | " + getCopiesAvailable() + " copies";
    }

    @Override
    public boolean isValid() {
        return book.getTitle()      != null && !book.getTitle().trim().isEmpty()
                && book.getAuthor()     != null && !book.getAuthor().trim().isEmpty()
                && book.getSellerName() != null && !book.getSellerName().trim().isEmpty()
                && book.getPrice()      >  0
                && book.getCopiesAvailable() > 0;
    }

    // ── Searchable implementation ─────────────────────────────────────────────

    @Override
    public boolean matchesQuery(String query) {
        return book.matchesQuery(query);
    }

    @Override
    public boolean matchesCategory(String category) {
        return book.matchesCategory(category);
    }

    // ── Getter ────────────────────────────────────────────────────────────────
    public Book getBook() { return book; }
}