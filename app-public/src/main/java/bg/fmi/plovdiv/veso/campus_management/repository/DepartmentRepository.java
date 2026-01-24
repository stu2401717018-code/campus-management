package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>
{
    /**
     * Find department by name
     */
    Optional<Department> findByName(String name);

    /**
     * Find department by code
     */
    Optional<Department> findByCode(String code);

    /**
     * Check if department exists by name
     */
    boolean existsByName(String name);

    /**
     * Check if department exists by code
     */
    boolean existsByCode(String code);
}
