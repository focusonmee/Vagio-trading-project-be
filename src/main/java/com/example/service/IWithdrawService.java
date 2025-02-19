package com.example.service;

import com.example.entity.User;
import com.example.entity.Withdraw;

import java.util.List;

public interface IWithdrawService {

    Withdraw requestWithdraw(Long amount, User user);

    Withdraw processingWithdraw(Long withdrawId, boolean accept);

    List<Withdraw> getUserWithdrawHistory(User user);

    List<Withdraw> getAllWithdrawRequest();

}
