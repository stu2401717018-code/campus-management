package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.EnrollmentDTO;
import bg.fmi.plovdiv.veso.campus_management.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Enrollment endpoints.
 */
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController
{
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService)
    {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Get all enrollments
     * GET /api/enrollments
     */
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments()
    {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Get enrollment by ID
     * GET /api/enrollments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id)
    {
        EnrollmentDTO enrollment = enrollmentService.getEnrollmentById(id);

        return ResponseEntity.ok(enrollment);
    }

    /**
     * Create new enrollment (enroll student in course)
     * POST /api/enrollments
     */
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@Valid @RequestBody EnrollmentDTO enrollmentDTO)
    {
        EnrollmentDTO createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
    }

    /**
     * Update existing enrollment
     * PUT /api/enrollments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentDTO enrollmentDTO)
    {
        EnrollmentDTO updatedEnrollment = enrollmentService.updateEnrollment(id, enrollmentDTO);

        return ResponseEntity.ok(updatedEnrollment);
    }

    /**
     * Delete enrollment
     * DELETE /api/enrollments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id)
    {
        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get enrollments by student
     * GET /api/enrollments/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId)
    {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Get enrollments by course
     * GET /api/enrollments/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable Long courseId)
    {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Get active enrollments for a student
     * GET /api/enrollments/student/{studentId}/active
     */
    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<List<EnrollmentDTO>> getActiveEnrollmentsByStudent(@PathVariable Long studentId)
    {
        List<EnrollmentDTO> enrollments = enrollmentService.getActiveEnrollmentsByStudent(studentId);

        return ResponseEntity.ok(enrollments);
    }
}
