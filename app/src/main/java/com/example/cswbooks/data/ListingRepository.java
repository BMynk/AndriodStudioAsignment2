package com.example.cswbooks.data;

import com.example.cswbooks.models.Book;
import com.example.cswbooks.models.DuplicateListingException;
import com.example.cswbooks.models.TextbookListing;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ListingRepository — singleton in-memory store for ALL listings.
 *
 * Every fragment reads from and writes to this one instance so:
 *  - New listings appear immediately in Browse and Search
 *  - Duplicates are caught before insertion
 *  - My Listings in Settings shows accurate data
 *
 * Demonstrates: Singleton pattern, exception handling, encapsulation.
 */
public class ListingRepository {

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static ListingRepository instance;

    public static ListingRepository getInstance() {
        if (instance == null) {
            instance = new ListingRepository();
        }
        return instance;
    }

    private final List<TextbookListing> listings = new ArrayList<>();

    /** Pre-loads sample books on first access. */
    private ListingRepository() {
        for (Book book : SampleData.getSampleBooks()) {
            listings.add(new TextbookListing(generateId(), book));
        }
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /**
     * All active books — used by BrowseFragment and SearchFragment.
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        for (TextbookListing listing : listings) {
            if (listing.isActive()) books.add(listing.getBook());
        }
        return books;
    }

    /**
     * All active TextbookListing objects — used by SettingsFragment.
     */
    public List<TextbookListing> getAllListings() {
        List<TextbookListing> active = new ArrayList<>();
        for (TextbookListing l : listings) {
            if (l.isActive()) active.add(l);
        }
        return active;
    }

    /**
     * Books listed by a specific seller — used by My Listings.
     */
    public List<Book> getBooksBySeller(String sellerName) {
        List<Book> result = new ArrayList<>();
        for (TextbookListing listing : listings) {
            if (listing.isActive()
                    && listing.getBook()
                    .getSellerName()
                    .equalsIgnoreCase(sellerName)) {
                result.add(listing.getBook());
            }
        }
        return result;
    }

    /** Total number of active listings. */
    public int getActiveListingCount() {
        int count = 0;
        for (TextbookListing l : listings) {
            if (l.isActive()) count++;
        }
        return count;
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    /**
     * Adds a new listing.
     * Throws DuplicateListingException if title + author + seller already exist.
     */
    public void addListing(Book newBook) throws DuplicateListingException {
        for (TextbookListing existing : listings) {
            if (existing.isActive()
                    && newBook.isDuplicateOf(existing.getBook())) {
                throw new DuplicateListingException(existing.getBook());
            }
        }
        listings.add(new TextbookListing(generateId(), newBook));
    }

    /**
     * Finds an existing active book that is a duplicate of the given book.
     * Returns null if no duplicate found.
     * Used by SellBookBottomSheet to offer update options.
     */
    public Book findDuplicate(Book newBook) {
        for (TextbookListing existing : listings) {
            if (existing.isActive()
                    && newBook.isDuplicateOf(existing.getBook())) {
                return existing.getBook();
            }
        }
        return null;
    }

    /**
     * Deactivates a listing by book reference.
     */
    public void removeListing(Book book) {
        for (TextbookListing listing : listings) {
            if (listing.getBook() == book) {
                listing.setActive(false);
                break;
            }
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    private String generateId() {
        return "LST-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}