package stone;

/**
 * @author lichengpeng
 * @desc 字符串 token
 * @date 2020/7/11
 **/
public class StrToken extends Token {
    private String literal;

    protected StrToken(int line, String str) {
        super(line);
        literal = str;
    }

    public boolean isString() {
        return true;
    }

    public String getText() {
        return literal;
    }
}
