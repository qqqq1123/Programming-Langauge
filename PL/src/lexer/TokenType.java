package lexer;


public enum TokenType {
	INT,
	ID, 
	TRUE, FALSE, NOT,
	PLUS, MINUS, TIMES, DIV,   //special character 
	LT, GT, EQ, APOSTROPHE,    //special character
	L_PAREN, R_PAREN,QUESTION, //special character 
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS,
	ATOM_Q, NULL_Q, EQ_Q; 
	
	static TokenType fromSpecialCharactor(char ch) {
		switch ( ch ) {
			case '+':  // '+'인 경우에 PLUS 토큰타입 반환 
				return PLUS;
			//나머지 Special Character에 대해 토큰을 반환하도록 작성 
				
			case '-':  // '-'인 경우에 MINUS 토큰타입 반환 
				return MINUS;
			case '*': // '*'인 경우에 TIMES 토큰타입 반환 
				return TIMES;
			case '/': // '/'인 경우에 DIV 토큰타입 반환 
				return DIV;
			case '<': // '<'인 경우에 LT 토큰타입 반환 
				return LT;
			case '>': // '>'인 경우에 GT 토큰타입 반환 
				return GT;
			case '=': // '='인 경우에 EQ 토큰타입 반환 
				return EQ;
			case '\'': // '''인 경우에 APOSTROPHE 토큰타입 반환 
				return APOSTROPHE;
			case '(': // '('인 경우에 L_PAREN 토큰타입 반환 
				return L_PAREN;
			case ')': // ')'인 경우에 R_PAREN 토큰타입 반환 
				return R_PAREN;
			case '?': // '?'인 경우에 QUESTION 토큰타입 반환  
				return QUESTION;
			default:
				throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
