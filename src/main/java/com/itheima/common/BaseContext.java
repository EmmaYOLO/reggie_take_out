package com.itheima.common;

/**
 * @author Emma_Lyy
 * @create 2022-07-09 15:58
 * 基于ThreadLocal的封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }


}

