package stone;
import stone.ast.ASTree;

public class TokenException extends RuntimeException {
    public TokenException(String m) {
        super(m);
    }

    public TokenException(String m, ASTree t) {
        super(m + " " + t.location());
    }
}