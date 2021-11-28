package com.jumia.brunocardoso.utilities;

import com.jumia.brunocardoso.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * @SuppressWarnings("squid:S4784") Ignore SonaQube's suggestion to make sure that using a regular expression is safe here.
 */
@SuppressWarnings("squid:S4784")
public class CustomerUtils {

    private CustomerUtils() {
    }

    /**
     * Returns the country of phone number
     * @param countrycodes
     * @param phone
     * @return
     */
    public static String checkCountryByNumber(Map<String, String> countrycodes, String phone){
        for(Map.Entry<String, String> countryCode : countrycodes.entrySet()){
            if(phone.matches(countryCode.getValue())){
                return countryCode.getKey();
            }
        }
        return null;
    }

    /**
     * Returns de phone country code
     * @param country
     * @return
     */
    public static String checkNumberByCountry(Map<String, String> countrycodes, String country){
        for(Map.Entry<String, String> countryCode : countrycodes.entrySet()){
            if(country.equalsIgnoreCase(countryCode.getKey())){
                String code = countryCode.getValue().replaceAll(".*\\(|\\\\\\).*", "");
                return String.format("(%s)", code);
            }
        }
        return null;
    }

    public static Map<String, Object> mountResponse(Page<Customer> pageCustomers){

        Map<String, Object> response = new HashMap<>();
        response.put("customers", pageCustomers.getContent());
        response.put("currentPage", pageCustomers.getNumber());
        response.put("totalItems", pageCustomers.getTotalElements());
        response.put("totalPages", pageCustomers.getTotalPages());

        return response;
    }
}
