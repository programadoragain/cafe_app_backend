package com.ferdev.cafe.Controllers;

import com.ferdev.cafe.Constants.CafeConstanst;
import com.ferdev.cafe.Entities.Bill;
import com.ferdev.cafe.Entities.Category;
import com.ferdev.cafe.Service.BillService;
import com.ferdev.cafe.Util.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillRestControllerImpl implements BillRestController{

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<String>(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            return billService.getBills();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try{
            return billService.getPdf(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            return billService.deleteBill(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
