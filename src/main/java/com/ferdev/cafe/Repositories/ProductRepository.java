package com.ferdev.cafe.Repositories;

import com.ferdev.cafe.Entities.Product;
import com.ferdev.cafe.Wrapper.ProductWrapper;
import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p")
    List<ProductWrapper> getAllProduct();

    @Query("select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p where p.category.id= :id and p.status=1")
    List<ProductWrapper> getAllProductByCategory(@Param("id") Integer id);

    @Query("select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p where p.id=:id")
    ProductWrapper getProductById(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Product p set p.status=:status where p.id=:id")
    Integer updateStatusProduct(@Param("status") Boolean status, @Param("id") Integer id);
}
