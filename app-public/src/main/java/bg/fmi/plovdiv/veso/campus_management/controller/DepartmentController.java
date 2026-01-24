package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.DepartmentDTO;
import bg.fmi.plovdiv.veso.campus_management.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Department endpoints.
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController
{
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService)
    {
        this.departmentService = departmentService;
    }

    /**
     * Get all departments
     * GET /api/departments
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments()
    {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(departments);
    }

    /**
     * Get department by ID
     * GET /api/departments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id)
    {
        DepartmentDTO department = departmentService.getDepartmentById(id);

        return ResponseEntity.ok(department);
    }

    /**
     * Create new department
     * POST /api/departments
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO)
    {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    /**
     * Update existing department
     * PUT /api/departments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO)
    {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, departmentDTO);

        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * Delete department
     * DELETE /api/departments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id)
    {
        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }
}
