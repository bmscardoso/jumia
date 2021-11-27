package com.jumia.brunocardoso.utilities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerUtilsTest {

    static String validCameroonNumber = "(237) 677046616";
    static String invalidCameroonNumber = "(237) 6780009592";

    static String validEthiopia = "(251) 914701723";
    static String invalidEthiopia = "(251) 9119454961";

    static String validMorocco = "(212) 698054317";
    static String invalidMorocco = "(212) 6007989253";

    static String validMozambique = "(258) 847651504";
    static String invalidMozambique = "(258) 84330678235";

    static String validUganda = "(256) 775069443";
    static String invalidUganda = "(256) 7503O6263";

    //Country names
    String cameroon = "Cameroon";
    String ethiopia = "Ethiopia";
    String morocco = "Morocco";
    String mozambique = "Mozambique";
    String uganda = "Uganda";


    Map<String, String> countrycodes;

    @BeforeAll
    void setUp() {
        countrycodes = new HashMap<>();
        countrycodes.put("Cameroon","\\(237\\)\\ ?[2368]\\d{7,8}$");
        countrycodes.put("Ethiopia","\\(251\\)\\ ?[1-59]\\d{8}$");
        countrycodes.put("Morocco","\\(212\\)\\ ?[5-9]\\d{8}$");
        countrycodes.put("Mozambique","\\(258\\)\\ ?[28]\\d{7,8}$");
        countrycodes.put("Uganda","\\(256\\)\\ ?\\d{9}$");
    }

    @Test
    void checkCountryByNumberSuccess() {
        if(!CustomerUtils.checkCountryByNumber(countrycodes, validCameroonNumber).equals("Cameroon")){
            fail();        }
        if(!CustomerUtils.checkCountryByNumber(countrycodes, validEthiopia).equals("Ethiopia")){
            fail();        }
        if(!CustomerUtils.checkCountryByNumber(countrycodes, validMorocco).equals("Morocco")){
            fail();        }
        if(!CustomerUtils.checkCountryByNumber(countrycodes, validMozambique).equals("Mozambique")){
            fail();        }
        if(!CustomerUtils.checkCountryByNumber(countrycodes, validUganda).equals("Uganda")){
            fail();        }
    }

    @Test
    void checkCountryByNumberFail() {
        if(CustomerUtils.checkCountryByNumber(countrycodes, invalidCameroonNumber) != null){
            fail();
        }
        if(CustomerUtils.checkCountryByNumber(countrycodes, invalidEthiopia) != null){
            fail();
        }
        if(CustomerUtils.checkCountryByNumber(countrycodes, invalidMorocco) != null){
            fail();
        }
        if(CustomerUtils.checkCountryByNumber(countrycodes, invalidMozambique) != null){
            fail();
        }
        if(CustomerUtils.checkCountryByNumber(countrycodes, invalidUganda) != null){
            fail();
        }
    }

    @Test
    void checkNumberByCountry() {

        if(!CustomerUtils.checkNumberByCountry(countrycodes, cameroon).equals("(237)")){
            fail();
        }
        if(!CustomerUtils.checkNumberByCountry(countrycodes, ethiopia).equals("(251)")){
            fail();
        }
        if(!CustomerUtils.checkNumberByCountry(countrycodes, morocco).equals("(212)")){
            fail();
        }
        if(!CustomerUtils.checkNumberByCountry(countrycodes, mozambique).equals("(258)")){
            fail();
        }
        if(!CustomerUtils.checkNumberByCountry(countrycodes, uganda).equals("(256)")){
            fail();
        }
    }
}