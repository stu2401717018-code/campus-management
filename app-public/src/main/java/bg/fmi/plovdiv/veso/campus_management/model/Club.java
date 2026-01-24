package bg.fmi.plovdiv.veso.campus_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Club entity representing a student club or organization.
 * Relationship: Many-to-Many with Student
 */
@Entity
@Table(name = "clubs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "students")
@ToString(exclude = "students")
public class Club
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String category;

    @Column(length = 150)
    private String presidentEmail;

    @Column
    private Integer memberLimit;

    /**
     * Many-to-Many relationship with Student
     * A club can have multiple students as members
     * mappedBy indicates Student is the owner of the relationship
     */
    @ManyToMany(mappedBy = "clubs")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    public void addStudent(Student student)
    {
        students.add(student);
        student.getClubs().add(this);
    }

    public void removeStudent(Student student)
    {
        students.remove(student);
        student.getClubs().remove(this);
    }

    public int getCurrentMemberCount()
    {

        return students.size();
    }

    public boolean isAtCapacity()
    {

        return memberLimit != null && students.size() >= memberLimit;
    }
}
