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
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping()
    public ResponseEntity<StandardResponse> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        StandardResponse response = new StandardResponse("success", students, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a student by ID
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getStudent(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        StandardResponse response = new StandardResponse("success", student, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new student
     * Uses @Valid to validate a student according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 201 Created status with location header
     */
    @PostMapping()
    public ResponseEntity<StandardResponse> addStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto added = studentService.addStudent(studentDto);

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
     * Returns ResponseEntity with StandardResponse and 200 OK status
     * 
     * Note: The path variable ID identifies the resource to update, even though
     * the ID may also be present in the request body
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        StudentDto updated = studentService.updateStudent(studentDto, id);
        StandardResponse response = new StandardResponse("success", updated, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a student
     * Returns 204 No Content status without a response body
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}