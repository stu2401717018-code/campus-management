package bg.fmi.plovdiv.veso.campus_management.config;

import bg.fmi.plovdiv.veso.campus_management.model.*;
import bg.fmi.plovdiv.veso.campus_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Database seeder component for initializing sample data.
 * Runs automatically on application startup (except in test profile).
 * Seeds departments, professors, courses, students, enrollments, and clubs.
 */
@Component
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner
{
    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AddressRepository addressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClubRepository clubRepository;

    @Autowired
    public DatabaseSeeder(
            DepartmentRepository departmentRepository,
            ProfessorRepository professorRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            AddressRepository addressRepository,
            EnrollmentRepository enrollmentRepository,
            ClubRepository clubRepository)
    {
        this.departmentRepository = departmentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.addressRepository = addressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception
    {
        if (departmentRepository.count() > 0) {

            return;
        }

        Department csDept = new Department();
        csDept.setName("Computer Science");
        csDept.setCode("CS");
        csDept.setDescription("Department of Computer Science and Software Engineering");
        csDept.setBuilding("Building A, Floor 3");
        csDept = departmentRepository.save(csDept);

        Department mathDept = new Department();
        mathDept.setName("Mathematics");
        mathDept.setCode("MATH");
        mathDept.setDescription("Department of Mathematics and Statistics");
        mathDept.setBuilding("Building B, Floor 2");
        mathDept = departmentRepository.save(mathDept);

        Department physicsDept = new Department();
        physicsDept.setName("Physics");
        physicsDept.setCode("PHYS");
        physicsDept.setDescription("Department of Physics and Astronomy");
        physicsDept.setBuilding("Building C, Floor 1");
        physicsDept = departmentRepository.save(physicsDept);

        Professor prof1 = new Professor();
        prof1.setFirstName("Ivan");
        prof1.setLastName("Petrov");
        prof1.setEmail("ivan.petrov@fmi.plovdiv.bg");
        prof1.setPhoneNumber("+359 32 123 456");
        prof1.setSpecialization("Software Engineering");
        prof1.setTitle("Associate Professor");
        prof1.setDepartment(csDept);
        prof1 = professorRepository.save(prof1);

        Professor prof2 = new Professor();
        prof2.setFirstName("Maria");
        prof2.setLastName("Georgieva");
        prof2.setEmail("maria.georgieva@fmi.plovdiv.bg");
        prof2.setPhoneNumber("+359 32 234 567");
        prof2.setSpecialization("Database Systems");
        prof2.setTitle("Professor");
        prof2.setDepartment(csDept);
        prof2 = professorRepository.save(prof2);

        Professor prof3 = new Professor();
        prof3.setFirstName("Georgi");
        prof3.setLastName("Ivanov");
        prof3.setEmail("georgi.ivanov@fmi.plovdiv.bg");
        prof3.setPhoneNumber("+359 32 345 678");
        prof3.setSpecialization("Linear Algebra");
        prof3.setTitle("Assistant Professor");
        prof3.setDepartment(mathDept);
        prof3 = professorRepository.save(prof3);

        Course course1 = new Course();
        course1.setCourseCode("CS101");
        course1.setCourseName("Introduction to Programming");
        course1.setDescription("Fundamentals of programming using Java");
        course1.setCredits(6);
        course1.setDepartment(csDept);
        course1 = courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("CS201");
        course2.setCourseName("Data Structures and Algorithms");
        course2.setDescription("Advanced data structures and algorithm analysis");
        course2.setCredits(6);
        course2.setDepartment(csDept);
        course2 = courseRepository.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("CS301");
        course3.setCourseName("Database Systems");
        course3.setDescription("Relational database design and SQL");
        course3.setCredits(5);
        course3.setDepartment(csDept);
        course3 = courseRepository.save(course3);

        Course course4 = new Course();
        course4.setCourseCode("MATH101");
        course4.setCourseName("Calculus I");
        course4.setDescription("Differential and integral calculus");
        course4.setCredits(6);
        course4.setDepartment(mathDept);
        course4 = courseRepository.save(course4);

        Course course5 = new Course();
        course5.setCourseCode("MATH201");
        course5.setCourseName("Linear Algebra");
        course5.setDescription("Vector spaces, matrices, and linear transformations");
        course5.setCredits(5);
        course5.setDepartment(mathDept);
        course5 = courseRepository.save(course5);

        Address address1 = new Address();
        address1.setStreet("123 Main Street");
        address1.setCity("Plovdiv");
        address1.setState("Plovdiv");
        address1.setPostalCode("4000");
        address1.setCountry("Bulgaria");
        address1 = addressRepository.save(address1);

        Address address2 = new Address();
        address2.setStreet("456 University Avenue");
        address2.setCity("Plovdiv");
        address2.setState("Plovdiv");
        address2.setPostalCode("4000");
        address2.setCountry("Bulgaria");
        address2 = addressRepository.save(address2);

        Address address3 = new Address();
        address3.setStreet("789 Student Boulevard");
        address3.setCity("Sofia");
        address3.setState("Sofia");
        address3.setPostalCode("1000");
        address3.setCountry("Bulgaria");
        address3 = addressRepository.save(address3);

        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@student.fmi.plovdiv.bg");
        student1.setStudentNumber("STU001");
        student1.setDateOfBirth(LocalDate.of(2002, 5, 15));
        student1.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        student1.setAddress(address1);
        student1 = studentRepository.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@student.fmi.plovdiv.bg");
        student2.setStudentNumber("STU002");
        student2.setDateOfBirth(LocalDate.of(2003, 8, 20));
        student2.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        student2.setAddress(address2);
        student2 = studentRepository.save(student2);

        Student student3 = new Student();
        student3.setFirstName("Petar");
        student3.setLastName("Ivanov");
        student3.setEmail("petar.ivanov@student.fmi.plovdiv.bg");
        student3.setStudentNumber("STU003");
        student3.setDateOfBirth(LocalDate.of(2001, 12, 10));
        student3.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        student3.setAddress(address3);
        student3 = studentRepository.save(student3);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        enrollment1.setCourse(course1);
        enrollment1.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        enrollment1.setSemester("Fall 2023");
        enrollment1.setYear(2023);
        enrollment1.setStatus("Active");
        enrollment1.setGrade(85.5);
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student1);
        enrollment2.setCourse(course2);
        enrollment2.setEnrollmentDate(LocalDate.of(2024, 2, 1));
        enrollment2.setSemester("Spring 2024");
        enrollment2.setYear(2024);
        enrollment2.setStatus("Active");
        enrollmentRepository.save(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student2);
        enrollment3.setCourse(course1);
        enrollment3.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        enrollment3.setSemester("Fall 2023");
        enrollment3.setYear(2023);
        enrollment3.setStatus("Completed");
        enrollment3.setGrade(92.0);
        enrollmentRepository.save(enrollment3);

        Enrollment enrollment4 = new Enrollment();
        enrollment4.setStudent(student2);
        enrollment4.setCourse(course3);
        enrollment4.setEnrollmentDate(LocalDate.of(2024, 2, 1));
        enrollment4.setSemester("Spring 2024");
        enrollment4.setYear(2024);
        enrollment4.setStatus("Active");
        enrollmentRepository.save(enrollment4);

        Enrollment enrollment5 = new Enrollment();
        enrollment5.setStudent(student3);
        enrollment5.setCourse(course4);
        enrollment5.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        enrollment5.setSemester("Fall 2022");
        enrollment5.setYear(2022);
        enrollment5.setStatus("Completed");
        enrollment5.setGrade(88.0);
        enrollmentRepository.save(enrollment5);

        Enrollment enrollment6 = new Enrollment();
        enrollment6.setStudent(student3);
        enrollment6.setCourse(course5);
        enrollment6.setEnrollmentDate(LocalDate.of(2023, 2, 1));
        enrollment6.setSemester("Spring 2023");
        enrollment6.setYear(2023);
        enrollment6.setStatus("Completed");
        enrollment6.setGrade(90.5);
        enrollmentRepository.save(enrollment6);

        Club club1 = new Club();
        club1.setName("Programming Club");
        club1.setDescription("A club for students interested in programming and software development");
        club1.setCategory("Academic");
        club1.setPresidentEmail("president@programmingclub.fmi.plovdiv.bg");
        club1.setMemberLimit(50);
        club1 = clubRepository.save(club1);

        Club club2 = new Club();
        club2.setName("Mathematics Society");
        club2.setDescription("For students passionate about mathematics and problem solving");
        club2.setCategory("Academic");
        club2.setPresidentEmail("president@mathsociety.fmi.plovdiv.bg");
        club2.setMemberLimit(30);
        club2 = clubRepository.save(club2);

        Club club3 = new Club();
        club3.setName("Sports Club");
        club3.setDescription("Promoting physical activity and team sports");
        club3.setCategory("Recreational");
        club3.setPresidentEmail("president@sportsclub.fmi.plovdiv.bg");
        club3.setMemberLimit(100);
        club3 = clubRepository.save(club3);

        student1.addClub(club1);
        student1.addClub(club3);
        studentRepository.save(student1);

        student2.addClub(club1);
        studentRepository.save(student2);

        student3.addClub(club2);
        student3.addClub(club3);
        studentRepository.save(student3);
    }
}
