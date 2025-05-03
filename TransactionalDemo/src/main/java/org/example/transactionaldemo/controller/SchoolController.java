package org.example.transactionaldemo.controller;

import jakarta.validation.Valid;
import org.example.transactionaldemo.dto.CreateRequest;
import org.example.transactionaldemo.dto.UpdateRequest;
import org.example.transactionaldemo.entity.Student;
import org.example.transactionaldemo.entity.Teacher;
import org.example.transactionaldemo.response.StandardResponse;
import org.example.transactionaldemo.service.ISchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * REST Controller for school-related operations.
 * Handles HTTP requests for creating and updating teachers and students.
 */
@RestController
@RequestMapping("/api/school")
public class SchoolController {
    private static final Logger logger = Logger.getLogger(SchoolController.class.getName());

    private final ISchoolService schoolService;

    @Autowired
    public SchoolController(ISchoolService schoolService) {
        this.schoolService = schoolService;
    }

    /**
     * Creates a teacher and student as a transactional operation.
     * Returns ResponseEntity with StandardResponse and 200 OK status
     *
     * Endpoint: POST /api/school/create
     * Example request:
     * {
     *   "teacherName": "John Smith",
     *   "subject": "Mathematics",
     *   "studentName": "Jane Doe",
     *   "studentEmail": "jane.doe@example.com"
     * }
     *
     * Transaction flow:
     * 1. The Controller creates Teacher and Student objects from request
     * 2. Service method is called and performs transaction
     * 3. If successful, a success response is returned
     * 4. If an error occurs, the transaction is rolled back and
     *    GlobalExceptionHandler returns an error response
     *
     * @param request DTO containing teacher and student information
     * @return ResponseEntity with StandardResponse containing success message
     */
    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createTeacherAndStudent(@Valid @RequestBody CreateRequest request) {
        logger.info("Received create request: " + request);

        // Create entities from the request
        Teacher teacher = new Teacher(null, request.getTeacherName(), request.getSubject());
        Student student = new Student(null, request.getStudentName(), request.getStudentEmail());

        // Call the service method - this will be processed in a transaction
        // If the subject is "Invalid", it will throw a RollingBackException
        schoolService.createTeacherAndStudent(teacher, student);

        logger.info("Successfully created teacher and student");
        StandardResponse response = new StandardResponse("success", "Teacher and student created successfully", null);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates a teacher and student as a transactional operation.
     * Returns ResponseEntity with StandardResponse and 200 OK status
     *
     * Endpoint: PUT /api/school/update
     * Example request:
     * {
     *   "teacherId": 1,
     *   "studentId": 1,
     *   "newTeacherName": "John Updated",
     *   "newStudentName": "Jane Updated"
     * }
     *
     * Transaction behavior:
     * - Both updates succeed, or both are rolled back
     * - If the newTeacherName contains "error", a RollingBackException will be thrown
     *   and the transaction will be rolled back
     *
     * @param request DTO containing update information
     * @return ResponseEntity with StandardResponse containing success message
     */
    @PutMapping("/update")
    public ResponseEntity<StandardResponse> updateTeacherAndStudent(@Valid @RequestBody UpdateRequest request) {
        logger.info("Received update request: " + request);

        // Call the service method - this will be processed in a transaction
        schoolService.updateTeacherAndStudent(
                request.getTeacherId(),
                request.getStudentId(),
                request.getNewTeacherName(),
                request.getNewStudentName());

        logger.info("Successfully updated teacher and student");
        StandardResponse response = new StandardResponse("success", "Teacher and student updated successfully", null);
        return ResponseEntity.ok(response);
    }

    /**
     * Test endpoint to demonstrate transaction rollback.
     * Creates a teacher with an invalid subject to trigger rollback.
     * Returns ResponseEntity with StandardResponse and 200 OK status (never actually returns)
     *
     * Endpoint: POST /api/school/test-rollback
     *
     * @return This endpoint will never return normally as it always throws an exception
     */
    @PostMapping("/test-rollback")
    public ResponseEntity<StandardResponse> testTransactionRollback() {
        logger.info("Testing transaction rollback with invalid subject");

        Teacher teacher = new Teacher(null, "Test Teacher", "Invalid");
        Student student = new Student(null, "Test Student", "test@example.com");

        // This will throw RollingBackException because the subject is "Invalid"
        schoolService.createTeacherAndStudent(teacher, student);

        // This code will never be reached
        StandardResponse response = new StandardResponse("success", "This will never be returned", null);
        return ResponseEntity.ok(response);
    }
}