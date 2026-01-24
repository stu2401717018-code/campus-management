package bg.fmi.plovdiv.veso.campus_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Course entity representing an academic course.
 * Relationships:
 * - Many-to-One with Department
 * - One-to-Many with Enrollment
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"department", "enrollments"})
@ToString(exclude = {"department", "enrollments"})
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String courseCode;

    @Column(nullable = false, length = 200)
    private String courseName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer credits;

    /**
     * Many-to-One relationship with Department
     * Multiple courses can belong to one department
     */
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * One-to-Many relationship with Enrollment
     * A course can have multiple student enrollments
     */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    public void addEnrollment(Enrollment enrollment)
    {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    public void removeEnrollment(Enrollment enrollment)
    {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }
}
