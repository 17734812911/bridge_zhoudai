package com.xtw.bridge.model.page;

import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/16
 * Description: 分页查询请求封装类
 */
@Data
public class PageRequest {
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
}
