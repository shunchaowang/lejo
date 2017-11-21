package me.lejo.app.vo;

import java.util.List;

public class DataTableResult<T> {

    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
    private int draw; // the same value with the request

    public DataTableResult(DataTableParams params) {
        draw = params.getDraw();
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
