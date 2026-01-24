package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.CourseDTO;
import bg.fmi.plovdiv.veso.campus_management.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Course endpoints.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController
{
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService)
    {
        this.courseService = courseService;
    }

    /**
     * Get all courses
     * GET /api/courses
     */
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses()
    {
        List<CourseDTO> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    /**
     * Get course by ID
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id)
    {
        CourseDTO course = courseService.getCourseById(id);

        return ResponseEntity.ok(course);
    }

    /**
     * Create new course
     * POST /api/courses
     */
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO)
    {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    /**
     * Update existing course
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDTO courseDTO)
    {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);

        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Delete course
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id)
    {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get courses by department
     * GET /api/courses/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByDepartment(@PathVariable Long departmentId)
    {
        List<CourseDTO> courses = courseService.getCoursesByDepartment(departmentId);

        return ResponseEntity.ok(courses);
    }
}
