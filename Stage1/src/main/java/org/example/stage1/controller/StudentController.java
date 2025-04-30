package org.example.stage1.controller;

import jakarta.validation.Valid;
import org.example.stage1.dto.StudentDto;
import org.example.stage1.response.StandardResponse;
import org.example.stage1.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for student operations
 * The controller works directly with DTOs and delegates to the service layer
 * for business logic and data conversion
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Get all students
     * Returns a StandardResponse directly to maintain a consistent API response format
     */
    @GetMapping()
    public StandardResponse getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return new StandardResponse("success", students, null);
    }

    /**
     * Get a student by ID
     * Returns a StandardResponse directly to maintain a consistent API response format
     */
    @GetMapping("/{id}")
    public StandardResponse getStudent(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return new StandardResponse("success", student, null);
    }

    /**
     * Add a new student
     * Uses @Valid to validate a student according to Jakarta Validation constraints
     * Returns a ResponseEntity with StandardResponse and 201 Created status
     */
    @PostMapping()
    public ResponseEntity<StandardResponse> addStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto added = studentService.addStudent(studentDto);

        // it is customary to return the URI of the created resource in the Location header
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(added.getId())
                .toUri();

        StandardResponse response = new StandardResponse("success", added, null);
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update a student
     * Uses @Valid to validate a student according to Jakarta Validation constraints
     * Returns a StandardResponse directly to maintain a consistent API response format
     *
     * **** why the ID in the path and body, also?
     * It is customary to use ID in the URL to identify the resource being updated.
     * Even if the ID is also in the body
     */
    @PutMapping("/{id}")
    public StandardResponse updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        StudentDto updated = studentService.updateStudent(studentDto, id);
        return new StandardResponse("success", updated, null);
    }

    /**
     * Delete a student
     * Returns 204 No Content without a response body, bypassing GlobalResponseHandler
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Explicitly set the response status to 204
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        // Returning void with @ResponseStatus(NO_CONTENT) properly creates a 204 response
    }
}