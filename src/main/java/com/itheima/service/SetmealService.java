package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;

import java.util.List;

/**
 * @author Emma_Lyy
 * @create 2022-07-09 23:05
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除setmeal_dish里相关菜品信息
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    void updateWithDish(SetmealDto setmealDto);
}
