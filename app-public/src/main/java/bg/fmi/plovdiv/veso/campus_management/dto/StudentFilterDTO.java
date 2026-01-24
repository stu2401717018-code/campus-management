package bg.fmi.plovdiv.veso.campus_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter DTO for dynamic student search.
 * Contains minimum 5 fields as required by project specifications:
 * 1. Student name (firstName or lastName)
 * 2. City (from Address - associated entity)
 * 3. Club name (from Club - associated entity)
 * 4. Course name (from Enrollment/Course - associated entity)
 * 5. Enrollment year (from Enrollment - associated entity)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDTO
{
    /**
     * Filter by student first name or last name (partial match, case-insensitive)
     */
    private String studentName;

    /**
     * Filter by city from Address entity (exact match)
     */
    private String city;

    /**
     * Filter by club name from Club entity (partial match, case-insensitive)
     */
    private String clubName;

    /**
     * Filter by course name from Course entity (partial match, case-insensitive)
     */
    private String courseName;

    /**
     * Filter by enrollment year from Enrollment entity (exact match)
     */
    private Integer enrollmentYear;

    /**
     * Additional filter: Email (optional)
     */
    private String email;

    /**
     * Additional filter: Student number (optional)
     */
    private String studentNumber;
}
