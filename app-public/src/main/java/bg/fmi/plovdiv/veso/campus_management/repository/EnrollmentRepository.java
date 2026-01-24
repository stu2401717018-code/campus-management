package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enrollment entity.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>
{
    /**
     * Find all enrollments for a specific student
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Find all enrollments for a specific course
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     * Find enrollments by semester
     */
    List<Enrollment> findBySemester(String semester);

    /**
     * Find enrollments by year
     */
    List<Enrollment> findByYear(Integer year);

    /**
     * Find enrollments by status
     */
    List<Enrollment> findByStatus(String status);

    /**
     * Find enrollment by student and course
     */
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Check if student is enrolled in a course
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Find active enrollments for a student
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'Active'")
    List<Enrollment> findActiveEnrollmentsByStudentId(Long studentId);
}
