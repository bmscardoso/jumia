package com.jumia.brunocardoso.controller;

import com.jumia.brunocardoso.configuration.ConfigProperties;
import com.jumia.brunocardoso.entity.Customer;
import com.jumia.brunocardoso.repository.CustomerRepository;
import com.jumia.brunocardoso.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @SuppressWarnings("squid:S4684")
 * Objective: Ignore SonarQube's suggestion to use a DTO for the Customer object.
 * Reason: There is no such need.
 *
 * @SuppressWarnings("squid:S4529")
 * Objective: Ignore SonarQube's suggestion to make sure that exposing this HTTP endpoint is safe here.
 * Reason: There is no need to use any type of authentication in this application.
 */
@SuppressWarnings({"squid:S4684", "squid:S4529"})
@RequestMapping(value = "customer")
@RestController
public class CustomerController {
    
    private static final String NOT_ACCEPTABLE_MESSAGE = "Request not acceptable. Check logs for details";

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
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(NOT_ACCEPTABLE_MESSAGE);
    }

    @PutMapping(value = "update")
    public ResponseEntity updateCustomer(@RequestBody Customer customer){
        Customer updatedCustomer = customerService.updateCustomer(customerRepository, customer);
        if(updatedCustomer != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCustomer);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(NOT_ACCEPTABLE_MESSAGE);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity deleteCustomer(@RequestBody Customer customer){
        if(customerService.deleteCustomer(customerRepository, customer)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(NOT_ACCEPTABLE_MESSAGE);
    }

    @GetMapping(value = "all")
    public ResponseEntity listAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result = customerService.readCustomersByName(customerRepository, page, size, null, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "active")
    public ResponseEntity listActiveCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomersByName(customerRepository, page, size, null, true);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "inactive")
    public ResponseEntity listInactiveCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomersByName(customerRepository, page, size, null, false);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "byname")
    public ResponseEntity listCustomersByName(@RequestParam String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result =  customerService.readCustomersByName(customerRepository, page, size, name, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "bynameandstatus")
    public ResponseEntity listCustomersByNameAndStatus(@RequestParam String name, @RequestParam Boolean active, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result = customerService.readCustomersByName(customerRepository, page, size, name, active);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "byphone")
    public ResponseEntity listCustomersByPhone(@RequestParam String phone){
        Customer customer =  customerRepository.findByPhone(phone);
        return ResponseEntity.ok(customer);
    }

    @GetMapping(value = "bycountry")
    public ResponseEntity listCustomersByCountry(@RequestParam String country, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result = customerService.readCustomersByCountry(configProperties, customerRepository, page, size, country, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "bycountryandstatus")
    public ResponseEntity listCustomersByCountryAndStatus(@RequestParam String country, @RequestParam Boolean active, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Map<String, Object> result = customerService.readCustomersByCountry(configProperties, customerRepository, page, size, country, active);
        return ResponseEntity.ok(result);
    }

}
