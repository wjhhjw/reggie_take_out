package com.it.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.example.common.R;
import com.it.example.dto.SetmealDto;
import com.it.example.entity.Category;
import com.it.example.entity.Setmeal;
import com.it.example.service.CategoryService;
import com.it.example.service.SetmealDishService;
import com.it.example.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    //注入一下
    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,lambdaQueryWrapper);
        //对象拷贝  因为records的泛型不一样，所以要忽略掉
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> list = setmealPage.getRecords();

        //对list进行处理  转化成想要的对象
        List<SetmealDto> setmealDtos = list.stream().map((item)-> {
            Category category = categoryService.getById(item.getCategoryId());

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类名称
            if(category!=null){
                setmealDto.setCategoryName(category.getName());
            }


            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtos);

        return R.success(setmealDtoPage);
    }
}
