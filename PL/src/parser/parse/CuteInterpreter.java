package parser.parse;

import parser.ast.*;
import java.util.*;

public class CuteInterpreter {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("> "); // �Է¹ޱ� �� > ���
			String str = sc.nextLine(); // ���α׷� �Է� �ޱ� //String type
			System.out.print("..."); // ��� ��� �� ... ���

			CuteParser cuteParser = new CuteParser(str); // �Է¹��� ������ ���ڷ� ��ü ����
			CuteInterpreter interpreter = new CuteInterpreter();

			Node parseTree = cuteParser.parseExpr();
			Node resultNode = interpreter.runExpr(parseTree);
			NodePrinter nodePrinter = new NodePrinter(resultNode);
			nodePrinter.prettyPrint();
			System.out.println(); // ����
		}

	}

	private void errorLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return null;
	}

	private Node stripList(ListNode node) {
		if (node.car() instanceof ListNode && node.cdr().car() == null) {
			Node listNode = node.car();
			return listNode;
		} else {
			return node;
		}
	}

	private Node runList(ListNode list) {

		list = (ListNode) stripList(list);
		if (list.equals(ListNode.EMPTYLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			if (false)
				return list;
			else {
				return runFunction((FunctionNode) list.car(), list.cdr());
			}
		}
		if (list.car() instanceof BinaryOpNode) {
			if (false)
				return list;
			else
				return runBinary(list);
		}
		return list;
	}

	static Map<String, Node> hash_map = new HashMap<String, Node>(); // �ɺ� ���̺�

	/* ���� ��� ( define a 1 ) �̶�� �ϸ�, a�� ���� 1���� �����ϴ� �ɹ����̺� */
	private void insertTable(String id, Node value) {
		if (hash_map.containsKey(id)) { // �ؽ��ʿ� id�� �����ϴ��� �˻�
			// �̹� �����ϴ� Ű���
			hash_map.remove(id); // ���� ��
			hash_map.put(id, value); // ���� ����
		} else {
			hash_map.put(id, value); // ������ �״�� ����
		}
		System.out.print("completed"); // define�� �� ���� �Ǹ� �Ϸ�Ǿ��ٴ� ��¹� ���

	}

	/* �ش� ������ ���� ���� �������� �Լ� */
	private Node lookupTable(String id) {

		return hash_map.get(id); // �ش� Ű�� ���� value�� return
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		Node nd;
		ListNode ln;
		ListNode operand_ln;
		operand_ln = operand;

		switch (operator.funcType) {
		// CAR, CDR, CONS, NULL_Q...�� ��� function node�� ���� ���� ����

		case DEFINE:
			if (operand.cdr().car() instanceof ListNode) { // �����ϰ� ���� ���� ����Ʈ�� ���
				//���� �ش� ����Ʈ�� ������ �ʿ���ϸ� ����� ����� define �� �� �ֵ��� runExpr�� ���ڷ� �Ѱܼ� �����Ŵ 
				ln = ListNode.cons(runExpr(operand.cdr().car()), ListNode.ENDLIST); 

				insertTable(operand.car().toString(), ln);
				break;

			}
			insertTable(operand.car().toString(), operand.cdr()); // �����ϰ� ���� ���� ����Ʈ�� �ƴ� ���
			break;

		case CAR:

			if (operand_ln.car() instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			nd = ((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car();
			QuoteNode qn = new QuoteNode(); // ��Ʈ ��� qn ����

			if (nd instanceof ListNode) { // ����Ʈ ��� ���·� ���ϵǴ� ���
				nd = ListNode.cons(nd, ListNode.ENDLIST); // ENDLIST�� �ٿ��ְ�
				nd = ListNode.cons(qn, (ListNode) nd); // ��Ʈ�� �տ� �ٿ���
			}

			return nd;

		case CDR:

			if (operand_ln.car() instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}
			ln = ((ListNode) ((ListNode) operand_ln.car()).cdr().car()).cdr(); // list�� �� ó�� ���Ҹ� ������ ������ list

			qn = new QuoteNode(); // ��Ʈ ���
			ln = ListNode.cons(ln, ListNode.ENDLIST); // ��ȯ���� list�̹Ƿ� ENDLIST�� �ٿ��ְ�
			ln = ListNode.cons(qn, ln); // ��Ʈ�� ��������Ƿ� ��Ʈ�� �ٿ���

			return ln;

		case CONS:

			if (operand_ln.car() instanceof IdNode) { // ù ��° �ǿ����ڰ� ������ ��� ���̺��� ������ ���� ������
				ln = (ListNode) lookupTable(operand_ln.car().toString());
				nd = ln.car();

			} else { // ù ��° �ǿ����ڰ� ������ �ƴ� ���
				nd = operand_ln.car();
			}

			ListNode tail = operand_ln.cdr(); // operand�� cdr (��Ʈ ���� �� �κ� ����Ʈ )
			if (tail.car() instanceof IdNode) { // �� ��° �ǿ����ڰ� ������ ��� ���̺��� �ش� �� ã�ƿ�
				tail = (ListNode) lookupTable(operand_ln.cdr().car().toString());
			}
			ListNode cons = ListNode.cons(nd, ((ListNode) tail.car()).cdr()); // list�� �� ���ҿ� �� ����Ʈ ( ��Ʈ ����
																				// )
																				// cons�� �ٿ���
			qn = new QuoteNode(); // ��Ʈ ���
			Node head = cons.car(); // operand�� car�� ��Ʈ�� ������ �� �κ��� ����Ʈ�� ��ģ ����Ʈ�� car

			if (head instanceof ListNode) { // ���� ��尡 '�̸� list�̱� ������ list���� �˻�
				head = ((ListNode) (cons.car())).cdr().car(); // ' �� �κ��� ���� ���
			}
			ln = ListNode.cons(head, (ListNode) cons.cdr().car()); // ��������� ���� ��, �� ���� ����(head)�� �� ���� ����Ʈ(tail)�� �ٿ��� ���ο�
																	// ����Ʈ ����
			ln = ListNode.cons(ln, ListNode.ENDLIST); // ENDLIST�� �ٿ��ְ�
			ln = ListNode.cons(qn, ln); // ��Ʈ�� �տ� �ٿ���
			return ln;

		case NULL_Q:

			if (operand_ln.car() instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			if (((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car() == null) { // ����Ʈ ���� ������ null�̸� true

				return BooleanNode.TRUE_NODE; // return true
			}

			return BooleanNode.FALSE_NODE; // �ƴϸ� return false

		case ATOM_Q:

			if (operand_ln.car() instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			if (operand_ln.car() instanceof IntNode || operand_ln.car() instanceof BooleanNode) { // Int �̰ų� Bool�� ���
																									// true
				return BooleanNode.TRUE_NODE;
			}

			else {
				if (((ListNode) ((ListNode) operand_ln.car()).cdr()).car() instanceof ListNode) { // ��Ʈ ���� ���Ұ� ����Ʈ���� �˻�
					if (((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car() == null) { // null list�� atom�̹Ƿ�
																								// true
						return BooleanNode.TRUE_NODE;
					}
					return BooleanNode.FALSE_NODE; // ����Ʈ�� ��� atom�� �ƴϹǷ� false ��ȯ
				}
				return BooleanNode.TRUE_NODE; // list�� �ƴ� ��� atom�̹Ƿ� return true
			}

		case EQ_Q:

			if (operand_ln.car() instanceof IdNode) { // ù ��° �� ����� ������ ���
				Node nd1 = lookupTable(operand_ln.car().toString()); // ���̺��� ù��° ������ �� ��������
				if (operand_ln.cdr().car() instanceof IdNode) { // �� ��° �� ��� ������ ���
					Node nd2 = lookupTable(operand_ln.cdr().car().toString()); // ���̺��� �ι�° ������ �� ��������
					if (nd1.equals(nd2)) { // ��
						return BooleanNode.TRUE_NODE; // ������ return true node
					}
				}

			}

			else if (operand_ln.car() instanceof IntNode) { // �� ����� Int�� ���
				if (operand_ln.cdr().car() instanceof IntNode) {
					if (operand_ln.car().equals(operand_ln.cdr().car())) { // �� int ��
						return BooleanNode.TRUE_NODE; // ������ return true
					}
				}
			} else if (operand_ln.car() instanceof BooleanNode) { // �� ����� bool node�� ���
				if (operand_ln.cdr().car() instanceof BooleanNode) {
					if (operand_ln.car().equals(operand_ln.cdr().car())) { // �� ��� ��
						return BooleanNode.TRUE_NODE; // ������ return true
					}
				}
			} else { // �� ���� �͵� ��
				if ((((ListNode) operand_ln.car()).cdr().car())
						.equals(((ListNode) operand_ln.cdr().car()).cdr().car())) {
					// ���� ��Ʈ ���� �� ��尡 ������ ��
					// ����Ʈ�� ���� �ٸ� ��ü�� �����Ͽ� false

					return BooleanNode.TRUE_NODE; // ������ return true
				}

			}

			return BooleanNode.FALSE_NODE; // �ٸ��� return false

		case NOT:
			if (operand_ln.car() instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}
			if (operand_ln.car().equals(BooleanNode.TRUE_NODE)) { // #T�� ���
				return BooleanNode.FALSE_NODE; // return false
			}

			else if (operand_ln.car() instanceof ListNode) { // ������ ���(list node)
				if (runBinary((ListNode) operand_ln.car()).equals(BooleanNode.TRUE_NODE)) { // runBinary�� ȣ���Ͽ� ���� ���� ����,
																							// ����
																							// ���
					return BooleanNode.FALSE_NODE; // return false
				}
			}

			return BooleanNode.TRUE_NODE; // ���� if�� �� ��ġ�� ���� ���ǿ� �ش��ϴ� ���� �����Ƿ� return true

		case COND:
			Node result;

			if (operand_ln.car() == null) { // operand�� null�̸� �� �̻� ���� ������ �����Ƿ� null ����
				return null;
			}

			if (!(operand_ln.car() instanceof BooleanNode) && ((ListNode) operand_ln.car()).car() instanceof ListNode) { // ���ǽ���
				// ���迬����
				// ���
				result = this.runBinary(((ListNode) ((ListNode) operand_ln.car()).car())); // ���ǽ��� runBinary�� ���� ��� ����

				if (result.equals(BooleanNode.TRUE_NODE)) { // ���ǽ��� ���̸�

					return ((ListNode) operand_ln.car()).cdr().car(); // ���ǽĿ� ���� ���ǹ��� ��� �� ����
				} else { // ���ǽ��� �����̸�
					return runFunction(operator, operand_ln.cdr()); // ���� ���ǹ��� ���� runFunction ��� ȣ��
				}
			}
			nd = ((ListNode) (operand_ln.car())).car();
			if (nd instanceof IdNode) { // operand�� ������ ��� ���̺��� ������ ���� ������
				nd = ((ListNode) (ListNode) lookupTable(nd.toString())).car();

			}
//			((ListNode) (operand_ln.car())).car()
			if (nd instanceof BooleanNode) { // ������ #T �Ǵ� #F�� ���
				if (nd.equals(BooleanNode.TRUE_NODE)) { // ������ #T�� ���
					return ((ListNode) operand_ln.car()).cdr().car(); // ���ǽĿ� ���� ���ǹ��� ��� �� ����
				} else { // ������ #F�� ���
					return runFunction(operator, operand_ln.cdr()); // ���� ���ǹ��� ���� runFunction ��� ȣ��
				}
			}

		default:
			break;

		}
		return null;
	}

	private Node runBinary(ListNode list) {

		BinaryOpNode operator = (BinaryOpNode) list.car(); // list�� operator ����

		Node nd1 = list.cdr().car(); // ù ��° operand
		Node nd2 = list.cdr().cdr().car(); // �� ��° operand

		if (nd1 instanceof IdNode) {
			nd1 = lookupTable(nd1.toString());
			nd1 = ((ListNode) nd1).car();

		}
		if (nd2 instanceof IdNode) {
			nd2 = lookupTable(nd2.toString());
			nd2 = ((ListNode) nd2).car();

		}
		if (nd1 instanceof ListNode) { // ù ��° operand�� ����Ʈ���� �˻�(�������� �˻�)
			nd1 = runBinary((ListNode) nd1); // ����Ʈ�̸� ���� ������ �ؼ� ���� �����ϱ� ������ nd1�� ���� ���ȣ��
		}

		if (nd2 instanceof ListNode) { // �� ��° operand�� ����Ʈ���� �˻�(�������� �˻�)
			nd2 = runBinary((ListNode) nd2); // ����Ʈ�̸� ���� ������ �ؼ� ���� �����ϱ� ������ nd2�� ���� ���ȣ��
		}

		/* ������ operand�� value�� Integer�� ���� */
		Integer i = ((IntNode) nd1).getValue();
		Integer j = ((IntNode) nd2).getValue();

		// ������������ �ʿ��� ���� �� �Լ� �۾� ����
		switch (operator.binType) { // +,-,*,=�� ���binarynode�� ���� ���� ����
		// +,-,/ � ���� ���̳ʸ� ���� ���� ����

		/* ������� - ���� ��: int */
		case PLUS:
			return new IntNode(Integer.toString(i + j)); // ���ϱ� ���� �� IntNode�� ��ȯ
		case MINUS:
			return new IntNode(Integer.toString(i - j)); // ���� ���� �� IntNode�� ��ȯ
		case DIV:
			return new IntNode(Integer.toString(i / j)); // ������ ���� �� IntNode�� ��ȯ
		case TIMES:
			return new IntNode(Integer.toString(i * j)); // ���ϱ� ���� �� IntNode�� ��ȯ

		/* ���迬�� - ���� ��: boolean */
		case LT:
			if (i < j) { // ù ��° operand�� �� ��° operand���� ������ true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // �ƴϸ� false

		case GT:
			if (i > j) { // ù ��° operand�� �� ��° operand���� ũ�� true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // �ƴϸ� false

		case EQ:
			if (i.equals(j)) { // �� operand�� ���� ������ true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // �ƴϸ� false

		default:
			break;
		}
		return null;
	}
}
