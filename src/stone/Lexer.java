package stone;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lichengpeng
 * @desc 词法分析器
 * @date 2020/7/11
 **/
public class Lexer {
    // pattern = 空白 + (注释 or 字符串 or 标识符)
    // 标识符   = (变量 or 逻辑运算符 or 比较运算符 or 标点符号)
    public static String regexPat = "\\s*((//.*)|" + "([0-9]+)|" + "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|" + "([A-Z_a-z][A-Z_a-z0-9]*)|" +
            "<=|>=|==|&&|\\|\\||\\p{Punct})?";

    private Pattern pattern = Pattern.compile(regexPat);
    private ArrayList<Token> queue = new ArrayList<Token>();
    private boolean hashMore;
    private LineNumberReader reader;

    public Lexer(Reader r) {
        hashMore = true;
        reader   = new LineNumberReader(r);
    }

    // 读取下一个token，并从队列中删除
    public Token read() throws ParseException {
        if(fillQueue(0)) {
            return queue.remove(0);
        }else {
            return Token.EOF;
        }
    }

    // 读取第i个token
    public Token peek(int i) throws ParseException {
        if(fillQueue(i)) {
            return queue.get(i);
        }else {
            return Token.EOF;
        }
    }

    // 填充token队列
    private boolean fillQueue(int i) throws ParseException {
        while(i >= queue.size()) {
            if(hashMore) {
                readLine();
            }else {
                return false;
            }
        }
        return true;
    }

    // 读取一行
    protected void readLine() throws ParseException{
        String line;
        try {
            line = reader.readLine();
        }catch(IOException e) {
            throw new ParseException(e);
        }

        if(line == null) {
            hashMore = false;
            return;
        }

        int lineNo = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);

        int pos = 0;
        int endPos = line.length();
        while(pos < endPos) {
            matcher.region(pos, endPos);
            if(matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            }else {
                throw new ParseException("bad token at line: " + lineNo);
            }
        }
        queue.add(new IdToken(lineNo, Token.EOL));
    }

    protected void addToken(int lineNo, Matcher matcher) {
        /**
         * java regex group example:
         * Regex: ([a-zA-Z0-9]+)([\s]+)([a-zA-Z ]+)([\s]+)([0-9]+)
         *
         * String: "!* UserName10 John Smith 01123 *!"
         *
         * group(0): UserName10 John Smith 01123
         * group(1): UserName10
         * group(2):
         * group(3): John Smith
         * group(4):
         * group(5): 01123
         *
         * Regex: \\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|([A-Z_a-z][A-Z_a-z0-9]*)|<=|>=|==|&&|\\|\\||\\p{Punct})?
         * ((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|([A-Z_a-z][A-Z_a-z0-9]*)|<=|>=|==|&&|\\|\\||\\p{Punct}): group(1)
         * (//.*): group(2)
         * ([0-9]+): group(3)
         * (\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\"): group(4)
         * ([A-Z_a-z][A-Z_a-z0-9]*)|<=|>=|==|&&|\\|\\||\\p{Punct})： group(5)
         */
        String m = matcher.group(1);

        // 当是空格的时候，直接退出
        if(m == null) {
            return;
        }

        // 当是注释时，直接退出
        if(matcher.group(2) != null) {
            return;
        }

        Token token;
        if(matcher.group(3) != null) {      // 匹配数字
            token = new NumToken(lineNo, Integer.parseInt(m));
        }else if(matcher.group(4) != null) {        // 匹配字符串
            token = new StrToken(lineNo, toStringLiteral(m));
        }else {             // 匹配标识符
            token = new IdToken(lineNo, m);
        }

        queue.add(token);
    }

    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for(int i = 1; i < len; i ++) {     // 这一步相当于是去掉首位字符啊，就是把双引号去掉的意思吧
            char c = s.charAt(i);
            if(c == '\\' && i + 1 < len) {
                char c2 = s.charAt(++ i);
                if(c2 == '\\' || c2 == '"') {
                    c = s.charAt(++ i);
                }else if(c2 == 'n') {
                    ++ i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }


}
