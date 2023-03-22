package com.ferdev.cafe.Repositories;

import com.ferdev.cafe.Entities.Product;
import com.ferdev.cafe.Wrapper.ProductWrapper;
import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<ProductWrapper> getAllProduct();
    ProductWrapper getProductById(@Param("id") Integer id);
    List<ProductWrapper> getAllProductByCategory(@Param("id") Integer id);

    @Modifying
    @Transactional
    Integer updateStatusProduct(@Param("status") Boolean status, @Param("id") Integer id);
}
