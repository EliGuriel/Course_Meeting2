package org.example.stage2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity for storing additional details about a student.
 * Has a bidirectional One-to-One relationship with Student.
 */
@Entity
@Table(name = "student_details",
        /*
         * The unique constraint on student_id ensures that a StudentDetails
         * record can be associated with only one Student.
         * This enforces referential integrity at the database level.
         */
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

    /**
     * Pattern for validating Israeli phone numbers.
     * Accepts formats like +972541234567 or 0541234567.
     */
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    @Column(length = 20)
    private String phoneNumber;

    @Size(max = 100, message = "Emergency contact name must be less than 100 characters")
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    /**
     * Pattern for validating Israeli phone numbers for emergency contacts.
     * Same validation rules as the primary phone number.
     */
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Emergency contact phone must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    /**
     * This field establishes the bidirectional relationship between StudentDetails and Student entities.
     *
     * @OneToOne(mappedBy = "details") - Indicates that a StudentDetails must be associated with a Student,
     *                                  and that the Student entity owns the relationship
     *
     * @ToString.Exclude - Prevents this field from being included in the toString() method generated by Lombok,
     *                    which would otherwise cause infinite recursion (Student → StudentDetails → Student → ...)
     *                    when toString() is called
     *
     * @JsonIgnore - Prevents this field from being included during JSON serialization by Jackson,
     *              which would otherwise cause infinite recursion when converting to JSON
     *              (a Student object contains StudentDetails, which contains Student which contains StudentDetails...)
     *
     * @EqualsAndHashCode.Exclude - Prevents this field from being included into the equals() and hashCode() methods generated by Lombok,
     *                    which would otherwise cause infinite recursion (Student → StudentDetails → Student → ...)
     *
     * Using both annotations together is the recommended practice for bidirectional relationships
     * to prevent recursion issues in different contexts (string representation and JSON serialization).
     */
    @OneToOne(mappedBy = "details")
    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Student student;
}