package com.smartcampus.Student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceHallInformation {
    private String hallName;         // Name of the residence hall or housing facility
    private String roomNumber;       // Room number or assigned living space
    private String roommateName;     // Name of the assigned roommate (if applicable)
    private String moveInDate;       // Date when the student moved into the residence hall
    private String moveOutDate;      // Date when the student moved out of the residence hall
    private String hallContact;      // Contact information for the residence hall office
    private String mealPlan;         // Type of meal plan associated with the residence hall
    private String hallAmenities;    // List of amenities provided by the residence hall
}

