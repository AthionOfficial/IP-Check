package net.risenphoenix.ipcheck.database;

import java.sql.ResultSet;

public class QueryFilter {

    private Object[] data = null;

    public QueryFilter() { }

    public QueryFilter(Object[] data) {
        this.data = data;
    }

    public Object onExecute(ResultSet resultSet) {
        return null;
    }

    public Object[] getData() {
        return this.data;
    }
}
