package bg.fmi.plovdiv.veso.campus_management.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Club entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO
{
    private Long id;

    @NotBlank(message = "Club name is required")
    @Size(max = 100, message = "Club name must not exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Email(message = "President email must be valid")
    @Size(max = 150, message = "President email must not exceed 150 characters")
    private String presidentEmail;

    @Min(value = 1, message = "Member limit must be at least 1")
    private Integer memberLimit;

    private Integer currentMemberCount;
}
