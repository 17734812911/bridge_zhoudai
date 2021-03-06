package com.xtw.bridge.model.page;

import lombok.Data;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/16
 * Description: 分页查询结果封装类
 */
@Data
public class PageResult {
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;
    /**
     * 记录总数
     */
    private long totalSize;
    /**
     * 页码总数
     */
    private int totalPages;
    /**
     * 数据模型
     */
    private List<?> content;
}
