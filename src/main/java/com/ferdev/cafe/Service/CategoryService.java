package com.ferdev.cafe.Service;

import com.ferdev.cafe.Entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<List<Category>> getAllCategory (String filterValue);
    ResponseEntity<String> addNewCategory(Map<String,String> requestMap);
    ResponseEntity<String> updateCategory(Map<String,String> requestMap);
}
