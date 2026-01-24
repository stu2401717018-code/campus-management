package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.ProfessorDTO;
import bg.fmi.plovdiv.veso.campus_management.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Professor endpoints.
 */
@RestController
@RequestMapping("/api/professors")
public class ProfessorController
{
    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService)
    {
        this.professorService = professorService;
    }

    /**
     * Get all professors
     * GET /api/professors
     */
    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors()
    {
        List<ProfessorDTO> professors = professorService.getAllProfessors();

        return ResponseEntity.ok(professors);
    }

    /**
     * Get professor by ID
     * GET /api/professors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable Long id)
    {
        ProfessorDTO professor = professorService.getProfessorById(id);

        return ResponseEntity.ok(professor);
    }

    /**
     * Create new professor
     * POST /api/professors
     */
    @PostMapping
    public ResponseEntity<ProfessorDTO> createProfessor(@Valid @RequestBody ProfessorDTO professorDTO)
    {
        ProfessorDTO createdProfessor = professorService.createProfessor(professorDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessor);
    }

    /**
     * Update existing professor
     * PUT /api/professors/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> updateProfessor(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorDTO professorDTO)
    {
        ProfessorDTO updatedProfessor = professorService.updateProfessor(id, professorDTO);

        return ResponseEntity.ok(updatedProfessor);
    }

    /**
     * Delete professor
     * DELETE /api/professors/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id)
    {
        professorService.deleteProfessor(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get professors by department
     * GET /api/professors/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ProfessorDTO>> getProfessorsByDepartment(@PathVariable Long departmentId)
    {
        List<ProfessorDTO> professors = professorService.getProfessorsByDepartment(departmentId);

        return ResponseEntity.ok(professors);
    }
}
