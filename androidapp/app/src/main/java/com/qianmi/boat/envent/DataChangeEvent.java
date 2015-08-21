package com.qianmi.boat.envent;

import java.util.EventObject;

/**
 */
public class DataChangeEvent extends EventObject {

    private static final long serialVersionUID = 61665616565L;

    private int dataCount = 1;

    public DataChangeEvent(Object source, int dataCount) {
        super(source);
        this.dataCount = dataCount;
    }

    public void setDataChange(int dataCount) {
        this.dataCount = dataCount;
    }

    public float getDataCount() {
        return this.dataCount;
    }

}
