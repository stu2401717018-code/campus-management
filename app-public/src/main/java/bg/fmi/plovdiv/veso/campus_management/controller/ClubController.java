package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.ClubDTO;
import bg.fmi.plovdiv.veso.campus_management.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Club endpoints.
 */
@RestController
@RequestMapping("/api/clubs")
public class ClubController
{
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService)
    {
        this.clubService = clubService;
    }

    /**
     * Get all clubs
     * GET /api/clubs
     */
    @GetMapping
    public ResponseEntity<List<ClubDTO>> getAllClubs()
    {
        List<ClubDTO> clubs = clubService.getAllClubs();

        return ResponseEntity.ok(clubs);
    }

    /**
     * Get club by ID
     * GET /api/clubs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long id)
    {
        ClubDTO club = clubService.getClubById(id);

        return ResponseEntity.ok(club);
    }

    /**
     * Create new club
     * POST /api/clubs
     */
    @PostMapping
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody ClubDTO clubDTO)
    {
        ClubDTO createdClub = clubService.createClub(clubDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdClub);
    }

    /**
     * Update existing club
     * PUT /api/clubs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClubDTO> updateClub(
            @PathVariable Long id,
            @Valid @RequestBody ClubDTO clubDTO)
    {
        ClubDTO updatedClub = clubService.updateClub(id, clubDTO);

        return ResponseEntity.ok(updatedClub);
    }

    /**
     * Delete club
     * DELETE /api/clubs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id)
    {
        clubService.deleteClub(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Add student to club
     * POST /api/clubs/{clubId}/students/{studentId}
     */
    @PostMapping("/{clubId}/students/{studentId}")
    public ResponseEntity<ClubDTO> addStudentToClub(
            @PathVariable Long clubId,
            @PathVariable Long studentId)
    {
        ClubDTO club = clubService.addStudentToClub(clubId, studentId);

        return ResponseEntity.ok(club);
    }

    /**
     * Remove student from club
     * DELETE /api/clubs/{clubId}/students/{studentId}
     */
    @DeleteMapping("/{clubId}/students/{studentId}")
    public ResponseEntity<ClubDTO> removeStudentFromClub(
            @PathVariable Long clubId,
            @PathVariable Long studentId)
    {
        ClubDTO club = clubService.removeStudentFromClub(clubId, studentId);

        return ResponseEntity.ok(club);
    }

    /**
     * Get clubs that a student is a member of
     * GET /api/clubs/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClubDTO>> getClubsByStudent(@PathVariable Long studentId)
    {
        List<ClubDTO> clubs = clubService.getClubsByStudent(studentId);

        return ResponseEntity.ok(clubs);
    }
}
