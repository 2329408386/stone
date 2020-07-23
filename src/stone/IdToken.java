package stone;

/**
 * @author lichengpeng
 * @desc 标识符 token
 * @date 2020/7/11
 **/
public class IdToken extends Token{
    private String text;

    protected IdToken(int line, String id) {
        super(line);
        text = id;
    }

    public boolean isIdentifier() {
        return true;
    }

    public String getText() {
        return text;
    }
}
