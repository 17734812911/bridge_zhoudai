package com.xtw.bridge.schedule;

import com.xtw.bridge.service.DeviceService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Resource
    DeviceService deviceService;

    //3.添加定时任务（每天23点执行一次）
    @Scheduled(cron = "0 0 23 * * ?")
    private void configureTasks() {
        // 查询当前无故障天数
        Integer number = Integer.parseInt(deviceService.safeDuration());
        // 计算更新无故障天数
        deviceService.calculation(number);
    }



}
