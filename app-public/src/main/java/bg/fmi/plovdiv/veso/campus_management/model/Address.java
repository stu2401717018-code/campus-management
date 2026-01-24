package bg.fmi.plovdiv.veso.campus_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Address entity representing a student's address.
 * Relationship: One-to-One with Student
 */
@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "student")
@ToString(exclude = "student")
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String street;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country;

    /**
     * One-to-One relationship with Student (owning side is Student)
     * JsonIgnore prevents circular reference in JSON serialization
     */
    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Student student;
}
