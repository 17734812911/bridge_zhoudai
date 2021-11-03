package com.xtw.bridge.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/8/4
 * Description: 数据处理的工具类
 */
public class MyUtils {

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 将字符串数组转换成double数组
    public static BigDecimal[] toDoubleArray(String[] strArr) {
        // 定义一个double数组
        BigDecimal[] arr=new BigDecimal[strArr.length];
        // 对字符串数组进行遍历
        for (int i = 0; i < arr.length; i++) {
            // 将数组格式的字符串转成双精度数，存储到arr数组中
            if(strArr[i].equals("") || strArr[i]==null){
                return null;
            }

            arr[i] = new BigDecimal(strArr[i].trim());
            // arr[i]=Double.parseDouble();
        }
        return arr;
    }

    // 数组排序
    public static void arraySort(BigDecimal[] doubleArr){
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

    // 获取当前日期时间
    public static String getDateTime(Integer monthNumber, Integer addDasNumber, Integer addHNumber){   // addNumber为要加上的天数
        Calendar calendar=Calendar.getInstance();

        String year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
        String month = format((calendar.get(GregorianCalendar.MONTH) + monthNumber));   // 约束要加1,因为生成的约束会比当前少一个月
        String day = format((calendar.get(GregorianCalendar.DAY_OF_MONTH) + addDasNumber));     // 通过addDasNumber参数，决定获取哪一天
        int h = (calendar.get(GregorianCalendar.HOUR) + addHNumber);

        // 判断是否需要加12小时
        if(ifAfternoonByDate()){
            h = h + 12;
        }
        String hour = String.valueOf(h);
        String min = format(calendar.get(GregorianCalendar.MINUTE));
        String second = format(calendar.get(GregorianCalendar.SECOND));

        return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;

    }
    // 格式化时间
    private static String format(Integer num){
        if(num < 10){
            return "0" + num;
        }
        return String.valueOf(num);
    }

    // 判断当前时间是否是下午
    // 0是上午,1是下午
    public static boolean ifAfternoonByDate(){
        GregorianCalendar ca = new GregorianCalendar();
        int i = ca.get(GregorianCalendar.AM_PM);
        if(i == 1){
            return true;
        }
        return false;
    }
}
