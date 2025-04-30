package org.example.stage2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/* TODO fix entities relationship: Added unique constraint on student_id to ensure
 that a StudentDetails record can be associated with only one Student
 */

@Entity
@Table(name = "student_details",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Address must be less than 255 characters")
    @Column(length = 255)
    private String address;

    // regex: for Israeli phone numbers
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    @Column(length = 20)
    private String phoneNumber;

    @Size(max = 100, message = "Emergency contact name must be less than 100 characters")
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    // regex: for Israeli phone numbers
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Emergency contact phone must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    /* TODO fix entities relationship: Changed relationship to make it more explicit
     that a StudentDetails must be associated with a Student
     This is now the inverse side of the OneToOne relationship with the Student entity
     */
    @OneToOne(mappedBy = "details")
    // This prevents recursion in toString, in JSON entering infinite loop
    @ToString.Exclude
    private Student student;
}