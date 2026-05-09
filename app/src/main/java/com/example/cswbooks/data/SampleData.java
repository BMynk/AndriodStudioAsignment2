package com.example.cswbooks.data;

import com.example.cswbooks.R;
import com.example.cswbooks.models.Book;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    public static List<Book> getSampleBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Intro to Psychology",        "James W. Kalat",       "Sarah J.", 65.00, 3, R.drawable.cover_psychology));
        books.add(new Book("Organic Chemistry",          "David R. Klein",       "Sarah J.", 40.00, 1, R.drawable.cover_organic_chem));
        books.add(new Book("Microeconomics",             "Paul Krugman",         "Sarah J.", 65.00, 2, R.drawable.cover_microeconomics));
        books.add(new Book("Microeconomics and Business","N. Gregory Mankiw",    "Sarah J.", 40.00, 1, R.drawable.cover_micro_business));
        books.add(new Book("Principles of Biology",      "Robert J. Brooker",    "James K.", 55.00, 4, R.drawable.cover_biology));
        books.add(new Book("Organic Chemisology",        "Paula Y. Bruice",      "Sarah J.", 48.00, 2, R.drawable.cover_organic_chem2));
        books.add(new Book("Calculus: Early Transcendentals","James Stewart",    "Mike T.",  70.00, 1, R.drawable.cover_calculus));
        books.add(new Book("Introduction to Algorithms", "Cormen et al.",        "Anna R.",  80.00, 2, R.drawable.cover_algorithms));
        return books;
    }
}