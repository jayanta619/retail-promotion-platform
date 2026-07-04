package com.retailplatform.notification.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.notification.entity.Notification;
import com.retailplatform.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repository;

    @GetMapping
    public ApiResponse<Page<Notification>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Notification> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<Notification> create(@RequestBody Notification entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
