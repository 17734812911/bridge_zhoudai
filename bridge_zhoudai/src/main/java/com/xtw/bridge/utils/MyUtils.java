package com.xtw.bridge.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/4
 * Description: 数据处理的工具类
 */
public class MyUtils {

    // 将字符串数组转换成double数组
    public static double[] toDoubleArray(String[] strArr) {
        // 定义一个int数组
        double[] arr=new double[strArr.length];
        // 对字符串数组进行遍历
        for (int i = 0; i < arr.length; i++) {
            // 将数组格式的字符串转成双精度数，存储到arr数组中
            arr[i]=Double.parseDouble(strArr[i]);
        }
        return arr;
    }

    // 数组排序
    public static void arraySort(double[] doubleArr){
        Arrays.sort(doubleArr);
    }

    // 合并数组
    public static ArrayList<String> mergeArray(List<String> firstArr, List<String> secondArr){
        ArrayList<String> mergeArr = new ArrayList<>();
        mergeArr.addAll(firstArr);
        mergeArr.addAll(secondArr);
        return mergeArr;
    }

    // 将字符串转换为数组，并返回指定下标的数据
    public static String getPointValue(String str, int point){
        String[] strArr = str.split(",");
        return strArr[point];
    }
}
