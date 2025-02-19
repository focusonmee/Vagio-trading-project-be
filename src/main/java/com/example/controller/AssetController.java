package com.example.controller;


import com.example.entity.Asset;
import com.example.entity.User;
import com.example.response.ApiResponse;
import com.example.service.IAssetService;
import com.example.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetController {

    IAssetService assetService;
    IUserService userService;

    @GetMapping("{assetId}")
    public ApiResponse<Asset> getAssetById(
            @PathVariable long assetId
    ) {
        Asset asset = assetService.getAssetById(assetId);
        return ApiResponse.<Asset>builder()
                .result(asset)
                .build();
    }


    @GetMapping("/coin/{coinId}/user")
    public ApiResponse<Asset> getAssetByUserIdAndId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt

    ) {
        User user = userService.findUserbyJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ApiResponse.<Asset>builder()
                .result(asset)
                .build();
    }


    @GetMapping()
    public ApiResponse<List<Asset>> getAssetForUser(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserbyJwt(jwt);
        List<Asset> assets = assetService.getUserAssets(user.getId());
        return ApiResponse.<List<Asset>>builder()
                .result(assets)
                .build();
    }


}
