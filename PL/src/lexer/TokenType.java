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
			case '+':  // '+'�� ��쿡 PLUS ��ūŸ�� ��ȯ 
				return PLUS;
			//������ Special Character�� ���� ��ū�� ��ȯ�ϵ��� �ۼ� 
				
			case '-':  // '-'�� ��쿡 MINUS ��ūŸ�� ��ȯ 
				return MINUS;
			case '*': // '*'�� ��쿡 TIMES ��ūŸ�� ��ȯ 
				return TIMES;
			case '/': // '/'�� ��쿡 DIV ��ūŸ�� ��ȯ 
				return DIV;
			case '<': // '<'�� ��쿡 LT ��ūŸ�� ��ȯ 
				return LT;
			case '>': // '>'�� ��쿡 GT ��ūŸ�� ��ȯ 
				return GT;
			case '=': // '='�� ��쿡 EQ ��ūŸ�� ��ȯ 
				return EQ;
			case '\'': // '''�� ��쿡 APOSTROPHE ��ūŸ�� ��ȯ 
				return APOSTROPHE;
			case '(': // '('�� ��쿡 L_PAREN ��ūŸ�� ��ȯ 
				return L_PAREN;
			case ')': // ')'�� ��쿡 R_PAREN ��ūŸ�� ��ȯ 
				return R_PAREN;
			case '?': // '?'�� ��쿡 QUESTION ��ūŸ�� ��ȯ  
				return QUESTION;
			default:
				throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
