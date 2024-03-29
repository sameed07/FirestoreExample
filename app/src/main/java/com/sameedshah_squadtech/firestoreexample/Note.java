package com.sameedshah_squadtech.firestoreexample;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String title;
    private String description;
    private String documentId;
    private int priority;

    public Note(String title, String description, int priority ) {
        this.title = title;
        this.priority = priority;
        this.description = description;
    }

    public Note() {
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
