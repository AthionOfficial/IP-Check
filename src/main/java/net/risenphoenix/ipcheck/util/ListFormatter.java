package net.risenphoenix.ipcheck.util;

import java.util.ArrayList;

public class ListFormatter {

    private ArrayList<String> input;
    private FormatFilter filter = null;

    public ListFormatter(ArrayList<String> input) {
        this.input = input;
    }

    public ListFormatter(ArrayList<String> input, FormatFilter filter) {
        this.input = input;
        this.filter = filter;
    }

    public StringBuilder getFormattedList() {
        // Convert the Input into a fixed Array
        ArrayList<String> filtered = new ArrayList<String>();
        String[] convert;

        // Filter Input if so requested
        if (filter != null) {
            for (String s:input) {
                String result = this.filter.execute(s);
                if (result != null && !result.equals("")) filtered.add(result);
            }

            convert = new String[filtered.size()];
            filtered.toArray(convert);
        } else {
            convert = new String[input.size()];
            input.toArray(convert);
        }

        // New String Builder
        StringBuilder sb = new StringBuilder();

        // Format the List
        for (int i = 0; i < convert.length; i++) {
            if (convert.length == 1) {
                sb.append(convert[0]);
                break;
            } else if (convert.length == 2) {
                sb.append(convert[0]);
                sb.append(" and ");
                sb.append(convert[1]);
                break;
            } else if (convert.length > 2) {
                sb.append(convert[i]);

                if (i == (convert.length - 2)) {
                    sb.append(" and ");
                } else if (i == (convert.length - 1)) {
                    sb.append(".");
                } else {
                    sb.append(", ");
                }
            }
        }

        return sb;
    }

}
