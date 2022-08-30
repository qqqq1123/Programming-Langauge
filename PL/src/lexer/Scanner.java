package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator //�Ķ���Ͱ� File�� �ƴ� String 
    public static Iterator<Token> scan(String str) throws FileNotFoundException { 
        ScanContext context = new ScanContext(str);
        return new TokenIterator(context);
    }

    // return tokens as a Stream  //�Ķ���Ͱ� File�� �ƴ� String
    public static Stream<Token> stream(String str) throws FileNotFoundException {
        Iterator<Token> tokens = scan(str);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}