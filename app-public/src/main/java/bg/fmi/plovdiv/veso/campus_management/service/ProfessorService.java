package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.ProfessorDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Department;
import bg.fmi.plovdiv.veso.campus_management.model.Professor;
import bg.fmi.plovdiv.veso.campus_management.repository.DepartmentRepository;
import bg.fmi.plovdiv.veso.campus_management.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Professor business logic.
 */
@Service
@Transactional
public class ProfessorService
{
    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository, DepartmentRepository departmentRepository)
    {
        this.professorRepository = professorRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<ProfessorDTO> getAllProfessors()
    {
        return professorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProfessorDTO getProfessorById(Long id)
    {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));

        return convertToDTO(professor);
    }

    public ProfessorDTO createProfessor(ProfessorDTO professorDTO)
    {
        if (professorRepository.existsByEmail(professorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + professorDTO.getEmail());
        }

        Professor professor = convertToEntity(professorDTO);
        Professor savedProfessor = professorRepository.save(professor);

        return convertToDTO(savedProfessor);
    }

    public ProfessorDTO updateProfessor(Long id, ProfessorDTO professorDTO)
    {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));

        if (!professor.getEmail().equals(professorDTO.getEmail()) &&
                professorRepository.existsByEmail(professorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + professorDTO.getEmail());
        }

        professor.setFirstName(professorDTO.getFirstName());
        professor.setLastName(professorDTO.getLastName());
        professor.setEmail(professorDTO.getEmail());
        professor.setPhoneNumber(professorDTO.getPhoneNumber());
        professor.setSpecialization(professorDTO.getSpecialization());
        professor.setTitle(professorDTO.getTitle());

        if (!professor.getDepartment().getId().equals(professorDTO.getDepartmentId())) {
            Department department = departmentRepository.findById(professorDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", professorDTO.getDepartmentId()));
            professor.setDepartment(department);
        }

        Professor updatedProfessor = professorRepository.save(professor);

        return convertToDTO(updatedProfessor);
    }

    public void deleteProfessor(Long id)
    {
        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Professor", "id", id);
        }
        professorRepository.deleteById(id);
    }

    public List<ProfessorDTO> getProfessorsByDepartment(Long departmentId)
    {
        return professorRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProfessorDTO convertToDTO(Professor professor)
    {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setFirstName(professor.getFirstName());
        dto.setLastName(professor.getLastName());
        dto.setEmail(professor.getEmail());
        dto.setPhoneNumber(professor.getPhoneNumber());
        dto.setSpecialization(professor.getSpecialization());
        dto.setTitle(professor.getTitle());
        dto.setDepartmentId(professor.getDepartment().getId());
        dto.setDepartmentName(professor.getDepartment().getName());

        return dto;
    }

    private Professor convertToEntity(ProfessorDTO dto)
    {
        Professor professor = new Professor();
        professor.setFirstName(dto.getFirstName());
        professor.setLastName(dto.getLastName());
        professor.setEmail(dto.getEmail());
        professor.setPhoneNumber(dto.getPhoneNumber());
        professor.setSpecialization(dto.getSpecialization());
        professor.setTitle(dto.getTitle());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
        professor.setDepartment(department);

        return professor;
    }
}
