package parser.ast;

import java.util.Objects;

public class IntNode extends QuotableNodeImpl implements ValueNode {
    private Integer value;

    public IntNode(String text) {
        this.value = new Integer(text);
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (!(o instanceof IntNode)) return false; 
    	IntNode intNode = (IntNode) o;
    	
    	return Objects.equals(value, intNode.value);
    }
    
    public Integer getValue() {
    	return value;
    }
}
