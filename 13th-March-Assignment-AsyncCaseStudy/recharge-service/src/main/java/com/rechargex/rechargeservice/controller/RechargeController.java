package com.rechargex.rechargeservice.controller;

import com.rechargex.rechargeservice.dto.RechargeRequestDTO;
import com.rechargex.rechargeservice.dto.RechargeResponseDTO;
import com.rechargex.rechargeservice.service.RechargeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recharge")
public class RechargeController {

    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @PostMapping("/")
    public ResponseEntity<RechargeResponseDTO> initiate(
            @Valid @RequestBody RechargeRequestDTO request,
            @AuthenticationPrincipal Long userId) {

        if (userId == null) {
            userId = 1L; // fallback if security context not properly populated
        }
        RechargeResponseDTO responseDTO = rechargeService.initiateRecharge(request, userId);
        return ResponseEntity.status(202).body(responseDTO);
    }
}
