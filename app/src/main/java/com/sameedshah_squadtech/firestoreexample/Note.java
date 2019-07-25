package com.sameedshah_squadtech.firestoreexample;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String title;
    private String description;
    private String documentId;

    public Note(String title, String description ) {
        this.title = title;

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
