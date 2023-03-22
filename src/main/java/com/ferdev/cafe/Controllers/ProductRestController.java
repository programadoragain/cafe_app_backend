package com.ferdev.cafe.Controllers;

import com.ferdev.cafe.Wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductRestController {

    @GetMapping("/get")
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    @GetMapping("/getProductById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);

    @GetMapping("/getAllByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getAllProductByCategory(@PathVariable Integer id);

    @PostMapping("/add")
    ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String,String> request);

    @PostMapping("/update")
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String,String> request);

    @PostMapping("/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PostMapping("/updateStatus")
    ResponseEntity<String> addStatus(@RequestBody(required = true) Map<String,String> request);
}
