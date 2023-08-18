package com.it.example.dto;

import com.it.example.entity.Setmeal;
import com.it.example.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
