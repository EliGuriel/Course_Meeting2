package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.TeacherDto;
import org.example.stage3.response.StandardResponse;
import org.example.stage3.service.TeacherService;
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
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping
    public ResponseEntity<StandardResponse> getAllTeachers() {
        List<TeacherDto> teachers = teacherService.getAllTeachers();
        StandardResponse response = new StandardResponse("success", teachers, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get teacher by ID
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getTeacherById(@PathVariable Long id) {
        TeacherDto teacher = teacherService.getTeacherById(id);
        StandardResponse response = new StandardResponse("success", teacher, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get teachers by subject using query parameter.
     * This method uses query parameter to handle Hebrew characters properly.
     *
     * Example: /teachers/subject?name=מתמטיקה
     *
     * @param name The subject name as a query parameter
     * @return ResponseEntity with StandardResponse and 200 OK status
     */
    @GetMapping("/subject")
    public ResponseEntity<StandardResponse> getTeachersBySubject(@RequestParam String name) {
        List<TeacherDto> teachers = teacherService.getTeachersBySubject(name);
        StandardResponse response = new StandardResponse("success", teachers, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new teacher
     * Uses @Valid to validate teacher according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 201 Created status with location header
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
     * Uses @Valid to validate the teacher according to Jakarta Validation constraints
     * Returns ResponseEntity with StandardResponse and 200 OK status
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        TeacherDto updatedTeacher = teacherService.updateTeacher(id, teacherDto);
        StandardResponse response = new StandardResponse("success", updatedTeacher, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a teacher
     * Returns 204 No Content status without a response body
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }

    /**
     * Assign student to teacher
     * Returns ResponseEntity with StandardResponse and 200 OK status
     * Note: This operation modifies an existing resource, so 200 OK is returned instead of 201 Created
     */
    @PostMapping("/{teacherId}/students/{studentId}")
    public ResponseEntity<StandardResponse> assignStudentToTeacher(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        TeacherDto updatedTeacher = teacherService.assignStudentToTeacher(teacherId, studentId);
        StandardResponse response = new StandardResponse("success", updatedTeacher, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove student from teacher
     * Returns 204 No Content status without a response body
     */
    @DeleteMapping("/{teacherId}/students/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStudentFromTeacher(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        teacherService.removeStudentFromTeacher(teacherId, studentId);
    }
}