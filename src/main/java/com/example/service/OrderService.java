package com.example.service;

import com.example.domain.OrderStatus;
import com.example.domain.OrderType;
import com.example.entity.Coin;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.User;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class OrderService implements IOrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    WalletService walletService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        return orderRepository.save(order);

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
//        if (orderType == orderType.BUY) {
//            return buyAsset(coin, quantity, user);
//
//        } else if (orderType.equals(OrderType.SELL)) {
//            return sellAsset(coin, quantity, user);
//        }
//        throw new Exception("Invalid order type");
        return null;
    }

    private OrderItem createOrderItem(Coin coin, double quantity
            , double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("quantity should be >0");
        }
        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        return orderRepository.save(order);
    }

//    @Transactional
//    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
//        if (quantity <= 0) {
//            throw new Exception("quantity should be >0");
//        }
//        double sellPrice = coin.getCurrentPrice();
//        double buyPrice = assetToSell.getPrice();
//
//        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
//
//        Order order = createOrder(user, orderItem, OrderType.SELL);
//        orderItem.setOrder(order);
//
//        if (assetTosell.getQuantity() =>quantity){
//            walletService.payOrderPayment(order, user);
//            Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
//            if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
//                assetService.deleteAsset(updatedAsset.getId());
//            }
//            return saveOrder;
//        }
//        throw new Exception("Insufficient quantity to sell");
//    }
}
