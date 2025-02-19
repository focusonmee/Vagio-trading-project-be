package com.example.controller;


import com.example.entity.Coin;
import com.example.entity.User;
import com.example.entity.WatchList;
import com.example.response.ApiResponse;
import com.example.service.IUserService;
import com.example.service.IWatchListService;
import com.example.service.IcoinService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/watchlist")
public class WatchListController {
    IWatchListService watchListService;
    IUserService userService;
    IcoinService coiService;

    @GetMapping
    public ApiResponse<WatchList> getUserWatchList(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserbyJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return ApiResponse.<WatchList>builder()
                .result(watchList)
                .build();
    }

//    @PostMapping("/create")
//    public ApiResponse<WatchList> createWatchList(@RequestHeader("Authorization") String jwt) {
//        User user = userService.findUserbyJwt(jwt);
//        WatchList createWatchList = watchListService.createWatchList(user);
//        return ApiResponse.<WatchList>builder()
//                .result(createWatchList)
//                .build();
//    }

    @PostMapping("/create")
    public ApiResponse<WatchList> getWatchListById(@PathVariable Long watchListId) {
        WatchList watchList = watchListService.findById(watchListId);
        return ApiResponse.<WatchList>builder()
                .result(watchList)
                .build();
    }

    @PatchMapping("/add/coin/{coinId}")
    public ApiResponse<Coin> addItemToWatchList(@PathVariable String coinId,
                                                @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserbyJwt(jwt);
        Coin coin = coiService.findById(coinId);
        Coin addedCoin = watchListService.addItemToWatchList(coin, user);
        return ApiResponse.<Coin>builder()
                .result(addedCoin)
                .build();
    }
}
