package bg.fmi.plovdiv.veso.campus_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Student entity representing a student in the campus management system.
 * Relationships:
 * - One-to-One with Address
 * - One-to-Many with Enrollment
 * - Many-to-Many with Club
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"address", "enrollments", "clubs"})
@ToString(exclude = {"address", "enrollments", "clubs"})
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String studentNumber;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    /**
     * One-to-One relationship with Address
     * CascadeType.ALL ensures address is saved/deleted with student
     * orphanRemoval = true ensures old address is deleted when replaced
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    /**
     * One-to-Many relationship with Enrollment
     * A student can be enrolled in multiple courses
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    /**
     * Many-to-Many relationship with Club
     * A student can join multiple clubs
     */
    @ManyToMany
    @JoinTable(
            name = "student_clubs",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private Set<Club> clubs = new HashSet<>();

    public void setAddress(Address address)
    {
        this.address = address;
        if (address != null) {
            address.setStudent(this);
        }
    }

    public void addEnrollment(Enrollment enrollment)
    {
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }

    public void removeEnrollment(Enrollment enrollment)
    {
        enrollments.remove(enrollment);
        enrollment.setStudent(null);
    }

    public void addClub(Club club)
    {
        clubs.add(club);
        club.getStudents().add(this);
    }

    public void removeClub(Club club)
    {
        clubs.remove(club);
        club.getStudents().remove(this);
    }
}
