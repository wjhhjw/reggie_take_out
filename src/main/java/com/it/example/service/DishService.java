package com.it.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.example.dto.DishDto;
import com.it.example.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入口味数据
    //需要操作两张表 dish, dish_flavor
    public void saveWithFlavor(DishDto dishDto);
}
