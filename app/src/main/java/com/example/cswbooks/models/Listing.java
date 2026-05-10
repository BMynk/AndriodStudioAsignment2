package com.example.cswbooks.models;

/**
 * Listing — abstract base class for any item listed for sale.
 *
 * Demonstrates: Abstract class, encapsulation, polymorphism.
 * TextbookListing extends this.
 */
public abstract class Listing {

    private final String listingId;
    private final String sellerName;
    private double price;
    private int    copiesAvailable;
    private boolean isActive;

    public Listing(String listingId, String sellerName,
                   double price, int copiesAvailable) {
        this.listingId       = listingId;
        this.sellerName      = sellerName;
        this.price           = price;
        this.copiesAvailable = copiesAvailable;
        this.isActive        = true;
    }

    // ── Abstract methods — subclasses must implement ───────────────────────────
    public abstract String getDisplayTitle();
    public abstract String getSummary();
    public abstract boolean isValid();

    // ── Getters ───────────────────────────────────────────────────────────────
    public String  getListingId()      { return listingId; }
    public String  getSellerName()     { return sellerName; }
    public double  getPrice()          { return price; }
    public int     getCopiesAvailable(){ return copiesAvailable; }
    public boolean isActive()          { return isActive; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setPrice(double price)            { this.price = price; }
    public void setCopiesAvailable(int copies)    { this.copiesAvailable = copies; }
    public void setActive(boolean active)         { this.isActive = active; }

    /** Price formatted in Rands. */
    public String getFormattedPrice() {
        return "R" + String.format("%.2f", price);
    }
}