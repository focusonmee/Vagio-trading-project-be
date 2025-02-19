package com.example.service;


import com.example.entity.Order;
import com.example.entity.User;
import com.example.entity.Wallet;

public interface IWalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet, Long money);

    Wallet findWalletById(Long id);

    Wallet walletToWalletTransfer(User sender,Wallet receiverWallet,Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;
}
