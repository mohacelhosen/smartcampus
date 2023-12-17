package com.smartcampus.additional.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TeacherStuffSection {
    @Id
    private String id;

    @JsonProperty("sectionName")
    private String sectionName;

    @JsonProperty("contactList")
    private List<String> contactList;

    @JsonProperty("emailList")
    private List<String> emailList;
}
