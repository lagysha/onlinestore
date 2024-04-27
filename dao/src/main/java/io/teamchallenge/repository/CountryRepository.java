package io.teamchallenge.repository;

import io.teamchallenge.entity.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName (String name);
}
