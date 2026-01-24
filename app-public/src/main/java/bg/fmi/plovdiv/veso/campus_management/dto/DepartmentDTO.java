package bg.fmi.plovdiv.veso.campus_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Department entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO
{
    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code must not exceed 20 characters")
    private String code;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 200, message = "Building must not exceed 200 characters")
    private String building;
}
