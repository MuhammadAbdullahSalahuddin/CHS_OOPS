package Stegno;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable { // Made public
    private static final long serialVersionUID = 1L;
    private final String text;
    private final String author;
    private final Date timestamp;

    public Message(String text, String author) {
        if (text == null || author == null) {
            throw new IllegalArgumentException("Text and author must not be null.");
        }
        this.text = text;
        this.author = author;
        this.timestamp = new Date();
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime()); // Return a copy to prevent modification
    }

    @Override
    public String toString() {
        return "From: " + author + " @ " + timestamp + "\nMessage: " + text;
    }
}