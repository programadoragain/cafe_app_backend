package com.ferdev.cafe.Service;

import com.ferdev.cafe.Wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<List<ProductWrapper>> getAllProduct ();
    ResponseEntity<ProductWrapper> getProductById(Integer id);
    ResponseEntity<List<ProductWrapper>> getAllProductByCategory (Integer id);
    ResponseEntity<String> addNewProduct(Map<String,String> requestMap);
    ResponseEntity<String> updateProduct(Map<String,String> requestMap);
    ResponseEntity<String> updateStatus(Map<String,String> requestMap);
    ResponseEntity<String> deleteProduct(Integer id);
}
