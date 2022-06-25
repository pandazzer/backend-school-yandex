package com.example.backendschoolyandex.Repository;

import com.example.backendschoolyandex.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemsRepo extends JpaRepository<Product, Long> {
    Product findByid(String id);
    Long deleteByid(String id);
    List<Product> findByparentId(String parentid);
}
