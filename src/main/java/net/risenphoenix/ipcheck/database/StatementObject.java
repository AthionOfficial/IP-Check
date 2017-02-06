package net.risenphoenix.ipcheck.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import net.risenphoenix.ipcheck.IPCheck;

public class StatementObject {

    private final IPCheck plugin;
    private String SQL;
    private Object[] values = null;

    public StatementObject(IPCheck ipCheck, String SQL) {
        this.plugin = ipCheck;
        this.SQL = SQL;
    }

    public StatementObject(IPCheck plugin, String SQL, Object[] values) {
        this.plugin = plugin;
        this.SQL = SQL;
        this.values = values;
    }

    public PreparedStatement getStatement(Connection c) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(this.SQL);

            if (this.values != null) {
                for (int i = 1; i <= this.values.length; i++) {

                    if (this.values[i-1] instanceof Integer) {
                        prepStmt.setInt(i, (Integer) this.values[i-1]);
                        continue;
                    }

                    if (this.values[i-1] instanceof String) {
                        prepStmt.setString(i, this.values[i-1].toString());
                        continue;
                    }

                    this.plugin.getLogger().severe( this.plugin
                            .getLocalizationManager()
                            .getLocalString("BAD_SQL_INPUT"));
                }
            }

            return prepStmt;
        } catch (Exception e) {
        	this.plugin.getLogger().severe( this.plugin
                    .getLocalizationManager()
                    .getLocalString("DB_PREP_STMT_ERR"));
            e.printStackTrace();
        }

        return null;
    }
}
