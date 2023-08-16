package com.it.example.controller;

import com.it.example.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dishFlavor")
@Slf4j
public class DishFlavorController {
    @Autowired
    private DishFlavorService dishFlavorService;
}
