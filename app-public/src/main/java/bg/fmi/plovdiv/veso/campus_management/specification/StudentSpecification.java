package bg.fmi.plovdiv.veso.campus_management.specification;

import bg.fmi.plovdiv.veso.campus_management.dto.StudentFilterDTO;
import bg.fmi.plovdiv.veso.campus_management.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification for dynamic student search.
 * Implements dynamic query building based on StudentFilterDTO.
 * Demonstrates searching across associated entities (Address, Club, Course, Enrollment).
 */
public class StudentSpecification
{
    /**
     * Creates a Specification based on the provided filter criteria.
     * Only non-null filter values are included in the query.
     *
     * @param filter StudentFilterDTO containing search criteria
     * @return Specification for Student entity
     */
    public static Specification<Student> filterStudents(StudentFilterDTO filter)
    {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStudentName() != null && !filter.getStudentName().isEmpty()) {
                String namePattern = "%" + filter.getStudentName().toLowerCase() + "%";
                Predicate firstNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        namePattern
                );
                Predicate lastNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        namePattern
                );
                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            if (filter.getCity() != null && !filter.getCity().isEmpty()) {
                Join<Student, Address> addressJoin = root.join("address", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(addressJoin.get("city")),
                        filter.getCity().toLowerCase()
                ));
            }

            if (filter.getClubName() != null && !filter.getClubName().isEmpty()) {
                Join<Student, Club> clubJoin = root.join("clubs", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(clubJoin.get("name")),
                        "%" + filter.getClubName().toLowerCase() + "%"
                ));
            }

            if (filter.getCourseName() != null && !filter.getCourseName().isEmpty()) {
                Join<Student, Enrollment> enrollmentJoin = root.join("enrollments", JoinType.LEFT);
                Join<Enrollment, Course> courseJoin = enrollmentJoin.join("course", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(courseJoin.get("courseName")),
                        "%" + filter.getCourseName().toLowerCase() + "%"
                ));
            }

            if (filter.getEnrollmentYear() != null) {
                Join<Student, Enrollment> enrollmentJoin = root.join("enrollments", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(
                        enrollmentJoin.get("year"),
                        filter.getEnrollmentYear()
                ));
            }

            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"
                ));
            }

            if (filter.getStudentNumber() != null && !filter.getStudentNumber().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("studentNumber"),
                        filter.getStudentNumber()
                ));
            }

            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
