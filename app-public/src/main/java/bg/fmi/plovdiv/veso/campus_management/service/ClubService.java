package bg.fmi.plovdiv.veso.campus_management.service;

import bg.fmi.plovdiv.veso.campus_management.dto.ClubDTO;
import bg.fmi.plovdiv.veso.campus_management.exception.ResourceNotFoundException;
import bg.fmi.plovdiv.veso.campus_management.model.Club;
import bg.fmi.plovdiv.veso.campus_management.model.Student;
import bg.fmi.plovdiv.veso.campus_management.repository.ClubRepository;
import bg.fmi.plovdiv.veso.campus_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Club business logic.
 */
@Service
@Transactional
public class ClubService
{
    private final ClubRepository clubRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository, StudentRepository studentRepository)
    {
        this.clubRepository = clubRepository;
        this.studentRepository = studentRepository;
    }

    public List<ClubDTO> getAllClubs()
    {
        return clubRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClubDTO getClubById(Long id)
    {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Club", "id", id));

        return convertToDTO(club);
    }

    public ClubDTO createClub(ClubDTO clubDTO)
    {
        if (clubRepository.existsByName(clubDTO.getName())) {
            throw new IllegalArgumentException("Club name already exists: " + clubDTO.getName());
        }

        Club club = convertToEntity(clubDTO);
        Club savedClub = clubRepository.save(club);

        return convertToDTO(savedClub);
    }

    public ClubDTO updateClub(Long id, ClubDTO clubDTO)
    {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Club", "id", id));

        if (!club.getName().equals(clubDTO.getName()) &&
                clubRepository.existsByName(clubDTO.getName())) {
            throw new IllegalArgumentException("Club name already exists: " + clubDTO.getName());
        }

        club.setName(clubDTO.getName());
        club.setDescription(clubDTO.getDescription());
        club.setCategory(clubDTO.getCategory());
        club.setPresidentEmail(clubDTO.getPresidentEmail());
        club.setMemberLimit(clubDTO.getMemberLimit());

        Club updatedClub = clubRepository.save(club);

        return convertToDTO(updatedClub);
    }

    public void deleteClub(Long id)
    {
        if (!clubRepository.existsById(id)) {
            throw new ResourceNotFoundException("Club", "id", id);
        }
        clubRepository.deleteById(id);
    }

    /**
     * Add a student to a club
     */
    public ClubDTO addStudentToClub(Long clubId, Long studentId)
    {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club", "id", clubId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        if (club.isAtCapacity()) {
            throw new IllegalArgumentException("Club has reached maximum capacity");
        }

        if (club.getStudents().contains(student)) {
            throw new IllegalArgumentException("Student is already a member of this club");
        }

        club.addStudent(student);
        Club updatedClub = clubRepository.save(club);

        return convertToDTO(updatedClub);
    }

    /**
     * Remove a student from a club
     */
    public ClubDTO removeStudentFromClub(Long clubId, Long studentId)
    {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club", "id", clubId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        if (!club.getStudents().contains(student)) {
            throw new IllegalArgumentException("Student is not a member of this club");
        }

        club.removeStudent(student);
        Club updatedClub = clubRepository.save(club);

        return convertToDTO(updatedClub);
    }

    /**
     * Get all clubs that a student is a member of
     */
    public List<ClubDTO> getClubsByStudent(Long studentId)
    {
        return clubRepository.findClubsByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClubDTO convertToDTO(Club club)
    {
        ClubDTO dto = new ClubDTO();
        dto.setId(club.getId());
        dto.setName(club.getName());
        dto.setDescription(club.getDescription());
        dto.setCategory(club.getCategory());
        dto.setPresidentEmail(club.getPresidentEmail());
        dto.setMemberLimit(club.getMemberLimit());
        dto.setCurrentMemberCount(club.getCurrentMemberCount());

        return dto;
    }

    private Club convertToEntity(ClubDTO dto)
    {
        Club club = new Club();
        club.setName(dto.getName());
        club.setDescription(dto.getDescription());
        club.setCategory(dto.getCategory());
        club.setPresidentEmail(dto.getPresidentEmail());
        club.setMemberLimit(dto.getMemberLimit());

        return club;
    }
}
