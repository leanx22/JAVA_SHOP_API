package com.leandro.shop.order.controller;

import com.leandro.shop.order.dto.OrderCreationRequest;
import com.leandro.shop.order.service.OrderService;
import com.leandro.shop.shared.payload.AppResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<AppResponse<UUID>> createOrder(
            @Valid @RequestBody OrderCreationRequest request
    ){

        UUID orderUUID = orderService.createOrder(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderUUID)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(AppResponse.success("Order created successfully (Pending Payment)", orderUUID));
    }

}
