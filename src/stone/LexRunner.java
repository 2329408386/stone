package stone;

/**
 * @author lichengpeng
 * @desc 测试词法分析器
 * @date 2020/7/14
 **/
public class LexRunner {
    public static void main(String[] args) throws ParseException{
        Lexer lexer = new Lexer(new CodeDialog());
        for(Token t; (t = lexer.read()) != Token.EOF; ) {
            System.out.println("=>" + t.getText());
        }
    }
}
