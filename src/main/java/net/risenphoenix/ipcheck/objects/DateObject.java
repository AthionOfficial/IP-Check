package net.risenphoenix.ipcheck.objects;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateObject {

    private String player;
    private Timestamp date;

    public DateObject(String player, String datestamp) {
        this.player = player;
        this.date = this.parseTimestamp(datestamp);
    }

    public Timestamp getDate() {
        return this.date;
    }

    public String getPlayer() {
        return this.player;
    }

    private Timestamp parseTimestamp(String stamp) {
        SimpleDateFormat dF =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date parsedDate = null;

        try {
            parsedDate = dF.parse(stamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new java.sql.Timestamp(parsedDate.getTime());
    }

}
