package org.example.stage2.service;

import org.example.stage2.dto.StudentDetailsDto;
import org.example.stage2.entity.Student;
import org.example.stage2.entity.StudentDetails;
import org.example.stage2.exception.InvalidRequestException;
import org.example.stage2.exception.NotExists;
import org.example.stage2.mapper.StudentMapper;
import org.example.stage2.repository.StudentDetailsRepository;
import org.example.stage2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentDetailsServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetails getStudentDetailsByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        if (student.getDetails() == null) {
            throw new NotExists("Details for student with id " + studentId + " do not exist");
        }

        return student.getDetails();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto getStudentDetailsDtoByStudentId(Long studentId) {
        StudentDetails details = getStudentDetailsByStudentId(studentId);
        StudentDetailsDto dto = studentMapper.toDto(details);
        // Entity relationship: Always set the studentId in the DTO to maintain relationship
        dto.setStudentId(studentId);
        return dto;
    }

    @Override
    @Transactional
    public StudentDetailsDto validateAndCreateStudentDetails(Long pathStudentId, Long originalStudentId, 
                                                           StudentDetailsDto detailsDto) {
        // entity relationship: Validate that the original studentId (if provided)
        // matches the path studentId to prevent accidental relationship changes
        validateStudentIdMatch(pathStudentId, originalStudentId);
        
        // Continue with normal creation process
        StudentDetails created = createStudentDetails(detailsDto);
        StudentDetailsDto createdDto = studentMapper.toDto(created);
        // Ensure the studentId is always set correctly in the response
        createdDto.setStudentId(pathStudentId);
        return createdDto;
    }

    @Override
    @Transactional
    public StudentDetailsDto validateAndUpdateStudentDetails(Long pathStudentId, Long originalStudentId,
                                                             StudentDetailsDto detailsDto) {
        // entity relationship: Validate that the original studentId (if provided)
        // matches the path studentId to prevent accidental relationship changes
        validateStudentIdMatch(pathStudentId, originalStudentId);

        // Continue with a normal update process
        return updateStudentDetailsAndReturnDto(pathStudentId, detailsDto);
    }

    /**
     * Helper method to validate that the original studentId (if provided) matches the path studentId
     */
    private void validateStudentIdMatch(Long pathStudentId, Long originalStudentId) {
        if (originalStudentId != null && !originalStudentId.equals(pathStudentId)) {
            throw new InvalidRequestException("Student ID in the path (" + pathStudentId +
                    ") does not match the one in the request body (" + originalStudentId + ")");
        }
    }

    @Override
    @Transactional
    public StudentDetails createStudentDetails(StudentDetailsDto detailsDto) {
        // TODO fix entities relationship: When creating StudentDetails directly (not through a Student), studentId is required
        // This enforces that standalone StudentDetails creation must be linked to an existing Student
        if (detailsDto.getStudentId() == null) {
            throw new InvalidRequestException("Student ID is required when creating student details directly");
        }

        // Verify the student exists
        Student student = studentRepository.findById(detailsDto.getStudentId())
                .orElseThrow(() -> new NotExists("Student with id " + detailsDto.getStudentId() + " does not exist"));

        // Check if a student already has details
        if (student.getDetails() != null) {
            throw new InvalidRequestException("Student with id " + detailsDto.getStudentId() + " already has details");
        }

        // Create new student details and set fields from DTO
        StudentDetails details = new StudentDetails();
        details.setAddress(detailsDto.getAddress());
        details.setPhoneNumber(detailsDto.getPhoneNumber());
        details.setEmergencyContactName(detailsDto.getEmergencyContactName());
        details.setEmergencyContactPhone(detailsDto.getEmergencyContactPhone());

        // Set up the bidirectional relationship explicitly
        details.setStudent(student);
        student.setDetails(details);

        // Save the student, which will cascade to details due to CascadeType.ALL
        studentRepository.save(student);

        return details;
    }

    @Override
    @Transactional
    public StudentDetails updateStudentDetails(Long studentId, StudentDetailsDto detailsDto) {
        // Note: The validation of studentId match is now moved to validateAndUpdateStudentDetails
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        StudentDetails details;
        if (student.getDetails() == null) {
            // If no existing details, create new ones
            details = new StudentDetails();
            details.setStudent(student);
            student.setDetails(details);
        } else {
            // Use existing details
            details = student.getDetails();
        }
        
        // Update details fields
        details.setAddress(detailsDto.getAddress());
        details.setPhoneNumber(detailsDto.getPhoneNumber());
        details.setEmergencyContactName(detailsDto.getEmergencyContactName());
        details.setEmergencyContactPhone(detailsDto.getEmergencyContactPhone());
        
        // Explicitly maintain bidirectional relationship
        details.setStudent(student);

        // Saving the student will also save the details due to CascadeType.ALL
        studentRepository.save(student);

        return details;
    }

    @Override
    @Transactional
    public StudentDetailsDto updateStudentDetailsAndReturnDto(Long studentId, StudentDetailsDto detailsDto) {
        // TODO fix entities relationship: Always ensure the studentId is consistent
        // by explicitly setting it from the path parameter before any operations
        detailsDto.setStudentId(studentId);
        
        StudentDetails updated = updateStudentDetails(studentId, detailsDto);
        StudentDetailsDto updatedDto = studentMapper.toDto(updated);
        // Ensure the studentId is always set correctly in the response
        updatedDto.setStudentId(studentId);
        return updatedDto;
    }

    @Override
    @Transactional
    public void deleteStudentDetails(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        if (student.getDetails() == null) {
            throw new NotExists("Details for student with id " + studentId + " do not exist");
        }

        // TODO fix entities relationship: With orphanRemoval=true, setting details to null 
        // will automatically delete the details from the database
        student.setDetails(null);
        studentRepository.save(student);
    }
}