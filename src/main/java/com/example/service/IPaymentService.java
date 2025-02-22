package com.example.service;

import com.example.domain.PaymentMethod;
import com.example.entity.PaymentOrder;
import com.example.entity.User;
import com.example.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface IPaymentService {
    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderId(Long id);

    Boolean proceedPaymentMethod(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorPaymentLink(User user, Long amount) throws RazorpayException;

    PaymentResponse createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;
}
