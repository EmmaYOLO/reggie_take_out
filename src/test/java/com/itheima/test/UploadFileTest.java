package com.itheima.test;


import org.junit.jupiter.api.Test;

/**
 * @author Emma_Lyy
 * @create 2022-07-10 14:46
 */
public class UploadFileTest {
    @Test
    public void test1(){
        String fileName = "123456fdsa.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);

    }
}
