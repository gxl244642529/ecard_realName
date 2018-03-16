package com.damai.core;

import java.util.List;

/**
 * 用来描述一个分页列表
 * @author Randy
 *
 * @param <T>
 */
public class DMPage<T> {
    List<T> list;
    /**
     * 当前分页，1开始
     */
    int page;
    /**
     * 总分页
     */
    int total;


    public List<T> getList() {
        return list;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public boolean isLast(){
        return page >= total;
    }

    public boolean isFirst(){
        return page <= 1;
    }

}
