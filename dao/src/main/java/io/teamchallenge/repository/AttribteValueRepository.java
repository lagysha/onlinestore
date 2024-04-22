package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttribteValueRepository extends JpaRepository<AttributeValue,Long> {
}
