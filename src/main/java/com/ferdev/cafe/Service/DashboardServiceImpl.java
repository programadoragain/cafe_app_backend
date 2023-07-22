package com.ferdev.cafe.Service;

import com.ferdev.cafe.Repositories.BillRepository;
import com.ferdev.cafe.Repositories.CategoryRepository;
import com.ferdev.cafe.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BillRepository billRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map= new HashMap<>();
        map.put("category", categoryRepository.count());
        map.put("product", productRepository.count());
        map.put("bill", billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
