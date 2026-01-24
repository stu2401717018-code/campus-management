package bg.fmi.plovdiv.veso.campus_management.controller;

import bg.fmi.plovdiv.veso.campus_management.dto.StudentDTO;
import bg.fmi.plovdiv.veso.campus_management.dto.StudentFilterDTO;
import bg.fmi.plovdiv.veso.campus_management.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO createTestStudentDTO()
    {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setStudentNumber("STU001");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 15));
        dto.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        return dto;
    }

    @Test
    void testGetAllStudents() throws Exception
    {
        List<StudentDTO> students = Arrays.asList(createTestStudentDTO());
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void testGetStudentById() throws Exception
    {
        StudentDTO student = createTestStudentDTO();
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testCreateStudent() throws Exception
    {
        StudentDTO inputDTO = createTestStudentDTO();
        inputDTO.setId(null);
        StudentDTO createdDTO = createTestStudentDTO();

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(createdDTO);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateStudent_PartialUpdate() throws Exception
    {
        StudentDTO updateDTO = new StudentDTO();
        updateDTO.setStudentNumber("STU005");
        StudentDTO updatedDTO = createTestStudentDTO();
        updatedDTO.setStudentNumber("STU005");

        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentNumber").value("STU005"));
    }

    @Test
    void testDeleteStudent() throws Exception
    {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchStudents() throws Exception
    {
        StudentFilterDTO filter = new StudentFilterDTO();
        filter.setStudentName("John");
        filter.setCity("Plovdiv");

        List<StudentDTO> results = Arrays.asList(createTestStudentDTO());
        when(studentService.searchStudents(any(StudentFilterDTO.class))).thenReturn(results);

        mockMvc.perform(post("/api/students/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}
