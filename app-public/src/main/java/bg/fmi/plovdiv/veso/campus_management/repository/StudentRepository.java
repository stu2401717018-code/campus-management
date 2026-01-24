package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Student entity.
 * Extends JpaSpecificationExecutor for dynamic search capabilities.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student>
{
    /**
     * Find student by email
     */
    Optional<Student> findByEmail(String email);

    /**
     * Find student by student number
     */
    Optional<Student> findByStudentNumber(String studentNumber);

    /**
     * Check if student exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Check if student exists by student number
     */
    boolean existsByStudentNumber(String studentNumber);
}
