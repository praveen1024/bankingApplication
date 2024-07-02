package com.example.banking_application.utils;

import org.springframework.stereotype.Component;

import java.time.Year;


public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account!";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been created successfully";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the account number does not exists";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User found";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Amount has been Credited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Amount has been Debited Successfully";

    public static String generateAccountNumber(){

        /**
         *  2024 + 6 digit random number
         */
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        // generate a random number between min and max
        int randomNumber = (int) Math.floor(Math.random() * (max - min+1) + min);

        String year = String.valueOf(currentYear);
        String number = String.valueOf(randomNumber);

        // return year + number;

        // or

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(number).toString();
    }
}
