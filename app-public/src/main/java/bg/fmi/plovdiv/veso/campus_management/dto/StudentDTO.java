package bg.fmi.plovdiv.veso.campus_management.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Student entity.
 * Used for API requests/responses with validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO
{
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Student number is required")
    @Size(max = 20, message = "Student number must not exceed 20 characters")
    private String studentNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    private AddressDTO address;
}
