package com.itheima.controller;

/**
 * @author Emma_Lyy
 * @create 2022-07-10 17:10
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize, String name) {
//        //构造分页构造器对象
//        Page<Dish> pageInfo = new Page<>(page, pageSize);
//        Page<DishDto> dishDtoPage = new Page<>();
//
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        //添加过滤条件
//        queryWrapper.like(name != null, Dish::getName, name);
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        //执行分页查询
//        dishService.page(pageInfo, queryWrapper);
//
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
//
//        List<Dish> records = pageInfo.getRecords();
//        List<DishDto> list = records.stream().map((item) -> {//这里把pageInfo的records一个个取出来了
//
//            DishDto dishDto = new DishDto();
//
//            BeanUtils.copyProperties(item, dishDto);//这里copy过去的数据里不包括categoryName
//
//            Long categoryId = item.getCategoryId();//分类id
//            //根据id查询分类对象
//            Category category = categoryService.getById(categoryId);
//            String categoryName = category.getName();
//
//            dishDto.setCategoryName(categoryName);
//
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        dishDtoPage.setRecords(list);
//        return R.success(dishDtoPage);
//    }
    /*
    本来最后是return R.succes(dishPage)的，但dish缺少categoryName信息，
    所以需要一个包含了dishPage而且有categoryName的dishDtoPage；
    step1：Page的属性records，包含了页面信息，先把除了records以外的所有properties拷贝给dishDtoPage；
    step2：把dishPage的records，与获取到的categoryName，组合交给new DishDto()，得到list；
    step3：把2中的list作为dishDtoPage的records。
     */







    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPage, queryWrapper);

        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    /*
    讲解：
    首先我们要清楚，这里我们要做什么，前面的分页，都是最终返回R.success(dishPage)就行了，这里为什么不行呢，因为dishPage给出的信息是不全的，
    页面要求的信息里，有一个categoryName，是dishPage没有的，于是我们就想到，那谁有呢，dishDtoPage有。

    所以，我们最后就return R.success(dishPage)了，转为return R.success(dishDtoPage)；
    这个dishDtoPage必须包含dishPage里的所有内容，以及categoryName；
    categoryName应该放在哪里呢，Page的属性里，有一个records，就是储存这些信息的。

    于是我们就想到，把dishPage分为两部分，一个是records，储存页面这些信息的，一个是除records之外的；
    我们先把除了records之外的所有内容从dishPage，copy给dishDtoPage，
    再把dishPage的records取出来，给records加上categoryName之后，生成一个List<DishDto> list,
    把这个list赋给dishDtoPage的records，dishDtoPage需要包含的信息就全了，

    return R.success(DishDtoPage)即可。

     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);//1代表起售，0是停售
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }



    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }






}
