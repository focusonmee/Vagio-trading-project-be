package com.example.service;

import com.example.entity.Coin;

import java.util.List;

public interface IcoinService {

    List<Coin> geCoinList(int page);

    String getMarketChart(String coinId, int days);

    String getCoinDetails(String coinId);

    Coin findById(String coinId);

    String searchCoin(String keyword);

    String getTop50CoinByMarketCapRank();

    String getTradingCoin();
}
