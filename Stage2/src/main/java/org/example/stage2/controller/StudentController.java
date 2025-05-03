package org.example.stage2.controller;

import jakarta.validation.Valid;
import org.example.stage2.dto.StudentDto;
import org.example.stage2.response.StandardResponse;
import org.example.stage2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Get all students
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping()
    public ResponseEntity<StandardResponse> getAllStudents() {
        List<StudentDto> studentDtos = studentService.getAllStudentsAsDto();
        StandardResponse response = new StandardResponse("success", studentDtos, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get student by ID
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getStudentById(@PathVariable Long id) {
        StudentDto studentDto = studentService.getStudentDtoById(id);
        StandardResponse response = new StandardResponse("success", studentDto, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new student
     * Uses @Valid to validate student according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 201 Created status with location header
     */
    @PostMapping()
    public ResponseEntity<StandardResponse> addStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto addedDto = studentService.addStudentAndReturnDto(studentDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedDto.getId())
                .toUri();

        StandardResponse response = new StandardResponse("success", addedDto, null);
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update a student
     * Uses @Valid to validate student according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        StudentDto updatedDto = studentService.updateStudentAndReturnDto(studentDto, id);
        StandardResponse response = new StandardResponse("success", updatedDto, null);
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