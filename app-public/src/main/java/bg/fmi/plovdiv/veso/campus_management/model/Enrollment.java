package bg.fmi.plovdiv.veso.campus_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Enrollment entity representing a student's enrollment in a course.
 * This is a join entity between Student and Course with additional attributes.
 * Relationships:
 * - Many-to-One with Student
 * - Many-to-One with Course
 */
@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id", "semester"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"student", "course"})
@ToString(exclude = {"student", "course"})
public class Enrollment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-One relationship with Student
     * Multiple enrollments can belong to one student
     */
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * Many-to-One relationship with Course
     * Multiple enrollments can be for one course
     */
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @Column(nullable = false, length = 20)
    private String semester; // e.g., "Fall 2024", "Spring 2025"

    @Column(nullable = false)
    private Integer year;

    @Column(length = 20)
    private String status; // e.g., "Active", "Completed", "Dropped", "Withdrawn"

    @Column
    private Double grade; // Final grade (nullable until course is completed)
}
