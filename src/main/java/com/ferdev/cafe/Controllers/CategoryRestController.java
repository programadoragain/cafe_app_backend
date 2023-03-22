package com.ferdev.cafe.Controllers;

import com.ferdev.cafe.Entities.Category;
import com.ferdev.cafe.Repositories.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/category")
public interface CategoryRestController {

    @GetMapping("/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestBody(required = false) String filterValue);

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> request);

    @PostMapping("/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> request);
}
