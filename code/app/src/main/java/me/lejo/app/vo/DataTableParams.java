package me.lejo.app.vo;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class DataTableParams {

    private String search;
    private String order;
    private String orderDir;
    private int page;
    private int size; // page size
    private int draw; // used by DataTable to prevent CSS Cross Site Scripting

    public DataTableParams(HttpServletRequest request) {

        search = request.getParameter("search[value]");
        int orderColumnIndex = Integer.valueOf(request.getParameter("order[0][column]"));
        order = request.getParameter("columns[" + orderColumnIndex + "][name]");
        orderDir = StringUtils.upperCase(request.getParameter("order[0][dir]"));
        size = Integer.valueOf(request.getParameter("length"));
        // data tables use a start to indicate the start of the record, we have to convert it to zero-based page base
        // on size
        page = 0; // if no start parameter, page 0 wanted
        if (StringUtils.isNotEmpty(request.getParameter("start"))) {
            int start = Integer.valueOf(request.getParameter("start"));
            page = start / size;
        }
        draw = Integer.valueOf(request.getParameter("draw"));
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}
