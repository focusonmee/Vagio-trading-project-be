package com.example.controller;

import com.example.entity.*;
import com.example.response.ApiResponse;
import com.example.service.IOrderService;
import com.example.service.IPaymentService;
import com.example.service.IUserService;
import com.example.service.IWalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/wallet")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WalletController {

    IWalletService walletService;
    IUserService userService;
    IOrderService orderService;
    IPaymentService paymentService;

    @GetMapping
    public ApiResponse<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserbyJwt(jwt);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
        Wallet wallet = walletService.getUserWallet(user);

        return ApiResponse.<Wallet>builder()
                .result(wallet)
                .build();
    }

    @PutMapping("/{walletId}/transfer")
    public ApiResponse<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction request
    ) throws Exception {
        User senderUser = userService.findUserbyJwt(jwt);
        if (senderUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }

        Wallet receiverWallet = walletService.findWalletById(walletId);
        if (receiverWallet == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver wallet not found");
        }

        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, request.getAmount());

        return ApiResponse.<Wallet>builder()
                .result(wallet)
                .build();
    }

    @PutMapping("/order/{orderId}/pay")
    public ApiResponse<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception {
        User user = userService.findUserbyJwt(jwt);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
        }

        Wallet wallet = walletService.payOrderPayment(order, user);
        return ApiResponse.<Wallet>builder()
                .result(wallet)
                .build();
    }

    @PutMapping("/deposit")
    public ApiResponse<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId
    ) throws Exception {
        User user = userService.findUserbyJwt(jwt);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }

        Wallet wallet = walletService.getUserWallet(user);
        if (wallet == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User wallet not found");
        }

        PaymentOrder order = paymentService.getPaymentOrderId(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Order ID");
        }

        Boolean status = paymentService.proceedPaymentMethod(order, paymentId);
        if (!status) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment failed");
        }

        wallet = walletService.addBalance(wallet, order.getAmount());

        return ApiResponse.<Wallet>builder()
                .result(wallet)
                .build();
    }
}
