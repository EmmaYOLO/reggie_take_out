package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.OrderDetail;
import com.itheima.mapper.OrderDetailMapper;
import com.itheima.service.OrderDetailService;
import com.itheima.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author Emma_Lyy
 * @create 2022-07-20 15:43
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
