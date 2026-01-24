package bg.fmi.plovdiv.veso.campus_management.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Enrollment entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO
{
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotBlank(message = "Semester is required")
    @Size(max = 20, message = "Semester must not exceed 20 characters")
    private String semester;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer year;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    @Min(value = 0, message = "Grade must be at least 0")
    @Max(value = 100, message = "Grade must not exceed 100")
    private Double grade;

    private String studentName;
    private String courseName;
}
