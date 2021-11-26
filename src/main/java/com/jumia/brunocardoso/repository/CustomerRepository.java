package com.jumia.brunocardoso.repository;

import com.jumia.brunocardoso.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("select max(C.id) from Customer C")
    public Integer findMaxId();

    Page<Customer> findAll(Pageable pageable);

    boolean existsByPhone(String phone);

    Customer findByPhone(String phone);

    Page<Customer> findByActive(Pageable pageable, boolean active);

    @Query("SELECT C FROM Customer C WHERE LOWER(C.name) LIKE LOWER(concat(?1, '%'))")
    Page<Customer> findByName(Pageable pageable, String name);

    @Query("SELECT C FROM Customer C WHERE LOWER(C.name) LIKE LOWER(concat(?1, '%')) AND C.active = ?2")
    Page<Customer> findByNameAndActive(Pageable pageable, String name, boolean active);

}
