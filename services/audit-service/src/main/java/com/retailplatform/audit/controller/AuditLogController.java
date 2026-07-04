package com.retailplatform.audit.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.audit.entity.AuditLog;
import com.retailplatform.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit_logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository repository;

    @GetMapping
    public ApiResponse<Page<AuditLog>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<AuditLog> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<AuditLog> create(@RequestBody AuditLog entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
