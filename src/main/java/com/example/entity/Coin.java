package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coin {

    @Id
    @JsonProperty("id")
    String id;

    @JsonProperty("symbol")
    String symbol;

    @JsonProperty("name")
    String name;

    @JsonProperty("image")
    String image;

    @JsonProperty("current_price")
    double currentPrice;

    @JsonProperty("market_cap")
    long marketCap;

    @JsonProperty("market_cap_rank")
    int marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    long fullyDilutedValuation;

    @JsonProperty("total_volume")
    long totalVolume;

    @JsonProperty("high_24h")
    double high24h;

    @JsonProperty("low_24h")
    double low24h;

    @JsonProperty("price_change_24h")
    double priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    double priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    long marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    double marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    long circulatingSupply;

    @JsonProperty("total_supply")
    long totalSupply;

    @JsonProperty("max_supply")
    long maxSupply;

    @JsonProperty("ath")
    double ath;

    @JsonProperty("ath_change_percentage")
    double athChangePercentage;

    @JsonProperty("ath_date")
    LocalDateTime athDate;

    @JsonProperty("atl")
    double atl;

    @JsonProperty("atl_change_percentage")
    double atlChangePercentage;

    @JsonProperty("atl_date")
    LocalDateTime atlDate;

    @JsonProperty("roi")
    @JsonIgnore
    String roi;

    @JsonProperty("last_updated")
    LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "watchlist_id")
    @JsonIgnore
    private WatchList watchList;
}
