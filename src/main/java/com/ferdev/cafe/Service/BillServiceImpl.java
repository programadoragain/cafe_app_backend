package com.ferdev.cafe.Service;

import com.ferdev.cafe.Constants.CafeConstanst;
import com.ferdev.cafe.Entities.Bill;
import com.ferdev.cafe.Jwt.JwtFilter;
import com.ferdev.cafe.Repositories.BillRepository;
import com.ferdev.cafe.Util.CafeUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport ");
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerated"))
                    fileName= (String) requestMap.get("uuid");
                else {
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }
                String data= "Name: " + requestMap.containsKey("name") + "\n"
                            + "Contact Number: " + requestMap.containsKey("contactNumber") + "\n"
                            + "Email: " + requestMap.containsKey("email") + "\n"
                            + "Payment Method: " + requestMap.containsKey("paymentMethod") + "\n";

                Document document= new Document();
                PdfWriter.getInstance(document, new FileOutputStream(C))


                return CafeUtils.getResponseEntity(".", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Required data not found.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill= new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("total")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepository.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
                && requestMap.containsKey("productDetails") && requestMap.containsKey("total"));
    }


}
