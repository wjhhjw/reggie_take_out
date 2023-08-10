package com.it.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.example.common.CustomException;
import com.it.example.entity.Category;
import com.it.example.entity.Dish;
import com.it.example.entity.Setmeal;
import com.it.example.mapper.CategoryMapper;
import com.it.example.service.CategoryService;
import com.it.example.service.DishService;
import com.it.example.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService  dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前 需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper();
        //select count(*) from dish where category_id = ?
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1=dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联菜品，如果已经关联，抛出一个业务异常
        if(count1>0){
            //已经关联套餐
            throw new CustomException("当前分类下关联了菜品，不能删除");
            //由于需要在前端展示一下，所以 可以在全局异常处理器中捕获一下。
        }
        //查询当前分类是否关联套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if(count2>0){
            //已经关联套餐
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //都无关联---》正常删除
        super.removeById(id);

    }
}
