package com.example.service;

import com.example.entity.Coin;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.repository.CoinRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoinService implements ICoinService {
    CoinRepository coinRepository;

    ObjectMapper objectMapper;

    @Autowired
    public CoinService(CoinRepository coinRepository, ObjectMapper objectMapper) {
        this.coinRepository = coinRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Coin> geCoinList(int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return objectMapper.readValue(response.getBody(),
                    new TypeReference<List<Coin>>() {
                    });

        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }

    }

    @Override
    public String getMarketChart(String coinId, int days) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }
    }

    @Override
    public String getCoinDetails(String coinId) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            Coin coin = new Coin();

            coin.setId(getTextValue(jsonNode, "id"));
            coin.setName(getTextValue(jsonNode, "name"));
            coin.setSymbol(getTextValue(jsonNode, "symbol"));

            JsonNode imageNode = jsonNode.get("image");
            coin.setImage(imageNode != null && imageNode.has("large") ? imageNode.get("large").asText() : "");

            JsonNode marketData = jsonNode.get("market_data");

            coin.setCurrentPrice(getDoubleValue(marketData, "current_price", "usd"));
            coin.setMarketCap(getLongValue(marketData, "market_cap", "usd"));
            coin.setMarketCapRank(getIntValue(marketData, "market_cap_rank"));
            coin.setTotalVolume(getLongValue(marketData, "total_volume", "usd"));
            coin.setHigh24h(getDoubleValue(marketData, "high_24h", "usd"));
            coin.setLow24h(getDoubleValue(marketData, "low_24h", "usd"));
            coin.setPriceChange24h(getDoubleValue(marketData, "price_change_24h", "usd"));
            coin.setPriceChangePercentage24h(getDoubleValue(marketData, "price_change_percentage_24h"));
            coin.setMarketCapChange24h(getLongValue(marketData, "market_cap_change_24h"));
            coin.setMarketCapChangePercentage24h(getDoubleValue(marketData, "market_cap_change_percentage_24h"));
            coin.setTotalSupply(getLongValue(marketData, "total_supply"));

            coinRepository.save(coin);
            return response.getBody();

        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }
    }


    @Override
    public Coin findById(String coinId) {
        return coinRepository.findById(coinId).orElseThrow(
                () -> new AppException(ErrorCode.COIN_NOT_FOUND));
    }

    @Override
    public String searchCoin(String keyword) {

        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }

    }

    @Override
    public String getTop50CoinByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }
    }

    @Override
    public String getTradingCoin(String keyword) {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AppException(ErrorCode.API_REQUEST_FAILED);
        }
    }

    private String getTextValue(JsonNode node, String fieldName) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asText()
                : "";
    }

    private double getDoubleValue(JsonNode node, String fieldName) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asDouble()
                : 0.0;
    }

    private double getDoubleValue(JsonNode node, String fieldName, String subField) {
        return (node != null && node.has(fieldName) && node.get(fieldName).has(subField) && !node.get(fieldName).get(subField).isNull())
                ? node.get(fieldName).get(subField).asDouble()
                : 0.0;
    }

    private long getLongValue(JsonNode node, String fieldName) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asLong()
                : 0L;
    }

    private long getLongValue(JsonNode node, String fieldName, String subField) {
        return (node != null && node.has(fieldName) && node.get(fieldName).has(subField) && !node.get(fieldName).get(subField).isNull())
                ? node.get(fieldName).get(subField).asLong()
                : 0L;
    }

    private int getIntValue(JsonNode node, String fieldName) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asInt()
                : 0;
    }

}
