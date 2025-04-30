package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.StudentDetailsDto;
import org.example.stage3.response.StandardResponse;
import org.example.stage3.service.StudentDetailsService;
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
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping
    public StandardResponse getStudentDetails(@PathVariable Long studentId) {
        StudentDetailsDto detailsDto = studentDetailsService.getStudentDetailsDtoByStudentId(studentId);
        return new StandardResponse("success", detailsDto, null);
    }

    /**
     * Create new student details
     * Uses @Valid to validate student details according to Jakarta Validation constraints
     * Returns a ResponseEntity with StandardResponse and 201 Created status
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
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @PutMapping
    public StandardResponse updateStudentDetails(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentDetailsDto detailsDto) {

        // Save the original studentId from request body (if any)
        Long originalStudentId = detailsDto.getStudentId();
        
        // Set the studentId from the path parameter
        detailsDto.setStudentId(studentId);
        
        // Pass both values to the service for validation
        StudentDetailsDto updatedDto = studentDetailsService.validateAndUpdateStudentDetails(
                studentId, originalStudentId, detailsDto);
        
        return new StandardResponse("success", updatedDto, null);
    }

    /**
     * Delete student details
     * Returns 204 No Content without a response body, bypassing GlobalResponseHandler
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Explicitly set the response status to 204
    public void deleteStudentDetails(@PathVariable Long studentId) {
        studentDetailsService.deleteStudentDetails(studentId);
        // Returning void with @ResponseStatus(NO_CONTENT) properly creates a 204 response
    }
}