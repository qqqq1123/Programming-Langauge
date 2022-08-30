package lexer;

import java.io.File;
import java.io.FileNotFoundException;


class ScanContext {
	private final CharStream input;
	private StringBuilder builder;
	
	ScanContext(String str) throws FileNotFoundException { //File이 아닌 String에 대한 생성자 
		this.input = CharStream.from(str);
		this.builder = new StringBuilder();
	}
	
	CharStream getCharStream() {
		return input;
	}
	
	String getLexime() {
		String str = builder.toString();
		builder.setLength(0);
		return str;
	}
	
	void append(char ch) {
		builder.append(ch);
	}
}
