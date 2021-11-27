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

        marcoSilva = new Customer();
        marcoSilva.setName("Marco Silva");
        marcoSilva.setPhone("(237) 677046617");
        marcoSilva.setActive(true);

        marcoBarbosa = new Customer();
        marcoBarbosa.setName("marco barbosa");
        marcoBarbosa.setPhone("(212) 698054318");
        marcoBarbosa.setActive(false);

        martaBarbosa = new Customer();
        martaBarbosa.setName("Marta barbosa");
        martaBarbosa.setPhone("(258) 847651505");
        martaBarbosa.setActive(false);

        joaquimMarques = new Customer();
        joaquimMarques.setName("Joaquim Marques");
        joaquimMarques.setPhone("(351) 912345679");
        joaquimMarques.setActive(false);

        walidHammadi = new Customer();
        walidHammadi.setName("Walid Hammadi");
        walidHammadi.setPhone("(212) 6007989253");
        walidHammadi.setActive(false);

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

        //Valid Camerron number. Will be created
        boolean marcoSilvaIsIn = compareCustomerObject(marcoSilva, createdMarcoSilva);

        //Valid Morocco number. Will be created
        boolean marcoBarbosaIsIn = compareCustomerObject(marcoBarbosa, createdMarcoBarbosa);

        //Valid Mozambique number. Will be created
        boolean martaBarbosaIsIn = compareCustomerObject(martaBarbosa, createdMartaBarbosa);

        //Invalid number. Will not be created
        boolean joaquimMarquesIsIn = compareCustomerObject(joaquimMarques, createdMartaBarbosa);

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
        List<Customer> result = (List<Customer>) customerService.readCustomersByName(customerRepository, 0, 10, null, null).get("customers");
        Assert.isTrue(result.size() == 10, "Returned a full page of 10 Customers");
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