package io.teamchallenge.repository;


import io.teamchallenge.entity.attributes.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute,Long> {
}
