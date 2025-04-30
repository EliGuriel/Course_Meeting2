package org.example.stage4.controller;

import jakarta.validation.Valid;
import org.example.stage4.dto.TeacherDto;
import org.example.stage4.response.StandardResponse;
import org.example.stage4.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Get all teachers
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping
    public StandardResponse getAllTeachers() {
        List<TeacherDto> teachers = teacherService.getAllTeachers();
        return new StandardResponse("success", teachers, null);
    }

    /**
     * Get teacher by ID
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @GetMapping("/{id}")
    public StandardResponse getTeacherById(@PathVariable Long id) {
        TeacherDto teacher = teacherService.getTeacherById(id);
        return new StandardResponse("success", teacher, null);
    }

    /**
     * Create a new teacher
     * Uses @Valid to validate teacher according to Jakarta Validation constraints
     * Returns a ResponseEntity with StandardResponse and 201 Created status
     */
    @PostMapping
    public ResponseEntity<StandardResponse> createTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        TeacherDto createdTeacher = teacherService.createTeacher(teacherDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTeacher.getId())
                .toUri();
        
        StandardResponse response = new StandardResponse("success", createdTeacher, null);
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update a teacher
     * Uses @Valid to validate teacher according to Jakarta Validation constraints
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @PutMapping("/{id}")
    public StandardResponse updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        TeacherDto updatedTeacher = teacherService.updateTeacher(id, teacherDto);
        return new StandardResponse("success", updatedTeacher, null);
    }

    /**
     * Delete a teacher
     * Returns 204 No Content without a response body, bypassing GlobalResponseHandler
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Explicitly set the response status to 204
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        // Returning void with @ResponseStatus(NO_CONTENT) properly creates a 204 response
    }

    /**
     * Get teachers by subject using query parameter.
     * This method handles Hebrew characters properly by accepting the subject name as a query parameter.
     *
     * Example: /teachers/subject?name=מתמטיקה
     *
     * @param name The subject name as a query parameter
     * @return StandardResponse with a list of teachers
     */
    @GetMapping("/subject")
    public StandardResponse getTeachersBySubject(@RequestParam String name) {
        List<TeacherDto> teachers = teacherService.getTeachersBySubject(name);
        return new StandardResponse("success", teachers, null);
    }

    /**
     * Assign student to teacher
     * Returns a StandardResponse directly to maintain consistent API response format
     */
    @PostMapping("/{teacherId}/students/{studentId}")
    public StandardResponse assignStudentToTeacher(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        TeacherDto updatedTeacher = teacherService.assignStudentToTeacher(teacherId, studentId);
        return new StandardResponse("success", updatedTeacher, null);
    }

    /**
     * Remove student from teacher
     * Returns 204 No Content without a response body, bypassing GlobalResponseHandler
     */
    @DeleteMapping("/{teacherId}/students/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Explicitly set the response status to 204
    public void removeStudentFromTeacher(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        teacherService.removeStudentFromTeacher(teacherId, studentId);
        // Returning void with @ResponseStatus(NO_CONTENT) properly creates a 204 response
    }
}