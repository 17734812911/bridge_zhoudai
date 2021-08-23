package com.xtw.bridge.utils;

import com.github.pagehelper.PageInfo;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;

/**
 * User: Mr.Chen
 * Date: 2021/8/16
 * Description: 分页查询相关工具类
 */
public class PageUtils {
    /**
     * 将分页信息封装到统一的接口
     * @param pageRequest
     * @param page
     * @return
     */
    public static PageResult getPageResult(PageRequest pageRequest, PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}
