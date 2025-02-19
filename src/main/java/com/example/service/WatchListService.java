package com.example.service;

import com.example.entity.Coin;
import com.example.entity.User;
import com.example.entity.WatchList;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.repository.WatchListRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WatchListService implements IWatchListService {

    WatchListRepository watchListRepository;

    @Override
    public WatchList findUserWatchList(Long userId) {
        WatchList watchList = watchListRepository.findByUserId(userId);
        if (watchList == null) {
            throw new AppException(ErrorCode.WATCHLIST_NOT_FOUND);
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) {
        return watchListRepository.findById(id)
                .orElseThrow((() -> new AppException(ErrorCode.WATCHLIST_NOT_FOUND)));
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) {
        WatchList watchList = findUserWatchList(user.getId());
        if (watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        } else watchList.getCoins().add(coin);
        watchListRepository.save(watchList);
        return coin;
    }
}
