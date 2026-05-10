package com.example.cswbooks.models;

/**
 * Book — core data model for a textbook.
 *
 * Implements Searchable so SearchFragment can filter
 * without knowing the concrete type.
 *
 * Demonstrates: Encapsulation, interface implementation.
 */
public class Book implements Searchable {

    // ── Fields ────────────────────────────────────────────────────────────────
    private String title;
    private String author;
    private String sellerName;
    private double price;
    private int    copiesAvailable;
    private int    coverResId;
    private String edition;
    private String condition;
    private String sellerNotes;

    // ── Original constructor — keeps SampleData compiling unchanged ───────────
    public Book(String title, String author, String sellerName,
                double price, int copiesAvailable, int coverResId) {
        this.title           = title;
        this.author          = author;
        this.sellerName      = sellerName;
        this.price           = price;
        this.copiesAvailable = copiesAvailable;
        this.coverResId      = coverResId;
        this.edition         = "";
        this.condition       = "Good";
        this.sellerNotes     = "";
    }

    // ── Full constructor — used by SellBookBottomSheet ────────────────────────
    public Book(String title, String author, String sellerName,
                double price, int copiesAvailable, int coverResId,
                String edition, String condition, String sellerNotes) {
        this(title, author, sellerName, price, copiesAvailable, coverResId);
        this.edition     = edition;
        this.condition   = condition;
        this.sellerNotes = sellerNotes;
    }

    // ── Searchable implementation ─────────────────────────────────────────────

    /**
     * True if title, author, or seller contains the query.
     * Empty query always returns true.
     */
    @Override
    public boolean matchesQuery(String query) {
        if (query == null || query.isEmpty()) return true;
        String q = query.toLowerCase().trim();
        return title.toLowerCase().contains(q)
                || author.toLowerCase().contains(q)
                || sellerName.toLowerCase().contains(q);
    }

    /**
     * True if this book belongs to the selected category.
     * Matching is keyword-based on the title.
     * Empty category always returns true (show all).
     */
    @Override
    public boolean matchesCategory(String category) {
        if (category == null || category.isEmpty()) return true;
        String t = title.toLowerCase();
        switch (category) {
            case "Science":
                return t.contains("bio")
                        || t.contains("chem")
                        || t.contains("psych")
                        || t.contains("organ");
            case "History":
                return t.contains("hist");
            case "Business":
                return t.contains("econ")
                        || t.contains("micro")
                        || t.contains("business");
            case "Computing":
                return t.contains("algorithm")
                        || t.contains("comput")
                        || t.contains("calculus");
            default:
                return true;
        }
    }

    /**
     * Two books are duplicates if title + author + seller match
     * (case-insensitive). Used by ListingRepository.
     */
    public boolean isDuplicateOf(Book other) {
        if (other == null) return false;
        return this.title.equalsIgnoreCase(other.title)
                && this.author.equalsIgnoreCase(other.author)
                && this.sellerName.equalsIgnoreCase(other.sellerName);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getTitle()         { return title; }
    public String getAuthor()        { return author; }
    public String getSellerName()    { return sellerName; }
    public double getPrice()         { return price; }
    public int getCopiesAvailable()  { return copiesAvailable; }
    public int getCoverResId()       { return coverResId; }
    public String getEdition()       { return edition; }
    public String getCondition()     { return condition; }
    public String getSellerNotes()   { return sellerNotes; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setPrice(double price)                  { this.price = price; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }
    public void setEdition(String edition)              { this.edition = edition; }
    public void setCondition(String condition)          { this.condition = condition; }
    public void setSellerNotes(String notes)            { this.sellerNotes = notes; }

    @Override
    public String toString() {
        return title + " by " + author + " — R" + String.format("%.2f", price);
    }
}