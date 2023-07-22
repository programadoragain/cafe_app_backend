package com.ferdev.cafe.Repositories;

import com.ferdev.cafe.Entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> getAllBills();
    List<Bill> getAllBillsByUsername(@Param("username") String username);
}
