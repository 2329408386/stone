package stone;

import java.io.IOException;

/**
 * @author lichengpeng
 * @desc 解析异常处理
 * @date 2020/7/11
 **/
public class ParseException extends Exception {
    public ParseException(Token t) {
        this("", t);
    }

    public ParseException(String msg, Token t) {
        super("syntax error at: " + location(t) + ". " + msg);
    }

    private static String location(Token t) {
        if(t == Token.EOF) {
            return "last line";
        }else {
            return t.getText() + " at line: " + t.getLineNumber();
        }
    }

    public ParseException(IOException e) {
        super(e);
    }

    public ParseException(String msg) {
        super(msg);
    }
}
