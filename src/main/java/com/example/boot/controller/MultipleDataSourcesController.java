package com.example.boot.controller;


import com.example.boot.bean.Result;
import com.example.boot.bean.UserPO;
import com.example.boot.sevice.MultipleDataSourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:33
 */
@RestController
@RequestMapping("/mult-date")
public class MultipleDataSourcesController {

    private final MultipleDataSourcesService multipleDataSourcesService;
    @Autowired
    public MultipleDataSourcesController(MultipleDataSourcesService multipleDataSourcesService) {
        this.multipleDataSourcesService = multipleDataSourcesService;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/get-user")
    public Result<UserPO> getUser() {
        try {
            return Result.ok(multipleDataSourcesService.getUser());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}