package com.it.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.example.dto.SetmealDto;
import com.it.example.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     */
    public void saveWithDish(SetmealDto setmealDto);
}
