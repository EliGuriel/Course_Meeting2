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
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping()
    public ResponseEntity<StandardResponse> getAllStudents() {
        List<StudentDto> studentDtos = studentService.getAllStudentsAsDto();
        return ResponseEntity.ok(new StandardResponse("success", studentDtos, null));
    }

    /**
     * Get student by ID
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getStudentById(@PathVariable Long id) {
        StudentDto studentDto = studentService.getStudentDtoById(id);
        return ResponseEntity.ok(new StandardResponse("success", studentDto, null));
    }

    /**
     * Add a new student
     * Uses @Valid to validate a student according to Jakarta Validation constraints
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
     * Uses @Valid to validate a student according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        StudentDto updatedDto = studentService.updateStudentAndReturnDto(studentDto, id);
        return ResponseEntity.ok(new StandardResponse("success", updatedDto, null));
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
    
    /**
     * Get teacher for a student
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/{id}/teacher")
    public ResponseEntity<StandardResponse> getTeacherForStudent(@PathVariable Long id) {
        TeacherBasicDto teacherDto = studentService.getTeacherForStudent(id);
        return ResponseEntity.ok(new StandardResponse("success", teacherDto, null));
    }
    
    /**
     * Assign teacher to a student
     * Creates a relationship between existing resources
     * Returns ResponseEntity with StandardResponse and 201 Created status with location header
     */
    @PostMapping("/{studentId}/teacher/{teacherId}")
    public ResponseEntity<StandardResponse> assignTeacherToStudent(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {
        
        StudentDto updatedDto = studentService.assignTeacherToStudent(studentId, teacherId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedDto.getId())
                .toUri();
        
        StandardResponse response = new StandardResponse("success", updatedDto, null);
        return ResponseEntity.created(location).body(response);
    }
    
    /**
     * Remove teacher from student
     * Returns 204 No Content status without a response body
     */
    @DeleteMapping("/{id}/teacher")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTeacherFromStudent(@PathVariable Long id) {
        studentService.removeTeacherFromStudent(id);
    }
    
    /**
     * Get students without a teacher
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/without-teacher")
    public ResponseEntity<StandardResponse> getStudentsWithoutTeacher() {
        List<StudentDto> students = studentService.getStudentsWithoutTeacher();
        return ResponseEntity.ok(new StandardResponse("success", students, null));
    }
}