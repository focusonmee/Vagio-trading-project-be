package com.example.service;

import com.example.domain.OrderType;
import com.example.entity.Coin;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface IOrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;


}
