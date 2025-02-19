package com.example.controller;


import com.example.entity.Order;
import com.example.entity.User;
import com.example.entity.Wallet;
import com.example.entity.WalletTransaction;
import com.example.response.ApiResponse;
import com.example.service.IUserService;
import com.example.service.IWalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WalletController {

    IWalletService walletService;

    IUserService userService;


    @GetMapping
    public ApiResponse<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserbyJwt(jwt);
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
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, request.getAmount());

        return ApiResponse.<Wallet>builder()
                .result(wallet)
                .build();
    }

//    @PutMapping("/wallet/order/{orderId}/pay")
//    public ApiResponse<Wallet> payOrderPayment(
//            @RequestHeader("Authorization") String jwt,
//            @PathVariable Long orderId
//    ) throws Exception {
//        User senderUser = userService.findUserbyJwt(jwt);
//        Order order = or
//        return ApiResponse.<Wallet>builder()
//                .result(wallet)
//                .build();
//    }

}
