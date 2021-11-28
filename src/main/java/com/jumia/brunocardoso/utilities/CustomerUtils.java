package com.jumia.brunocardoso.utilities;

import java.util.Map;

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
}
