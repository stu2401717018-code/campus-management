package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Club entity.
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long>
{
    /**
     * Find club by name
     */
    Optional<Club> findByName(String name);

    /**
     * Find clubs by category
     */
    List<Club> findByCategory(String category);

    /**
     * Find clubs by name containing (case-insensitive search)
     */
    List<Club> findByNameContainingIgnoreCase(String name);

    /**
     * Check if club exists by name
     */
    boolean existsByName(String name);

    /**
     * Find clubs that a specific student is a member of
     */
    @Query("SELECT c FROM Club c JOIN c.students s WHERE s.id = :studentId")
    List<Club> findClubsByStudentId(Long studentId);
}
