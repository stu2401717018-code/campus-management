package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
    /**
     * Find course by course code
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * Find all courses by department
     */
    List<Course> findByDepartmentId(Long departmentId);

    /**
     * Find courses by credits
     */
    List<Course> findByCredits(Integer credits);

    /**
     * Find courses by name containing (case-insensitive search)
     */
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    /**
     * Check if course exists by course code
     */
    boolean existsByCourseCode(String courseCode);
}
