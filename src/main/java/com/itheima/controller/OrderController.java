package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.entity.Orders;
import com.itheima.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Emma_Lyy
 * @create 2022-07-20 15:44
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}", orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){

        Page<Orders> pageInfo = new Page<>(page, pageSize);

        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        Page<Orders> page1 = ordersService.page(pageInfo, queryWrapper);

        return R.success(page1);


    }

}
