package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Category;

/**
 * @author Emma_Lyy
 * @create 2022-07-09 17:44
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
