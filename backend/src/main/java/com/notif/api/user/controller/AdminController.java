package com.notif.api.user.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/admin")
public class AdminController {
    @GetMapping
    public String get() {
        return "GET:: admin controller";
    }

    @PostMapping
    public String post() {
        return "POST:: admin controller";
    }

    @PutMapping
    public String put() {
        return "PUT:: admin controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE:: admin controller";
    }
}