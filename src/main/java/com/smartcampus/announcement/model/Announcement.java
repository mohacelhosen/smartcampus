package com.smartcampus.announcement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Announcement {
    @Id
    private String id;
    private String announcementType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String announcementTime;
    private String announcementMessage;
    private String announcementTrackId;
}
