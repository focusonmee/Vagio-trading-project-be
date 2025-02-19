package com.example.entity;


import com.example.domain.WithdrawStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawStatus status;

    private long amount;

    @ManyToOne
    private User user;

    private LocalDateTime date = LocalDateTime.now();
}
