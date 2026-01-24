package bg.fmi.plovdiv.veso.campus_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Department entity representing an academic department.
 * Relationships:
 * - One-to-Many with Course
 * - One-to-Many with Professor
 */
@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"courses", "professors"})
@ToString(exclude = {"courses", "professors"})
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @Column(length = 1000)
    private String description;

    @Column(length = 200)
    private String building;

    /**
     * One-to-Many relationship with Course
     * A department can offer multiple courses
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    /**
     * One-to-Many relationship with Professor
     * A department can have multiple professors
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Professor> professors = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.setDepartment(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setDepartment(null);
    }

    public void addProfessor(Professor professor) {
        professors.add(professor);
        professor.setDepartment(this);
    }

    public void removeProfessor(Professor professor) {
        professors.remove(professor);
        professor.setDepartment(null);
    }
}