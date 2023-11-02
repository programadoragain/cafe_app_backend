package com.ferdev.cafe.Service;

import com.ferdev.cafe.Constants.CafeConstanst;
import com.ferdev.cafe.Entities.Category;
import com.ferdev.cafe.Entities.Product;
import com.ferdev.cafe.Jwt.JwtFilter;
import com.ferdev.cafe.Repositories.ProductRepository;
import com.ferdev.cafe.Util.CafeUtils;
import com.ferdev.cafe.Wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productRepository.save(getProductFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Product added successfully.", HttpStatus.OK);
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
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<List<ProductWrapper>>(productRepository.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProductByCategory(Integer id) {
        try {
            return new ResponseEntity<List<ProductWrapper>>(productRepository.getAllProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<ProductWrapper>(productRepository.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<ProductWrapper>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> product= productRepository.findById(Integer.parseInt(requestMap.get("id")));
                    if (!product.isEmpty()) {
                        Product productTemp= getProductFromMap(requestMap, true);
                        productTemp.setStatus(product.get().getStatus());
                        productRepository.save(productTemp);
                        return CafeUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
                    } else
                        return CafeUtils.getResponseEntity("Product Id not exist.", HttpStatus.OK);
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
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> product= productRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (!product.isEmpty()) {
                    productRepository.updateStatusProduct(Boolean.parseBoolean(requestMap.get("status")), Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("Product status updated successfully.", HttpStatus.OK);
                } else
                    return CafeUtils.getResponseEntity("Product Id not exist.", HttpStatus.OK);
            } else
                return CafeUtils.getResponseEntity(CafeConstanst.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> product= productRepository.findById(id);
                if (!product.isEmpty()) {
                    productRepository.deleteById(id);
                    return CafeUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
                } else
                    return CafeUtils.getResponseEntity("Product Id not exist.", HttpStatus.OK);
            } else
                return CafeUtils.getResponseEntity(CafeConstanst.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Product product= new Product();

        Category category= new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        if (isAdd) {
            product.setId(Integer.parseInt((requestMap.get("id"))));
        } else
            { product.setStatus(true); }

        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name") && requestMap.containsKey("price")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else
                if (!validateId) return true;
        }
        return false;
    }

    /*
    private ProductWrapper convertProductToProductWrapper(Product p) {
        ProductWrapper pw= new ProductWrapper();
        if (p != null) {
            pw.setId(p.getId());
            pw.setCategoryId(p.getCategory().getId());
            pw.setCategoryName(p.getCategory().getName());
            pw.setName(p.getName());
            pw.setDescription(p.getDescription());
            pw.setPrice(p.getPrice());
            pw.setStatus(p.getStatus());
        }
        return pw;
    }
    */
}
