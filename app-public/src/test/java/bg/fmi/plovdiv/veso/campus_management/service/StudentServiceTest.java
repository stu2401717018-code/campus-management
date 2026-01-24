package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.StudentDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.StudentFilterDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Address;
import bg.fmi.plovdiv.veso.campus_management.model.Student;
import bg.fmi.plovdiv.veso.campus_management.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest
{
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp()
    {
        Address address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("Plovdiv");
        address.setState("Plovdiv");
        address.setPostalCode("4000");
        address.setCountry("Bulgaria");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john.doe@example.com");
        testStudent.setStudentNumber("STU001");
        testStudent.setDateOfBirth(LocalDate.of(2000, 1, 15));
        testStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        testStudent.setAddress(address);

        testStudentDTO = new StudentDTO();
        testStudentDTO.setFirstName("John");
        testStudentDTO.setLastName("Doe");
        testStudentDTO.setEmail("john.doe@example.com");
        testStudentDTO.setStudentNumber("STU001");
        testStudentDTO.setDateOfBirth(LocalDate.of(2000, 1, 15));
        testStudentDTO.setEnrollmentDate(LocalDate.of(2023, 9, 1));
    }

    @Test
    void testGetAllStudents()
    {
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Success()
    {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        StudentDTO result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound()
    {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.getStudentById(999L);
        });
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateStudent_Success()
    {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByStudentNumber(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        StudentDTO result = studentService.createStudent(testStudentDTO);

        assertNotNull(result);
        verify(studentRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(studentRepository, times(1)).existsByStudentNumber("STU001");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_DuplicateEmail()
    {
        when(studentRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(testStudentDTO);
        });
        verify(studentRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_PartialUpdate()
    {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.existsByStudentNumber("STU005")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        StudentDTO updateDTO = new StudentDTO();
        updateDTO.setStudentNumber("STU005");

        StudentDTO result = studentService.updateStudent(1L, updateDTO);

        assertNotNull(result);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).existsByStudentNumber("STU005");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound()
    {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.updateStudent(999L, testStudentDTO);
        });
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testDeleteStudent_Success()
    {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_NotFound()
    {
        when(studentRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.deleteStudent(999L);
        });
        verify(studentRepository, times(1)).existsById(999L);
        verify(studentRepository, never()).deleteById(anyLong());
    }
}
