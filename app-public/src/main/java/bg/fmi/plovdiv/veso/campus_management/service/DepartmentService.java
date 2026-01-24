package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.DepartmentDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Department;
import bg.fmi.plovdiv.veso.campus_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Department business logic.
 */
@Service
@Transactional
public class DepartmentService
{
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository)
    {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments()
    {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartmentById(Long id)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        return convertToDTO(department);
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO)
    {
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department name already exists: " + departmentDTO.getName());
        }
        if (departmentRepository.existsByCode(departmentDTO.getCode())) {
            throw new IllegalArgumentException("Department code already exists: " + departmentDTO.getCode());
        }

        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);

        return convertToDTO(savedDepartment);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO)
    {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (!department.getName().equals(departmentDTO.getName()) &&
                departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department name already exists: " + departmentDTO.getName());
        }

        if (!department.getCode().equals(departmentDTO.getCode()) &&
                departmentRepository.existsByCode(departmentDTO.getCode())) {
            throw new IllegalArgumentException("Department code already exists: " + departmentDTO.getCode());
        }

        department.setName(departmentDTO.getName());
        department.setCode(departmentDTO.getCode());
        department.setDescription(departmentDTO.getDescription());
        department.setBuilding(departmentDTO.getBuilding());

        Department updatedDepartment = departmentRepository.save(department);

        return convertToDTO(updatedDepartment);
    }

    public void deleteDepartment(Long id)
    {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentDTO convertToDTO(Department department)
    {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setCode(department.getCode());
        dto.setDescription(department.getDescription());
        dto.setBuilding(department.getBuilding());

        return dto;
    }

    private Department convertToEntity(DepartmentDTO dto)
    {
        Department department = new Department();
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setDescription(dto.getDescription());
        department.setBuilding(dto.getBuilding());

        return department;
    }
}
