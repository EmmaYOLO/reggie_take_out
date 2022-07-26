package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.entity.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Emma_Lyy
 * @create 2022-07-18 18:19
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);

        if(shoppingCart1 != null){
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCart1);
        }else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }


        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }


    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        log.info("购物车减少:{}", shoppingCart);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();

        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);

        if(shoppingCart1.getNumber() == 1){
            shoppingCartService.removeById(shoppingCart1);
        }else{
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCart1);
        }

        return R.success(shoppingCart1);

    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        shoppingCartService.remove(queryWrapper);
        return R.success("购物车已清空");
    }






//
//    /**
//     * 添加购物车
//     *
//     * @param shoppingCart
//     * @return
//     */
//    @PostMapping("/add")
//    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
//        log.info("购物车数据：{}", shoppingCart);
//
//        Long currentId = BaseContext.getCurrentId();
//        shoppingCart.setUserId(currentId);
//
//        Long dishId = shoppingCart.getDishId();
//
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId, currentId);
//
//        if (dishId != null) {
//            //添加到购物车的是菜品
//            queryWrapper.eq(ShoppingCart::getDishId, dishId);
//        } else {
//            //添加到购物车的是套餐
//            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
//        }
//
//        //查询当前菜品或套餐是否在购物车中
//        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
//        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
//
//        if (cartServiceOne != null) {
//            //如果已经存在，就在原来数量基础上加一
//            Integer number = cartServiceOne.getNumber();
//            cartServiceOne.setNumber(number + 1);
//            shoppingCartService.updateById(cartServiceOne);
//        } else {
//            //如果不存在，则添加到购物车，数量默认就是一
//            shoppingCart.setNumber(1);
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            shoppingCartService.save(shoppingCart);
//            cartServiceOne = shoppingCart;
//        }
//        return R.success(cartServiceOne);
//
//    }
//
//    /**
//     * 查看购物车
//     *
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<ShoppingCart>> list() {
//        log.info("查看购物车");
//
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
//        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
//
//        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
//
//        return R.success(list);
//    }
//
//    @PostMapping("/sub")
//    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
//        Long currentId = BaseContext.getCurrentId();
//        shoppingCart.setUserId(currentId);
//
//        Long dishId = shoppingCart.getDishId();
//
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId, currentId);
//
//        if (dishId != null) {
//            queryWrapper.eq(ShoppingCart::getDishId, dishId);
//        } else {
//            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
//        }
//
//        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
//
//        Integer number = cartServiceOne.getNumber();
//        if(number == 1){
//            shoppingCartService.remove(queryWrapper);
//        }else{
//            cartServiceOne.setNumber(number - 1);
//        }
//
//        return R.success(cartServiceOne);
//    }
//
//    @DeleteMapping("/clean")
//    public R<String> clean() {
//
//        //SQL: delete from shopping_cart where user_id = ?
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
//
//        shoppingCartService.remove(queryWrapper);
//
//        return R.success("清空购物车成功");
//
//
//    }


}
