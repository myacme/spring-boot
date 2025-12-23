package com.example.boot.controller;


import com.example.boot.annotation.Log;
import com.example.boot.sevice.MySevice;
import com.example.boot.sevice.TransactionSevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 事务测试
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:32
 */
@Tag(name = "事务测试", description = "事务测试")
@RestController
@Log("transanction")
public class TransanctionController {

    @Resource(name = "multiThreadTransaction")
    private TransactionSevice transactionSevice;

    @Operation(summary = "测试事务", description = "测试事务")
    @GetMapping("/test")
    public String testTransaction() {
        try {
            transactionSevice.testTransaction();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}