package com.jumia.brunocardoso.repository;

import com.jumia.brunocardoso.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("select max(C.id) from Customer C")
    public Integer findMaxId();

    Page<Customer> findAll(Pageable pageable);

    boolean existsByPhone(String phone);

    Customer findByPhone(String phone);

    Page<Customer> findByActive(Pageable pageable, boolean active);

    Page<Customer> findByNameContaining(Pageable pageable, String name);

    Page<Customer> findByNameContainingAndActive(Pageable pageable, String name, boolean active);


    //Always avoid SQL injection, but pass only this time.
    //I've tried...sqlite does not have the REGEXP function installed by default
    @Query(value = "select * from customer where phone REGEXP ?1", nativeQuery = true)
    Page<Customer> findByPhoneRegex(Pageable pageable, String regex);
    Page<Customer> findByPhoneRegexAndActive(Pageable pageable, String regex, boolean active);


    Page<Customer> findByPhoneStartsWith(Pageable pageable, String phone);

    Page<Customer> findByPhoneStartsWithAndActive(Pageable pageable, String phone, boolean active);

}
