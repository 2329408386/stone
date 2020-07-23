package stone;

public abstract class Token {
    public static final Token  EOF = new Token(-1) {};
    public static final String EOL = "\\n";
    private int lineNumber;


    protected Token(int line) {
        lineNumber = line;
    }

    public boolean isIdentifier() { return false; }
    public boolean isNumber() { return false; }
    public boolean isString() { return false; }
    public int getLineNumber() { return lineNumber; }
    public int getNumber() { throw new TokenException("not a number"); }
    public String getText() { return ""; }
}