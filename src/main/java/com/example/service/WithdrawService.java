package com.example.service;

import com.example.domain.WithdrawStatus;
import com.example.entity.User;
import com.example.entity.Withdraw;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.repository.WithdrawRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawService implements IWithdrawService {

    WithdrawRepository withdrawRepository;


    @Override
    public Withdraw requestWithdraw(Long amount, User user) {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(amount);
        withdraw.setUser(user);
        withdraw.setStatus(WithdrawStatus.PENDING);
        return withdrawRepository.save(withdraw);
    }

    @Override
    public Withdraw processingWithdraw(Long withdrawId, boolean accept) {
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAW_NOT_FOUND));
        withdraw.setDate(LocalDateTime.now());
        if (accept) {
            withdraw.setStatus(WithdrawStatus.SUCCESS);
        } else {
            withdraw.setStatus(WithdrawStatus.PENDING);
        }
        return withdrawRepository.save(withdraw);
    }

    @Override
    public List<Withdraw> getUserWithdrawHistory(User user) {
        return withdrawRepository.findByUserId(user.getId());

    }

    @Override
    public List<Withdraw> getAllWithdrawRequest() {
        return withdrawRepository.findAll();
    }
}
