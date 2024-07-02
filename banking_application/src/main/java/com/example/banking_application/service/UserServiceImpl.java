package com.example.banking_application.service;

import com.example.banking_application.dto.*;
import com.example.banking_application.entity.User;
import com.example.banking_application.repository.UserRepository;
import com.example.banking_application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * creating an account - saving a new user into the db
         * check if user already has an account
         */

        if(userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
            return response;
        }
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .dob(userRequest.getDob())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(user);
        // Send Email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations! You have successfully created your account.\n Your account details: \n" +
                        "Account name: " + savedUser.getFirstName() +" "+ savedUser.getLastName() + "\n Account number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();

        return response;
    }


    // Balance enquiry, Name enquiry,
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists in the db
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        // checking if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() +" "+ userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        // check if the account exists
        // check if the amount that u intend to withdraw is not more than the account balance

        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        // cannot use < symbol between bigDecimal, so converted into  BigInteger
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(creditDebitRequest.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                            .build())
                    .build();
            //new
        }
    }


}
