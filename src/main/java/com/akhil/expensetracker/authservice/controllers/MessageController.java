package com.akhil.expensetracker.authservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class MessageController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Expense Tracker";
    }
}
