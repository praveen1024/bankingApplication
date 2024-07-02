package com.example.banking_application.service;

import com.example.banking_application.dto.BankResponse;
import com.example.banking_application.dto.CreditDebitRequest;
import com.example.banking_application.dto.EnquiryRequest;
import com.example.banking_application.dto.UserRequest;
import org.springframework.stereotype.Service;


public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

}
