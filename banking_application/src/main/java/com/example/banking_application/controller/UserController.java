package com.example.banking_application.controller;

import com.example.banking_application.dto.BankResponse;
import com.example.banking_application.dto.CreditDebitRequest;
import com.example.banking_application.dto.EnquiryRequest;
import com.example.banking_application.dto.UserRequest;
import com.example.banking_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/post")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }
}
