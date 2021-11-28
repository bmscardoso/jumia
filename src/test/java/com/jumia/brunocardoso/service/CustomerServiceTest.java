package com.jumia.brunocardoso.service;

import com.jumia.brunocardoso.configuration.ConfigProperties;
import com.jumia.brunocardoso.entity.Customer;
import com.jumia.brunocardoso.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceTest {

    @Autowired
    CustomerRepository customerRepository;

    CustomerService customerService;

    ConfigProperties configProperties;

    //Dummy customers
    Customer marcoSilva;
    Customer marcoBarbosa;
    Customer martaBarbosa;
    Customer joaquimMarques;

    //Actual record from our database
    Customer walidHammadi;

    @BeforeAll
    void setUp() {
        customerService = new CustomerService();

        marcoSilva =        new Customer("Marco Silva","(237) 677046617", true);
        marcoBarbosa =      new Customer("marco barbosa", "(212) 698054318", true);
        martaBarbosa =      new Customer("Marta barbosa", "(258) 847651505", false);
        joaquimMarques =    new Customer("Joaquim Marques", "(351) 912345679", false);
        walidHammadi =      new Customer("Walid Hammadi", "(212) 6007989253", false);

        Map<String, String> countrycodes;
        countrycodes = new HashMap<>();
        countrycodes.put("Cameroon","\\(237\\)\\ ?[2368]\\d{7,8}$");
        countrycodes.put("Ethiopia","\\(251\\)\\ ?[1-59]\\d{8}$");
        countrycodes.put("Morocco","\\(212\\)\\ ?[5-9]\\d{8}$");
        countrycodes.put("Mozambique","\\(258\\)\\ ?[28]\\d{7,8}$");
        countrycodes.put("Uganda","\\(256\\)\\ ?\\d{9}$");

        configProperties = new ConfigProperties();
        configProperties.setCountrycodes(countrycodes);

    }

    @Test
    void createCustomer() {
        Customer createdMarcoSilva = customerService.createCustomer(configProperties, customerRepository, marcoSilva);
        Customer createdMarcoBarbosa = customerService.createCustomer(configProperties, customerRepository, marcoBarbosa);
        Customer createdMartaBarbosa = customerService.createCustomer(configProperties, customerRepository, martaBarbosa);
        Customer createdJoaquimMarques = customerService.createCustomer(configProperties, customerRepository, joaquimMarques);

        //Valid Camerron number. Will be created
        boolean marcoSilvaIsIn = compareCustomerObject(marcoSilva, createdMarcoSilva);

        //Valid Morocco number. Will be created
        boolean marcoBarbosaIsIn = compareCustomerObject(marcoBarbosa, createdMarcoBarbosa);

        //Valid Mozambique number. Will be created
        boolean martaBarbosaIsIn = compareCustomerObject(martaBarbosa, createdMartaBarbosa);

        //Invalid number. Will not be created
        boolean joaquimMarquesIsIn = compareCustomerObject(joaquimMarques, createdJoaquimMarques);

        Assert.isTrue(marcoSilvaIsIn && marcoBarbosaIsIn && martaBarbosaIsIn && !joaquimMarquesIsIn, "All customers were created successfully except Joaquim Marques due to an invalid number");
    }

    @Test
    void createCustomerAlreadyExists() {
        Customer newWalidHammadi = customerService.createCustomer(configProperties, customerRepository, walidHammadi);

        boolean walidHammadiIsIn = compareCustomerObject(walidHammadi, newWalidHammadi);

        Assert.isTrue(!walidHammadiIsIn, "Customer creation rejected successfully. Already exists");
    }

    @Test
    void readCustomersAll() {
        List<Customer> allCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 100, null, null).get("customers");
        List<Customer> allActiveCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 100, null, true).get("customers");
        List<Customer> allInactiveCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 100, null, false).get("customers");

        Assert.isTrue(allActiveCustomers.size() + allInactiveCustomers.size() == allCustomers.size(), "The sum of active and inactive customers must equals the number of all customers.");
    }

    @Test
    void readCustomersAllWithPagination() {
        //Go get all customers
        List<Customer> allCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 100, null, null).get("customers");

        //Let's split them into two pages.
        //This formula to get the bigger half in case the number of allActiveCustomers.size() is odd. Otherwise, we would be left with 3 pages.
        int sizeOfPage = allCustomers.size() - allCustomers.size() / 2;

        List<Customer> page1ofCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, sizeOfPage, null, null).get("customers");
        List<Customer> page2ofCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 1, sizeOfPage, null, null).get("customers");

        //Just to make sure this comes empty
        List<Customer> page3ofCustomers = (List<Customer>) customerService.readCustomersByName(customerRepository, 2, 100, null, null).get("customers");
        if(!page3ofCustomers.isEmpty()){
            fail();
        }

        Assert.isTrue(page1ofCustomers.size() + page2ofCustomers.size() == allCustomers.size(), "The sum of both pages must equals the number of all customers.");
    }

    @Test
    void readCustomersByName() {
        List<Customer> result = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 10, "wal", null).get("customers");
        Assert.isTrue(result.size() == 3, "Returned two customers whose name contains 'wal'");
    }

    @Test
    void readCustomersByNameAndActive() {
        List<Customer> result = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 10, "wal", true).get("customers");
        Assert.isTrue(result.size() == 2, "Returned one customer whose name contains 'wal', and has an active phone number");
    }

    @Test
    void readCustomersByCountry() {
        List<Customer> customersFromCameroon = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Cameroon", null).get("customers");
        List<Customer> customersFromEthiopia = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Ethiopia", null).get("customers");
        List<Customer> customersFromMorocco = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Morocco", null).get("customers");
        List<Customer> customersFromMozambique = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Mozambique", null).get("customers");
        List<Customer> customersFromUganda = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Uganda", null).get("customers");

        if(customersFromCameroon.size() != 10){
            fail();
        }
        if(customersFromEthiopia.size() != 9){
            fail();
        }
        if(customersFromMorocco.size() != 7){
            fail();
        }
        if(customersFromMozambique.size() != 8){
            fail();
        }
        if(customersFromUganda.size() != 7){
            fail();
        }
    }

    @Test
    void readCustomersByCountryAndActive() {
        List<Customer> customersFromCameroon = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Cameroon", true).get("customers");
        List<Customer> customersFromEthiopia = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Ethiopia", true).get("customers");
        List<Customer> customersFromMorocco = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Morocco", true).get("customers");
        List<Customer> customersFromMozambique = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Mozambique", true).get("customers");
        List<Customer> customersFromUganda = (List<Customer>) customerService.readCustomersByCountry(configProperties, customerRepository, 0, 50, "Uganda", true).get("customers");

        System.out.println("OK");
        if(customersFromCameroon.size() != 7){
            fail();
        }
        if(customersFromEthiopia.size() != 8){
            fail();
        }
        if(customersFromMorocco.size() != 5){
            fail();
        }
        if(customersFromMozambique.size() != 6){
            fail();
        }
        if(customersFromUganda.size() != 5){
            fail();
        }
    }

    @Test
    void updateCustomer() {
        walidHammadi.setActive(true);
        Customer updatedWalidHammadiToActive = customerService.updateCustomer(customerRepository, walidHammadi);

        boolean walidHammadiIsUpdated = compareCustomerObject(walidHammadi, updatedWalidHammadiToActive);
        Assert.isTrue(walidHammadiIsUpdated, "Customer Bruno Cardoso was successfully updated");
    }


    @Test
    void updateNonexistentCustomer() {

        //Using dummy customer
        Customer updatedMarcoBarbosaToActive = customerService.updateCustomer(customerRepository, marcoBarbosa);

        Assert.isTrue(updatedMarcoBarbosaToActive == null, "Customer Marco Barbosa was updated unsuccessfully. He does not exists");
    }

    @Test
    void deleteCustomer() {
        boolean walidHammadiIsOut = customerService.deleteCustomer(customerRepository, walidHammadi);

        Assert.isTrue(walidHammadiIsOut, "Customer Walid Hammadi was successfully deleted");
    }

    @Test
    void deleteNonexistentCustomer() {
        //Using dummy customer
        boolean marcoBarbosaIsOut = customerService.deleteCustomer(customerRepository, marcoBarbosa);

        Assert.isTrue(!marcoBarbosaIsOut, "Customer Marco Barbosa was deleted unsuccessfully. He does not exists");
    }

    /**
     * Compares two Customer objects, ignoring the ID parameter
     * @param original
     * @param created
     * @return
     */
    private static boolean compareCustomerObject(Customer original, Customer created){

        if(original == null || created == null){
            return false;
        }

        boolean areEquals = original.getName().equals(created.getName());

        if(!original.getPhone().equals(created.getPhone())){
            areEquals = false;
        }
        if(original.isActive() != created.isActive()){
            areEquals = false;
        }
        return areEquals;
    }

}