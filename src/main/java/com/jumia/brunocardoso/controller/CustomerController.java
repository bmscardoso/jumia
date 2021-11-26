package com.jumia.brunocardoso.controller;

import com.jumia.brunocardoso.configuration.ConfigProperties;
import com.jumia.brunocardoso.entity.Customer;
import com.jumia.brunocardoso.repository.CustomerRepository;
import com.jumia.brunocardoso.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(value = "customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ConfigProperties configProperties;

    @GetMapping(value = "info")
    public String info(){
        return "The aplication is up....";
    }

    @PostMapping(value = "create")
    public ResponseEntity createCustomer(@RequestBody Customer customer){
        Customer createdCustomer = customerService.createCustomer(configProperties, customerRepository, customer);
        if(createdCustomer != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Request not acceptable. Check logs for details");
    }

    @PutMapping(value = "update")
    public ResponseEntity updateCustomer(@RequestBody Customer customer){
        Customer updatedCustomer = customerService.updateCustomer(customerRepository, customer);
        if(updatedCustomer != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCustomer);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Request not acceptable. Check logs for details");
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity deleteCustomer(@RequestBody Customer customer){
        if(customerService.deleteCustomer(customerRepository, customer)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Request not acceptable. Check logs for details");
    }

    @GetMapping(value = "all")
    public ResponseEntity listAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result = customerService.readCustomers(customerRepository, page, size, null, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "active")
    public ResponseEntity listActiveCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomers(customerRepository, page, size, null, true);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "inactive")
    public ResponseEntity listInactiveCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomers(customerRepository, page, size, null, false);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "byname")
    public ResponseEntity listCustomersByName(@RequestParam String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomers(customerRepository, page, size, name, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "bynameandstatus")
    public ResponseEntity listCustomersByNameAndStatus(@RequestParam String name, @RequestParam Boolean active, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomers(customerRepository, page, size, name, active);
        return ResponseEntity.ok(result);
    }

}
