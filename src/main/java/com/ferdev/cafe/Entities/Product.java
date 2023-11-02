package com.ferdev.cafe.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;

/* Queries changed to ProductRepository location.
@NamedQuery(name = "Product.getAllProduct", query = "select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p")
@NamedQuery(name = "Product.getProductById", query = "select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p where p.id=:id")
@NamedQuery(name = "Product.getAllProductByCategory", query = "select new com.ferdev.cafe.Wrapper.ProductWrapper(p.id, p.name, p.category.id, p.category.name, p.description, p.price, p.status) from Product p where p.category.id=:id and p.status='true'")
@NamedQuery(name = "Product.updateStatusProduct", query = "update Product p set p.status=:status where p.id=:id")
*/

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private Boolean status;
}
