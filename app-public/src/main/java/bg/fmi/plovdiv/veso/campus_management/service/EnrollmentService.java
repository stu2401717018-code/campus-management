package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.EnrollmentDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Course;
import bg.fmi.plovdiv.veso.campus_management.model.Enrollment;
import bg.fmi.plovdiv.veso.campus_management.model.Student;
import bg.fmi.plovdiv.veso.campus_management.repository.CourseRepository;
import bg.fmi.plovdiv.veso.campus_management.repository.EnrollmentRepository;
import bg.fmi.plovdiv.veso.campus_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Enrollment business logic.
 */
@Service
@Transactional
public class EnrollmentService
{
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository)
    {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<EnrollmentDTO> getAllEnrollments()
    {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EnrollmentDTO getEnrollmentById(Long id)
    {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));

        return convertToDTO(enrollment);
    }

    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO)
    {
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                enrollmentDTO.getStudentId(), enrollmentDTO.getCourseId())) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        Enrollment enrollment = convertToEntity(enrollmentDTO);

        if (enrollment.getStatus() == null || enrollment.getStatus().isEmpty()) {
            enrollment.setStatus("Active");
        }

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return convertToDTO(savedEnrollment);
    }

    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO)
    {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));

        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollment.setSemester(enrollmentDTO.getSemester());
        enrollment.setYear(enrollmentDTO.getYear());
        enrollment.setStatus(enrollmentDTO.getStatus());
        enrollment.setGrade(enrollmentDTO.getGrade());

        if (!enrollment.getStudent().getId().equals(enrollmentDTO.getStudentId())) {
            Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", enrollmentDTO.getStudentId()));
            enrollment.setStudent(student);
        }

        if (!enrollment.getCourse().getId().equals(enrollmentDTO.getCourseId())) {
            Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", enrollmentDTO.getCourseId()));
            enrollment.setCourse(course);
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return convertToDTO(updatedEnrollment);
    }

    public void deleteEnrollment(Long id)
    {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment", "id", id);
        }
        enrollmentRepository.deleteById(id);
    }

    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId)
    {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId)
    {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getActiveEnrollmentsByStudent(Long studentId)
    {
        return enrollmentRepository.findActiveEnrollmentsByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment)
    {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setSemester(enrollment.getSemester());
        dto.setYear(enrollment.getYear());
        dto.setStatus(enrollment.getStatus());
        dto.setGrade(enrollment.getGrade());

        dto.setStudentName(enrollment.getStudent().getFirstName() + " " +
                enrollment.getStudent().getLastName());
        dto.setCourseName(enrollment.getCourse().getCourseName());

        return dto;
    }

    private Enrollment convertToEntity(EnrollmentDTO dto)
    {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setSemester(dto.getSemester());
        enrollment.setYear(dto.getYear());
        enrollment.setStatus(dto.getStatus());
        enrollment.setGrade(dto.getGrade());

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));
        enrollment.setStudent(student);

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));
        enrollment.setCourse(course);

        return enrollment;
    }
}
