package org.example.stage2.service;

import org.example.stage2.dto.StudentDetailsDto;
import org.example.stage2.entity.StudentDetails;

/**
 * Service interface for managing student details
 */
public interface StudentDetailsService {

    /**
     * Get student details by student ID
     * @param studentId Student ID
     * @return StudentDetails entity
     */
    StudentDetails getStudentDetailsByStudentId(Long studentId);

    /**
     * Get student details by student ID as DTO
     * @param studentId Student ID
     * @return StudentDetailsDto
     */
    StudentDetailsDto getStudentDetailsDtoByStudentId(Long studentId);
    
    /**
     * Create new student details
     * @param detailsDto DTO with student details data and required studentId
     * @return Created StudentDetails entity
     */
    // TODO fix entities relationship: Added method to create student details that requires studentId
    // and validates the relationship integrity to prevent orphaned records
    StudentDetails createStudentDetails(StudentDetailsDto detailsDto);

    /**
     * Update student details by student ID
     * @param studentId Student ID
     * @param studentDetailsDto Updated student details
     * @return StudentDetails entity
     */
    // TODO fix entities relationship: This method validates that the studentId in the DTO
    // (if provided) matches the studentId parameter to prevent relationship mismatch
    StudentDetails updateStudentDetails(Long studentId, StudentDetailsDto studentDetailsDto);

    /**
     * Update student details by student ID and return DTO
     * @param studentId Student ID
     * @param studentDetailsDto Updated student details
     * @return Updated StudentDetailsDto
     */
    // TODO fix entities relationship: This method ensures studentId consistency
    // between the path parameter and returned DTO
    StudentDetailsDto updateStudentDetailsAndReturnDto(Long studentId, StudentDetailsDto studentDetailsDto);

    /**
     * Validate and create student details
     * @param pathStudentId Student ID from the path
     * @param originalStudentId Original student ID from the request body (if any)
     * @param detailsDto DTO with student details data
     * @return Created StudentDetailsDto
     */
    // TODO fix entities relationship: Added method to validate the original studentId from request
    // against the path studentId before creating details, to prevent relationship mismatch
    StudentDetailsDto validateAndCreateStudentDetails(Long pathStudentId, Long originalStudentId, 
                                                    StudentDetailsDto detailsDto);

    /**
     * Validate and update student details
     * @param pathStudentId Student ID from the path
     * @param originalStudentId Original student ID from the request body (if any)
     * @param detailsDto DTO with student details data
     * @return Updated StudentDetailsDto
     */
    // TODO fix entities relationship: Added method to validate the original studentId from request
    // against the path studentId before updating details, to prevent relationship mismatch
    StudentDetailsDto validateAndUpdateStudentDetails(Long pathStudentId, Long originalStudentId, 
                                                    StudentDetailsDto detailsDto);

    /**
     * Delete student details by student ID
     * @param studentId Student ID
     */
    // TODO fix entities relationship: Leverages orphanRemoval to ensure details
    // are completely removed when unlinked from a student
    void deleteStudentDetails(Long studentId);
}