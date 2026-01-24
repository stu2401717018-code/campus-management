package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.StudentDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.StudentFilterDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Address;
import bg.fmi.plovdiv.veso.campus_management.model.Student;
import bg.fmi.plovdiv.veso.campus_management.repository.StudentRepository;
import bg.fmi.plovdiv.veso.campus_management.specification.StudentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Student business logic.
 */
@Service
@Transactional
public class StudentService
{
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository)
    {
        this.studentRepository = studentRepository;
    }

    /**
     * Get all students
     */
    public List<StudentDTO> getAllStudents()
    {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get student by ID
     */
    public StudentDTO getStudentById(Long id)
    {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        return convertToDTO(student);
    }

    /**
     * Create new student
     */
    public StudentDTO createStudent(StudentDTO studentDTO)
    {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
        }

        if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already exists: " + studentDTO.getStudentNumber());
        }

        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);

        return convertToDTO(savedStudent);
    }

    /**
     * Update existing student
     */
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO)
    {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        if (studentDTO.getEmail() != null && !studentDTO.getEmail().isEmpty()) {
            if (!student.getEmail().equals(studentDTO.getEmail()) &&
                    studentRepository.existsByEmail(studentDTO.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
            }
            student.setEmail(studentDTO.getEmail());
        }

        if (studentDTO.getStudentNumber() != null && !studentDTO.getStudentNumber().isEmpty()) {
            if (!student.getStudentNumber().equals(studentDTO.getStudentNumber()) &&
                    studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
                throw new IllegalArgumentException("Student number already exists: " + studentDTO.getStudentNumber());
            }
            student.setStudentNumber(studentDTO.getStudentNumber());
        }

        if (studentDTO.getFirstName() != null && !studentDTO.getFirstName().isEmpty()) {
            student.setFirstName(studentDTO.getFirstName());
        }

        if (studentDTO.getLastName() != null && !studentDTO.getLastName().isEmpty()) {
            student.setLastName(studentDTO.getLastName());
        }

        if (studentDTO.getDateOfBirth() != null) {
            student.setDateOfBirth(studentDTO.getDateOfBirth());
        }

        if (studentDTO.getEnrollmentDate() != null) {
            student.setEnrollmentDate(studentDTO.getEnrollmentDate());
        }

        if (studentDTO.getAddress() != null) {
            if (student.getAddress() == null) {
                student.setAddress(new Address());
            }
            Address address = student.getAddress();
            if (studentDTO.getAddress().getStreet() != null) {
                address.setStreet(studentDTO.getAddress().getStreet());
            }
            if (studentDTO.getAddress().getCity() != null) {
                address.setCity(studentDTO.getAddress().getCity());
            }
            if (studentDTO.getAddress().getState() != null) {
                address.setState(studentDTO.getAddress().getState());
            }
            if (studentDTO.getAddress().getPostalCode() != null) {
                address.setPostalCode(studentDTO.getAddress().getPostalCode());
            }
            if (studentDTO.getAddress().getCountry() != null) {
                address.setCountry(studentDTO.getAddress().getCountry());
            }
        }

        Student updatedStudent = studentRepository.save(student);

        return convertToDTO(updatedStudent);
    }

    /**
     * Delete student by ID
     */
    public void deleteStudent(Long id)
    {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", "id", id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Dynamic search using JPA Specification
     */
    public List<StudentDTO> searchStudents(StudentFilterDTO filter)
    {
        Specification<Student> spec = StudentSpecification.filterStudents(filter);

        return studentRepository.findAll(spec).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Entity to DTO
     */
    private StudentDTO convertToDTO(Student student)
    {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setStudentNumber(student.getStudentNumber());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setEnrollmentDate(student.getEnrollmentDate());

        if (student.getAddress() != null) {
            Address address = student.getAddress();
            dto.setAddress(new bg.fmi.plovdiv.veso.campus_management.dto.AddressDTO(
                    address.getId(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getPostalCode(),
                    address.getCountry()
            ));
        }

        return dto;
    }

    /**
     * Convert DTO to Entity
     */
    private Student convertToEntity(StudentDTO dto)
    {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setStudentNumber(dto.getStudentNumber());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setEnrollmentDate(dto.getEnrollmentDate());

        if (dto.getAddress() != null) {
            Address address = new Address();
            address.setStreet(dto.getAddress().getStreet());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setPostalCode(dto.getAddress().getPostalCode());
            address.setCountry(dto.getAddress().getCountry());
            student.setAddress(address);
        }

        return student;
    }
}
