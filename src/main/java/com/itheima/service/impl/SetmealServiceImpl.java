package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.CustomException;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emma_Lyy
 * @create 2022-07-09 23:08
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;




    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);//这里存的是setmeal

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();//这里是把属于改套餐的菜品都取出来了

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());//取出来的setmealDishes，缺setmealId属性
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }


    @Transactional
    public void removeWithDish1(List<Long> ids){
        //delete from setmeal where id in ids and status = 0;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if(count > 0){
            throw new CustomException("套餐仍在售卖，无法删除。");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(queryWrapper1);


    }



    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        //step1 删除setmeal中对应套餐数据

        //delete from setmeal where id in (1,2,3) and status = 0;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        //状态：启用
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);

        //count>0，说明存在 启用的套餐，不能删除，要先停用才行
        if(count > 0){
            throw new CustomException("套餐仍在售卖，无法删除。");
        }

        this.removeByIds(ids);//removeByIds只适用于主键

        //step2 删除setmeal_dish中数据

        //这里为什么不能直接用removeByIds，把ids传进去，是因为在setmeal_dish表中，setmeal_id不是主键值

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(queryWrapper1);


    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {

    }
}
