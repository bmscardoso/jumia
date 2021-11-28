package com.jumia.brunocardoso.service;

import com.jumia.brunocardoso.configuration.ConfigProperties;
import com.jumia.brunocardoso.entity.Customer;
import com.jumia.brunocardoso.repository.CustomerRepository;
import com.jumia.brunocardoso.utilities.CustomerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Transactional
    public Customer createCustomer(ConfigProperties configProperties, CustomerRepository customerRepository, Customer customer){
        if (!customerRepository.existsByPhone(customer.getPhone())){

            String country = CustomerUtils.checkCountryByNumber(configProperties.getCountrycodes(), customer.getPhone());
            if(country == null){
                LOGGER.error("{} is an invalid number", customer.getPhone());
                return null;
            }

            LOGGER.info("{} identified has a {} number.", customer.getPhone(), country);

            customer.setId(null == customerRepository.findMaxId()? 0 : customerRepository.findMaxId() + 1);
            Customer createdCustomer = customerRepository.save(customer);

            LOGGER.info("Customer record created successfully.");

            return createdCustomer;
        }else {
            LOGGER.error("Phone number already exists");
            return null;
        }
    }

    public Map<String, Object> readCustomersByName(CustomerRepository customerRepository, int page, int size, String name, Boolean isActive){

        Pageable paging = PageRequest.of(page, size);

        Page<Customer> pageCustomers;

        if(name == null){

            if(isActive == null){
                pageCustomers = customerRepository.findAll(paging);
            }else{
                pageCustomers = customerRepository.findByActive(paging, isActive);
            }

        }else{

            if(isActive == null){
                pageCustomers = customerRepository.findByNameContaining(paging, name);
            }else{
                pageCustomers = customerRepository.findByNameContainingAndActive(paging, name, isActive);
            }

        }

        return CustomerUtils.mountResponse(pageCustomers);
    }

    public Map<String, Object> readCustomersByCountry(ConfigProperties configProperties, CustomerRepository customerRepository, int page, int size, String country, Boolean isActive){

        String countryCode = CustomerUtils.checkNumberByCountry(configProperties.getCountrycodes(), country);

        Pageable paging = PageRequest.of(page, size);

        Page<Customer> pageCustomers;

        if(isActive == null){
            pageCustomers = customerRepository.findByPhoneStartsWith(paging, countryCode);
        }else{
            pageCustomers = customerRepository.findByPhoneStartsWithAndActive(paging, countryCode, isActive);
        }

        return CustomerUtils.mountResponse(pageCustomers);
    }

    @Transactional
    public Customer updateCustomer(CustomerRepository customerRepository, Customer customer){

        if (customerRepository.existsByPhone(customer.getPhone())){

            Customer customerToBeUpdate = customerRepository.findByPhone(customer.getPhone());
            customerToBeUpdate.setName(customer.getName());
            customerToBeUpdate.setPhone(customer.getPhone());
            customerToBeUpdate.setActive(customer.isActive());
            Customer updatedCustomer = customerRepository.save(customerToBeUpdate);

            LOGGER.info("Customer record updated.");

            return updatedCustomer;

        }else {
            LOGGER.error("Customer does not exists.");
            return null;
        }
    }

    @Transactional
    public boolean deleteCustomer(CustomerRepository customerRepository, Customer customer){
        if (customerRepository.existsByPhone(customer.getPhone())){
            Customer customerToDelete = customerRepository.findByPhone(customer.getPhone());
            customerRepository.delete(customerToDelete);

            LOGGER.info("Customer record deleted successfully.");
            return true;

        }else {
            LOGGER.error("Customer does not exist.");
            return false;
        }
    }


}
