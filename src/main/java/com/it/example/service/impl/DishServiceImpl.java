package com.it.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.example.entity.Dish;
import com.it.example.mapper.DishMapper;
import com.it.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
