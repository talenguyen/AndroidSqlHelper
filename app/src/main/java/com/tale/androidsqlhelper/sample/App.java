package com.tale.androidsqlhelper.sample;

import android.app.Application;

import com.tale.androidsqlhelper.AndroidSqlHelper;
import com.tale.androidsqlhelper.DBContract;
import com.tale.androidsqlhelper.ITable;
import com.tale.androidsqlhelper.sample.model.PrimaryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giang on 18/10/2014.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidSqlHelper.init(this, new DBContract() {
            @Override
            public List<Class<? extends ITable>> getTableClasses() {
                List<Class<? extends ITable>> tables = new ArrayList<Class<? extends ITable>>();
                tables.add(PrimaryType.class);
                return tables;
            }

            @Override
            public int getDBVersion() {
                return 1;
            }

            @Override
            public String getDBName() {
                return App.class.getSimpleName();
            }
        });
    }
}
