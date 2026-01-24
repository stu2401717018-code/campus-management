package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.CourseDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Course;
import bg.fmi.plovdiv.veso.campus_management.model.Department;
import bg.fmi.plovdiv.veso.campus_management.repository.CourseRepository;
import bg.fmi.plovdiv.veso.campus_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Course business logic.
 */
@Service
@Transactional
public class CourseService
{
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, DepartmentRepository departmentRepository)
    {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<CourseDTO> getAllCourses()
    {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id)
    {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        return convertToDTO(course);
    }

    public CourseDTO createCourse(CourseDTO courseDTO)
    {
        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists: " + courseDTO.getCourseCode());
        }

        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);

        return convertToDTO(savedCourse);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO)
    {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        if (!course.getCourseCode().equals(courseDTO.getCourseCode()) &&
                courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists: " + courseDTO.getCourseCode());
        }

        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());
        course.setCredits(courseDTO.getCredits());

        if (!course.getDepartment().getId().equals(courseDTO.getDepartmentId())) {
            Department department = departmentRepository.findById(courseDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", courseDTO.getDepartmentId()));
            course.setDepartment(department);
        }

        Course updatedCourse = courseRepository.save(course);

        return convertToDTO(updatedCourse);
    }

    public void deleteCourse(Long id)
    {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    public List<CourseDTO> getCoursesByDepartment(Long departmentId)
    {
        return courseRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CourseDTO convertToDTO(Course course)
    {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setDepartmentId(course.getDepartment().getId());
        dto.setDepartmentName(course.getDepartment().getName());

        return dto;
    }

    private Course convertToEntity(CourseDTO dto)
    {
        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
        course.setDepartment(department);

        return course;
    }
}
