package com.example.controller;


import com.example.domain.PaymentMethod;
import com.example.entity.PaymentOrder;
import com.example.entity.User;
import com.example.response.ApiResponse;
import com.example.response.PaymentResponse;
import com.example.service.IPaymentService;
import com.example.service.IUserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    IUserService userService;
    IPaymentService paymentService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ApiResponse<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt)
            throws RazorpayException, StripeException {

        User user = userService.findUserbyJwt(jwt);
        PaymentResponse paymentResponse;
        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);
        if (paymentMethod == PaymentMethod.RAZORPAY) {
            paymentResponse = paymentService.createRazorPaymentLink(user, amount);
        } else {
            paymentResponse = paymentService.createStripePaymentLink(user, amount, order.getId());
        }
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentResponse)
                .build();
    }
}
