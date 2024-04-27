package io.teamchallenge.repository;

import io.teamchallenge.entity.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Country entities.
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
    /**
     * Retrieves an {@link Optional} of {@link Country} by country name.
     *
     * @param name the name of the country.
     * @return an Optional containing the Country object, if found.
     */
    Optional<Country> findByName(String name);
}