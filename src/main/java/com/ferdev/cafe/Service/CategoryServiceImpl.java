package com.ferdev.cafe.Service;

import com.ferdev.cafe.Constants.CafeConstanst;
import com.ferdev.cafe.Entities.Category;
import com.ferdev.cafe.Jwt.JwtFilter;
import com.ferdev.cafe.Repositories.CategoryRepository;
import com.ferdev.cafe.Util.CafeUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, false)) {
                    categoryRepository.save(getCategoryFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Category added successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstanst.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else
                return CafeUtils.getResponseEntity(CafeConstanst.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, true)) {
                    Optional<Category> category= categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
                    if (!category.isEmpty()) {
                        categoryRepository.save(getCategoryFromMap(requestMap,true));
                        return CafeUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
                    } else
                        return CafeUtils.getResponseEntity("Category Id is not valid.", HttpStatus.OK);
                } else
                    return CafeUtils.getResponseEntity(CafeConstanst.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else
                return CafeUtils.getResponseEntity(CafeConstanst.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if(!filterValue.isEmpty() && filterValue.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String,String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else
                if (!validateId) return true;
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd) {
        Category category= new Category();
        if (isAdd) {
            category.setId(Integer.parseInt((requestMap.get("id"))));
        }
        category.setName(requestMap.get("name"));
        return category;
    }


}
