package com.project.huinongkit.model;

import java.io.Serializable;

/**
 * @fileName: SelectFilterModel
 * @author: liuboyu
 * @date: 2021/3/16 5:49 PM
 * @description:
 */
public class SelectFilterModel implements Serializable {
    private String start;
    private String end;

    public SelectFilterModel(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
