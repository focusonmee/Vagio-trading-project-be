package com.example.controller;


import com.example.entity.PaymentDetails;
import com.example.entity.User;
import com.example.response.ApiResponse;
import com.example.service.IPaymentDetailsService;
import com.example.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api")
public class PaymentDetailsController {
    IPaymentDetailsService paymentDetailsService;
    IUserService userService;

    @PostMapping("/payment-details")
    public ApiResponse<PaymentDetails> addPaymentDetails(
            @RequestBody PaymentDetails request,
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserbyJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                request.getAccountNumber(),
                request.getAccountHolderName(),
                request.getIfsc(),
                request.getBankName(),
                user
        );
        return ApiResponse.<PaymentDetails>builder()
                .result(paymentDetails)
                .build();
    }

    @GetMapping("/payment-details")
    public ApiResponse<PaymentDetails> getUserPaymentDetails(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserbyJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUsersPaymentDetails(user);

        return ApiResponse.<PaymentDetails>builder()
                .result(paymentDetails)
                .build();
    }
}
