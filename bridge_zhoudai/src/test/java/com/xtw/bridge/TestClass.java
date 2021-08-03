package com.xtw.bridge;

import com.xtw.bridge.service.FibreTemperatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * User: Mr.Chen
 * Date: 2021/8/3
 * Description: No Description
 */
@SpringBootTest
@AutoConfigureMockMvc                   // 自动构建MockMvc对象（下面的@BeforeAll的方法就不需要了）
@ExtendWith(SpringExtension.class)      // 表示为当前测试加上Spring的运行时的容器环境，来进行依赖注入
public class TestClass {
    // mock对象
    @Resource
    private MockMvc mockMvc;
    @Resource
    FibreTemperatureService fibreTemperatureService;


    @Test
    public void testService(){
        fibreTemperatureService.queryAllPartitionMaxValue();
    }


    public void testFibreTemperature() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .request(HttpMethod.POST, "/rest/persions")    // 用POST方式请求"/rest/persions"地址
                        // .contentType("application/json")    // 发送的是json数据
                        // .content(jsonStr)      // 要发送的json字符串
        ).andExpect(MockMvcResultMatchers.status().isOk())      // 返回的状态码是否是200
                // .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("CT"))      // 返回的data数据中的name属性值是否是"CT"
                // .andExpect(MockMvcResultMatchers.jsonPath("$.data.hobby[0].name").value("羽毛球"))
                .andDo(print())   // 执行完成以后打印出来
                .andReturn();      // 将执行结果返回


        // 解决乱码问题
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
    }

}
