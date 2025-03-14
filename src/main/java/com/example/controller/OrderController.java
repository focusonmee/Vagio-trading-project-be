package com.example.controller;


import com.example.domain.OrderType;
import com.example.entity.Coin;
import com.example.entity.Order;
import com.example.entity.User;
import com.example.entity.Wallet;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.request.CreateOrderRequest;
import com.example.response.ApiResponse;
import com.example.service.IOrderService;
import com.example.service.IUserService;
import com.example.service.IWalletService;
import com.example.service.ICoinService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {
    IOrderService orderService;
    IUserService userService;
    ICoinService coinService;
    IWalletService walletService;

    @GetMapping("/pay")
    public ApiResponse<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                              @RequestBody CreateOrderRequest request) throws Exception {
        User user = userService.findUserbyJwt(jwt);
        Coin coin = coinService.findById(request.getCoinId());

        return ApiResponse.<Order>builder()
                .result(orderService.processOrder(coin, request.getQuantity()
                        , request.getOrderType(), user)).build();
    }


    @GetMapping("/{orderId}")
    public ApiResponse<Order> getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId
    ) {

        User user = userService.findUserbyJwt(jwtToken);

        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ApiResponse.<Order>builder()
                    .result(order).build();
        } else {
            throw new AppException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    @GetMapping()
    public ApiResponse<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    ) {

        Long userId = userService.findUserbyJwt(jwtToken).getId();

        List<Order> userOrders = orderService.getAllOrderOfUser(userId, order_type, asset_symbol);
        return ApiResponse.<List<Order>>builder()
                .result(userOrders)
                .build();
    }

}
