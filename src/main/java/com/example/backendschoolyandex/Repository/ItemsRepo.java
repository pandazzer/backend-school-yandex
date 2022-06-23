package com.example.backendschoolyandex.Repository;

import com.example.backendschoolyandex.Entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsRepo extends CrudRepository<Product, Long> {
    Product findByid(String id);
    Long deleteByid(String id);
    List<Product> findByparentId(String parentid);
}
