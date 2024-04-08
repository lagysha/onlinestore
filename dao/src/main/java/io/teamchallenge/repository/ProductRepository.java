package io.teamchallenge.repository;

import io.teamchallenge.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from Product p join fetch p.category where (:name is NULL or p.name like %:name%)")
    Page<Product> findAllBy(Pageable pageable,String name);
}
