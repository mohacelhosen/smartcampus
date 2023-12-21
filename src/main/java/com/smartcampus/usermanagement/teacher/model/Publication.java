package com.smartcampus.usermanagement.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {
    private String title;            // Title of the publication
    private String authors;          // List of authors or contributors
    private String publicationDate;  // Date of publication
    private String publisher;        // Publisher or journal name
    private String publicationType;  // Type of publication (e.g., journal article, book, conference paper)
    private String publicationURL;   // URL or reference to the publication online (if applicable)
    private String description;      // Brief description or abstract of the publication
}

