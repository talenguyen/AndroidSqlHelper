package com.tale.androidsqlhelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GIANG
 * Date: 12/22/13
 * Time: 11:22 AM
 */
public interface DBContract {

    /**
     * Get array of database tables in Class.
     *
     * @return array of Class present for database table.
     */
    public abstract List<Class<? extends ITable>> getTableClasses();

    /**
     * Get version of database
     *
     * @return Version of database
     */
    public abstract int getDBVersion();

    /**
     * Get name of database
     *
     * @return Name of database
     */
    public abstract String getDBName();
}
