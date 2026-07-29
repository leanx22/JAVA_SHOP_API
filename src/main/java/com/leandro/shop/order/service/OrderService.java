package com.leandro.shop.order.service;

import com.leandro.shop.order.dto.OrderCreationRequest;
import com.leandro.shop.order.dto.OrderItemRequest;
import com.leandro.shop.order.entity.Order;
import com.leandro.shop.order.entity.OrderItem;
import com.leandro.shop.order.entity.OrderStatus;
import com.leandro.shop.order.repository.OrderRepository;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.exceptions.BadRequestException;
import com.leandro.shop.shared.exceptions.ResourceNotFoundException;
import com.leandro.shop.shared.exceptions.UnauthorizedException;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import com.leandro.shop.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UUID createOrder(OrderCreationRequest request) {
        User buyer = getAuthenticatedUser();

        Order order = new Order();
        order.setBuyer(buyer);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;
        User seller = null;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productUUID())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (!product.getActive()) {
                throw new BadRequestException("Product " + product.getName() + " is not available");
            }

            if (seller == null) {
                seller = product.getSeller();
            } else if (!seller.getId().equals(product.getSeller().getId())) {
                throw new BadRequestException("All products in a single order must belong to the same seller");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.quantity());
            orderItem.setUnitPrice(product.getPrice());

            order.addItem(orderItem);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        order.setSeller(seller);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // TODO: MP

        return savedOrder.getId();
    }

    private User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getPrincipal() == null){
            throw new UnauthorizedException("Invalid authentication context");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        return userDetails.getUser();
    }

}
