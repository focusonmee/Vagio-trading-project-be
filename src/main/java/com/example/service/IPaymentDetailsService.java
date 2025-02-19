package com.example.service;

import com.example.entity.PaymentDetails;
import com.example.entity.User;

public interface IPaymentDetailsService {
    PaymentDetails addPaymentDetails(String accountNumber,
                                     String accountHolderName,
                                     String isfc,
                                     String bankName,
                                     User user);

    PaymentDetails getUsersPaymentDetails(User user);
}
