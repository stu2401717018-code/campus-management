package bg.fmi.plovdiv.veso.campus_management.repository;

import bg.fmi.plovdiv.veso.campus_management.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Address entity.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>
{
    /**
     * Find all addresses in a specific city
     */
    List<Address> findByCity(String city);

    /**
     * Find all addresses in a specific state
     */
    List<Address> findByState(String state);

    /**
     * Find all addresses in a specific country
     */
    List<Address> findByCountry(String country);
}
