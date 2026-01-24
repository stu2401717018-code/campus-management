package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Professor entity.
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>
{
    /**
     * Find professor by email
     */
    Optional<Professor> findByEmail(String email);

    /**
     * Find all professors by department
     */
    List<Professor> findByDepartmentId(Long departmentId);

    /**
     * Find professors by specialization
     */
    List<Professor> findBySpecialization(String specialization);

    /**
     * Find professors by title
     */
    List<Professor> findByTitle(String title);

    /**
     * Check if professor exists by email
     */
    boolean existsByEmail(String email);
}
