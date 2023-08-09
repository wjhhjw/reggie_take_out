package com.it.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.example.entity.Category;
import com.it.example.mapper.CategoryMapper;
import com.it.example.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
