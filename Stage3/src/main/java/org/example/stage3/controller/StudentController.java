package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.TeacherBasicDto;
import org.example.stage3.response.StandardResponse;
import org.example.stage3.service.StudentService;
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
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping()
    public StandardResponse getAllStudents() {
        List<StudentDto> studentDtos = studentService.getAllStudentsAsDto();
        return new StandardResponse("success", studentDtos, null);
    }

    /**
     * Get student by ID
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping("/{id}")
    public StandardResponse getStudentById(@PathVariable Long id) {
        StudentDto studentDto = studentService.getStudentDtoById(id);
        return new StandardResponse("success", studentDto, null);
    }

    /**
     * Add a new student
     * Uses @Valid to validate student according to Jakarta Validation constraints
     * Returns a ResponseEntity with StandardResponse and 201 Created status
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
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @PutMapping("/{id}")
    public StandardResponse updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        StudentDto updatedDto = studentService.updateStudentAndReturnDto(studentDto, id);
        return new StandardResponse("success", updatedDto, null);
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
    
    /**
     * Get teacher for student
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping("/{id}/teacher")
    public StandardResponse getTeacherForStudent(@PathVariable Long id) {
        TeacherBasicDto teacherDto = studentService.getTeacherForStudent(id);
        return new StandardResponse("success", teacherDto, null);
    }
    
    /**
     * Assign teacher to student
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @PostMapping("/{studentId}/teacher/{teacherId}")
    public StandardResponse assignTeacherToStudent(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {
        
        StudentDto updatedDto = studentService.assignTeacherToStudent(studentId, teacherId);
        return new StandardResponse("success", updatedDto, null);
    }
    
    /**
     * Remove teacher from student
     * Returns 204 No Content without a response body, bypassing GlobalResponseHandler
     */
    @DeleteMapping("/{id}/teacher")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Explicitly set the response status to 204
    public void removeTeacherFromStudent(@PathVariable Long id) {
        studentService.removeTeacherFromStudent(id);
        // Returning void with @ResponseStatus(NO_CONTENT) properly creates a 204 response
    }
    
    /**
     * Get students without teacher
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping("/without-teacher")
    public StandardResponse getStudentsWithoutTeacher() {
        List<StudentDto> students = studentService.getStudentsWithoutTeacher();
        return new StandardResponse("success", students, null);
    }
}