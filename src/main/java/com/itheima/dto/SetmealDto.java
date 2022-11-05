package com.itheima.dto;

import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

    private Long categoryId;

    private BigDecimal price;

    private String description;

    private String image;

}
