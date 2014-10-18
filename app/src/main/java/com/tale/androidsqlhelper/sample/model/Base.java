package com.tale.androidsqlhelper.sample.model;

import com.tale.androidsqlhelper.ITable;

/**
 * Created by giang on 18/10/2014.
 */
public class Base implements ITable {

    private long _id;

    @Override
    public long get_id() {
        return _id;
    }
}
