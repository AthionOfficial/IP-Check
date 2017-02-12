package net.risenphoenix.ipcheck.util;

/* This class is used for the parsing of String messages from user input.*/
public class MessageParser {

    private String[] args;
    private int startPos;

    public MessageParser(String[] args, int startPos) {
        this.args = args;
        this.startPos = startPos;
    }

    public String parseMessage() {
        String message = null;

        if (args.length >= startPos) {
            StringBuilder msgParse = new StringBuilder();

            for (int i = startPos; i < args.length; i++) {
                msgParse.append(args[i]);
                if (!(i == (args.length - 1))) msgParse.append(" ");
            }

            message = msgParse.toString();
        }

        return message;
    }
}
