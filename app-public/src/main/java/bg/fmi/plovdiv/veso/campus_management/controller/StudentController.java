package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.StudentDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.StudentFilterDTO;
import bg.fmi.plovdiv.veso.campus_management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Student endpoints.
 * Handles HTTP requests for student management.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController
{
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService)
    {
        this.studentService = studentService;
    }

    /**
     * Get all students
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents()
    {
        List<StudentDTO> students = studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id)
    {
        StudentDTO student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    /**
     * Create new student
     * POST /api/students
     */
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO)
    {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    /**
     * Update existing student
     * PUT /api/students/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDTO studentDTO)
    {
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);

        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Delete student
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id)
    {
        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Dynamic search for students
     * POST /api/students/search
     * <p>
     * This endpoint demonstrates dynamic search using JPA Specification.
     * Accepts StudentFilterDTO with multiple optional search criteria.
     */
    @PostMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(@RequestBody StudentFilterDTO filter)
    {
        List<StudentDTO> students = studentService.searchStudents(filter);

        return ResponseEntity.ok(students);
    }
}
