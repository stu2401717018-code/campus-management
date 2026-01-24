package bg.fmi.plovdiv.veso.campus_management.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Professor entity representing a faculty member.
 * Relationship: Many-to-One with Department
 */
@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "department")
@ToString(exclude = "department")
public class Professor
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

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String specialization;

    @Column(length = 50)
    private String title;

    /**
     * Many-to-One relationship with Department
     * Multiple professors can belong to one department
     */
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
