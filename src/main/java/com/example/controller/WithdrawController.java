package com.example.controller;


import com.example.entity.User;
import com.example.entity.Wallet;
import com.example.entity.Withdraw;
import com.example.response.ApiResponse;
import com.example.service.IUserService;
import com.example.service.IWalletService;
import com.example.service.IWithdrawService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawController {
    IWithdrawService withdrawService;
    IWalletService walletService;
    IUserService userService;

    @GetMapping("/{amount}")
    public ApiResponse<?> withdrawRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserbyJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdraw withdraw = withdrawService.requestWithdraw(amount, user);
        walletService.addBalance(userWallet, -withdraw.getAmount());
        return ApiResponse.builder()
                .result(withdraw)
                .build();
    }

    @PatchMapping("/admin/withdraw/{id}/proceed/{accept}")
    public ApiResponse<?> proceedWithdraw(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserbyJwt(jwt);
        Withdraw withdraw = withdrawService.processingWithdraw(id, accept);

        Wallet userWallet = walletService.getUserWallet(user);
        if (!accept) {
            walletService.addBalance(userWallet, withdraw.getAmount());
        }
        return ApiResponse.builder()
                .result(withdraw)
                .build();
    }

    @PatchMapping()
    public ApiResponse<List<Withdraw>> getWithdrawHistory(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserbyJwt(jwt);
        List<Withdraw> withdraws = withdrawService.getUserWithdrawHistory(user);
        return ApiResponse.<List<Withdraw>>builder()
                .result(withdraws)
                .build();
    }
}
