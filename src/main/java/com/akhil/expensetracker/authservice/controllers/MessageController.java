package com.akhil.expensetracker.authservice.controllers;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@Slf4j
public class MessageController {

    @Data
    @Builder
    public static class WelcomeDto {
        private String message;
    }
    @GetMapping("/welcome")
    public ResponseEntity<WelcomeDto> welcome() {
        System.out.println("Endpoint reached");
        return ResponseEntity.ok(
                WelcomeDto.builder()
                        .message("Welcome to Expense Tracker")
                        .build()
        );
    }
}
