package org.example.stage2.controller;

import jakarta.validation.Valid;
import org.example.stage2.dto.StudentDetailsDto;
import org.example.stage2.response.StandardResponse;
import org.example.stage2.service.StudentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/students/{studentId}/details")
public class StudentDetailsController {

    private final StudentDetailsService studentDetailsService;

    @Autowired
    public StudentDetailsController(StudentDetailsService studentDetailsService) {
        this.studentDetailsService = studentDetailsService;
    }

    /**
     * Get student details
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping
    public ResponseEntity<StandardResponse> getStudentDetails(@PathVariable Long studentId) {
        StudentDetailsDto detailsDto = studentDetailsService.getStudentDetailsDtoByStudentId(studentId);
        StandardResponse response = new StandardResponse("success", detailsDto, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Create new student details
     * Uses @Valid to validate student details according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 201 Created status with location header
     */
    @PostMapping
    public ResponseEntity<StandardResponse> createStudentDetails(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentDetailsDto detailsDto) {
        
        // Save the original studentId from request body (if any)
        Long originalStudentId = detailsDto.getStudentId();
        
        // Set the studentId from the path parameter
        detailsDto.setStudentId(studentId);
        
        // Pass both values to the service for validation
        StudentDetailsDto createdDto = studentDetailsService.validateAndCreateStudentDetails(
                studentId, originalStudentId, detailsDto);
        
        // Build the location URI for the created resource
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        
        StandardResponse response = new StandardResponse("success", createdDto, null);
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update student details
     * Uses @Valid to validate student details according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @PutMapping
    public ResponseEntity<StandardResponse> updateStudentDetails(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentDetailsDto detailsDto) {

        // Save the original studentId from request body (if any)
        Long originalStudentId = detailsDto.getStudentId();
        
        // Set the studentId from the path parameter
        detailsDto.setStudentId(studentId);
        
        // Pass both values to the service for validation
        StudentDetailsDto updatedDto = studentDetailsService.validateAndUpdateStudentDetails(
                studentId, originalStudentId, detailsDto);
        
        StandardResponse response = new StandardResponse("success", updatedDto, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete student details
     * Returns 204 No Content status without a response body
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentDetails(@PathVariable Long studentId) {
        studentDetailsService.deleteStudentDetails(studentId);
    }
}