package io.teamchallenge.repository;

import io.teamchallenge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepostiory extends JpaRepository<Category,Long> {
}
