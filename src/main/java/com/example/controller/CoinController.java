package com.example.controller;


import com.example.entity.Coin;
import com.example.response.ApiResponse;
import com.example.service.CoinService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("coins")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CoinController {
    CoinService coinService;

    ObjectMapper objectMapper;

    @GetMapping
    public ApiResponse<List<Coin>> getCoinList(@RequestParam("page") int page) {
        List<Coin> coins = coinService.geCoinList(page);
        return ApiResponse.<List<Coin>>builder()
                .result(coins)
                .build();
    }

    @GetMapping("/{coinId}/chart")
    public ApiResponse<JsonNode> getMarketChart(@PathVariable String coinId,
                                                @RequestParam("days") int days) throws JsonProcessingException {
        String response = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(response);
        return ApiResponse.<JsonNode>builder()
                .result(jsonNode)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<JsonNode> searchCoin(
            @RequestParam("q") String keyword) throws JsonProcessingException {
        String coin = coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ApiResponse.<JsonNode>builder()
                .result(jsonNode)
                .build();
    }

    @GetMapping("/top50")
    public ApiResponse<JsonNode> getTop50CoinByMarketCapRank(
    ) throws JsonProcessingException {
        String coin = coinService.getTop50CoinByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ApiResponse.<JsonNode>builder()
                .result(jsonNode)
                .build();
    }

    @GetMapping("/trading")
    public ApiResponse<JsonNode> getTradingCoin(
            @RequestParam("q") String keyword) throws JsonProcessingException {
        String coin = coinService.getTradingCoin();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ApiResponse.<JsonNode>builder()
                .result(jsonNode)
                .build();
    }

    @GetMapping("/details/{coinId}")
    public ApiResponse<JsonNode> getCoinDetails(
            @PathVariable String coinId) throws JsonProcessingException {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ApiResponse.<JsonNode>builder()
                .result(jsonNode)
                .build();
    }
}
