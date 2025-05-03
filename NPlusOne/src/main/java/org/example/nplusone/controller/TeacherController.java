package org.example.nplusone.controller;

import org.example.nplusone.dto.TeacherDto;
import org.example.nplusone.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/with-problem")
    public ResponseEntity<List<TeacherDto>> getAllTeachersWithProblem() {
        return ResponseEntity.ok(teacherService.getAllTeachersWithN1Problem());
    }

    @GetMapping("/without-problem")
    public ResponseEntity<List<TeacherDto>> getAllTeachersWithoutProblem() {
        return ResponseEntity.ok(teacherService.getAllTeachersWithoutN1Problem());
    }

    @GetMapping("/with-entity-graph")
    public ResponseEntity<List<TeacherDto>> getAllTeachersWithEntityGraph() {
        return ResponseEntity.ok(teacherService.getAllTeachersWithEntityGraph());
    }
}
