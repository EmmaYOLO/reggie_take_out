package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.entity.AddressBook;
import com.itheima.entity.User;
import com.itheima.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Emma_Lyy
 * @create 2022-07-15 1:46
 */


@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        addressBookService.save(addressBook);

        return R.success(addressBook);

    }

    @Transactional
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {

        log.info("addressBook:{}", addressBook);

        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);

        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);

        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     *
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook != null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }

    }


    @GetMapping("/list")
    public R<List<AddressBook>> page(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        addressBookService.removeById(ids);
        return R.success("地址删除成功");
    }

}
