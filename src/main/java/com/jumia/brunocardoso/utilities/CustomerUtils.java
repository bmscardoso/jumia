package com.jumia.brunocardoso.utilities;

import java.util.Map;

public class CustomerUtils {

    public static String checkCountryByNumber(Map<String, String> countrycodes, String phone){
        for(Map.Entry<String, String> countryCode : countrycodes.entrySet()){
            if(phone.matches(countryCode.getValue())){
                return countryCode.getKey();
            }
        }
        return null;
    }
}
