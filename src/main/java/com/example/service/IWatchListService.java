package com.example.service;

import com.example.entity.Coin;
import com.example.entity.User;
import com.example.entity.WatchList;

public interface IWatchListService {

    WatchList findUserWatchList(Long userId);

    WatchList createWatchList(User user);

    WatchList findById(Long id);

    Coin addItemToWatchList(Coin coin, User user);
}
