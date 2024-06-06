package io.teamchallenge.repository;

import io.teamchallenge.entity.Country;
import io.teamchallenge.entity.cartitem.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Country} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Denys Liubchenko
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