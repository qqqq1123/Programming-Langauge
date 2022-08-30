package parser.parse;

import parser.ast.*;
import java.util.*;

public class CuteInterpreter {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("> "); // 입력받기 전 > 출력
			String str = sc.nextLine(); // 프로그램 입력 받기 //String type
			System.out.print("..."); // 결과 출력 전 ... 출력

			CuteParser cuteParser = new CuteParser(str); // 입력받은 문장을 인자로 객체 생성
			CuteInterpreter interpreter = new CuteInterpreter();

			Node parseTree = cuteParser.parseExpr();
			Node resultNode = interpreter.runExpr(parseTree);
			NodePrinter nodePrinter = new NodePrinter(resultNode);
			nodePrinter.prettyPrint();
			System.out.println(); // 개행
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

	static Map<String, Node> hash_map = new HashMap<String, Node>(); // 심볼 테이블

	/* 예를 들어 ( define a 1 ) 이라고 하면, a의 값이 1임을 저장하는 심벌테이블 */
	private void insertTable(String id, Node value) {
		if (hash_map.containsKey(id)) { // 해쉬맵에 id가 존재하는지 검사
			// 이미 존재하는 키라면
			hash_map.remove(id); // 제거 후
			hash_map.put(id, value); // 새로 정의
		} else {
			hash_map.put(id, value); // 없으면 그대로 삽입
		}
		System.out.print("completed"); // define이 잘 정의 되면 완료되었다는 출력문 출력

	}

	/* 해당 변수에 대한 값을 꺼내오는 함수 */
	private Node lookupTable(String id) {

		return hash_map.get(id); // 해당 키에 대한 value를 return
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		Node nd;
		ListNode ln;
		ListNode operand_ln;
		operand_ln = operand;

		switch (operator.funcType) {
		// CAR, CDR, CONS, NULL_Q...등 모든 function node에 대한 동작 구현

		case DEFINE:
			if (operand.cdr().car() instanceof ListNode) { // 저장하고 싶은 값이 리스트인 경우
				//만약 해당 리스트가 연산을 필요로하면 연산된 결과를 define 할 수 있도록 runExpr에 인자로 넘겨서 수행시킴 
				ln = ListNode.cons(runExpr(operand.cdr().car()), ListNode.ENDLIST); 

				insertTable(operand.car().toString(), ln);
				break;

			}
			insertTable(operand.car().toString(), operand.cdr()); // 저장하고 싶은 값이 리스트가 아닌 경우
			break;

		case CAR:

			if (operand_ln.car() instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			nd = ((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car();
			QuoteNode qn = new QuoteNode(); // 쿼트 노드 qn 선언

			if (nd instanceof ListNode) { // 리스트 노드 형태로 리턴되는 경우
				nd = ListNode.cons(nd, ListNode.ENDLIST); // ENDLIST를 붙여주고
				nd = ListNode.cons(qn, (ListNode) nd); // 쿼트를 앞에 붙여줌
			}

			return nd;

		case CDR:

			if (operand_ln.car() instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}
			ln = ((ListNode) ((ListNode) operand_ln.car()).cdr().car()).cdr(); // list의 맨 처음 원소를 제외한 나머지 list

			qn = new QuoteNode(); // 쿼트 노드
			ln = ListNode.cons(ln, ListNode.ENDLIST); // 반환형이 list이므로 ENDLIST를 붙여주고
			ln = ListNode.cons(qn, ln); // 쿼트를 사용했으므로 쿼트도 붙여줌

			return ln;

		case CONS:

			if (operand_ln.car() instanceof IdNode) { // 첫 번째 피연산자가 변수인 경우 테이블에서 변수의 값을 가져옴
				ln = (ListNode) lookupTable(operand_ln.car().toString());
				nd = ln.car();

			} else { // 첫 번째 피연산자가 변수가 아닌 경우
				nd = operand_ln.car();
			}

			ListNode tail = operand_ln.cdr(); // operand의 cdr (쿼트 포함 뒷 부분 리스트 )
			if (tail.car() instanceof IdNode) { // 두 번째 피연산자가 변수인 경우 테이블에서 해당 값 찾아옴
				tail = (ListNode) lookupTable(operand_ln.cdr().car().toString());
			}
			ListNode cons = ListNode.cons(nd, ((ListNode) tail.car()).cdr()); // list의 앞 원소와 뒷 리스트 ( 쿼트 제외
																				// )
																				// cons로 붙여줌
			qn = new QuoteNode(); // 쿼트 노드
			Node head = cons.car(); // operand의 car과 쿼트를 제거한 뒷 부분의 리스트를 합친 리스트의 car

			if (head instanceof ListNode) { // 만약 헤드가 '이면 list이기 때문에 list인지 검사
				head = ((ListNode) (cons.car())).cdr().car(); // ' 뒷 부분을 헤드로 사용
			}
			ln = ListNode.cons(head, (ListNode) cons.cdr().car()); // 결과적으로 봤을 때, 한 개의 원소(head)와 한 개의 리스트(tail)을 붙여서 새로운
																	// 리스트 만듦
			ln = ListNode.cons(ln, ListNode.ENDLIST); // ENDLIST를 붙여주고
			ln = ListNode.cons(qn, ln); // 쿼트를 앞에 붙여줌
			return ln;

		case NULL_Q:

			if (operand_ln.car() instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			if (((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car() == null) { // 리스트 안의 내용이 null이면 true

				return BooleanNode.TRUE_NODE; // return true
			}

			return BooleanNode.FALSE_NODE; // 아니면 return false

		case ATOM_Q:

			if (operand_ln.car() instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}

			if (operand_ln.car() instanceof IntNode || operand_ln.car() instanceof BooleanNode) { // Int 이거나 Bool인 경우
																									// true
				return BooleanNode.TRUE_NODE;
			}

			else {
				if (((ListNode) ((ListNode) operand_ln.car()).cdr()).car() instanceof ListNode) { // 쿼트 뒤의 원소가 리스트인지 검사
					if (((ListNode) ((ListNode) operand_ln.car()).cdr().car()).car() == null) { // null list는 atom이므로
																								// true
						return BooleanNode.TRUE_NODE;
					}
					return BooleanNode.FALSE_NODE; // 리스트인 경우 atom이 아니므로 false 반환
				}
				return BooleanNode.TRUE_NODE; // list가 아닌 경우 atom이므로 return true
			}

		case EQ_Q:

			if (operand_ln.car() instanceof IdNode) { // 첫 번째 비교 대상이 변수인 경우
				Node nd1 = lookupTable(operand_ln.car().toString()); // 테이블에서 첫번째 변수의 값 가져오기
				if (operand_ln.cdr().car() instanceof IdNode) { // 두 번째 비교 대상도 변수인 경우
					Node nd2 = lookupTable(operand_ln.cdr().car().toString()); // 테이블에서 두번째 변수의 값 가져오기
					if (nd1.equals(nd2)) { // 비교
						return BooleanNode.TRUE_NODE; // 같으면 return true node
					}
				}

			}

			else if (operand_ln.car() instanceof IntNode) { // 비교 대상이 Int인 경우
				if (operand_ln.cdr().car() instanceof IntNode) {
					if (operand_ln.car().equals(operand_ln.cdr().car())) { // 두 int 비교
						return BooleanNode.TRUE_NODE; // 같으면 return true
					}
				}
			} else if (operand_ln.car() instanceof BooleanNode) { // 비교 대상이 bool node인 경우
				if (operand_ln.cdr().car() instanceof BooleanNode) {
					if (operand_ln.car().equals(operand_ln.cdr().car())) { // 두 노드 비교
						return BooleanNode.TRUE_NODE; // 같으면 return true
					}
				}
			} else { // 그 외의 것들 비교
				if ((((ListNode) operand_ln.car()).cdr().car())
						.equals(((ListNode) operand_ln.cdr().car()).cdr().car())) {
					// 각각 쿼트 뒤의 두 노드가 같은지 비교
					// 리스트인 경우는 다른 객체를 참조하여 false

					return BooleanNode.TRUE_NODE; // 같으면 return true
				}

			}

			return BooleanNode.FALSE_NODE; // 다르면 return false

		case NOT:
			if (operand_ln.car() instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				operand_ln = (ListNode) lookupTable(operand_ln.car().toString());

			}
			if (operand_ln.car().equals(BooleanNode.TRUE_NODE)) { // #T인 경우
				return BooleanNode.FALSE_NODE; // return false
			}

			else if (operand_ln.car() instanceof ListNode) { // 논리식인 경우(list node)
				if (runBinary((ListNode) operand_ln.car()).equals(BooleanNode.TRUE_NODE)) { // runBinary를 호출하여 관계 연산 수행,
																							// 참인
																							// 경우
					return BooleanNode.FALSE_NODE; // return false
				}
			}

			return BooleanNode.TRUE_NODE; // 위의 if문 안 거치면 참인 조건에 해당하는 것이 없으므로 return true

		case COND:
			Node result;

			if (operand_ln.car() == null) { // operand가 null이면 더 이상 따질 조건이 없으므로 null 리턴
				return null;
			}

			if (!(operand_ln.car() instanceof BooleanNode) && ((ListNode) operand_ln.car()).car() instanceof ListNode) { // 조건식이
				// 관계연산인
				// 경우
				result = this.runBinary(((ListNode) ((ListNode) operand_ln.car()).car())); // 조건식을 runBinary를 통해 결과 도출

				if (result.equals(BooleanNode.TRUE_NODE)) { // 조건식이 참이면

					return ((ListNode) operand_ln.car()).cdr().car(); // 조건식에 대한 조건문의 결과 값 리턴
				} else { // 조건식이 거짓이면
					return runFunction(operator, operand_ln.cdr()); // 다음 조건문에 대한 runFunction 재귀 호출
				}
			}
			nd = ((ListNode) (operand_ln.car())).car();
			if (nd instanceof IdNode) { // operand가 변수인 경우 테이블에서 변수의 값을 가져옴
				nd = ((ListNode) (ListNode) lookupTable(nd.toString())).car();

			}
//			((ListNode) (operand_ln.car())).car()
			if (nd instanceof BooleanNode) { // 조건이 #T 또는 #F인 경우
				if (nd.equals(BooleanNode.TRUE_NODE)) { // 조건이 #T인 경우
					return ((ListNode) operand_ln.car()).cdr().car(); // 조건식에 대한 조건문의 결과 값 리턴
				} else { // 조건이 #F인 경우
					return runFunction(operator, operand_ln.cdr()); // 다음 조건문에 대한 runFunction 재귀 호출
				}
			}

		default:
			break;

		}
		return null;
	}

	private Node runBinary(ListNode list) {

		BinaryOpNode operator = (BinaryOpNode) list.car(); // list의 operator 저장

		Node nd1 = list.cdr().car(); // 첫 번째 operand
		Node nd2 = list.cdr().cdr().car(); // 두 번째 operand

		if (nd1 instanceof IdNode) {
			nd1 = lookupTable(nd1.toString());
			nd1 = ((ListNode) nd1).car();

		}
		if (nd2 instanceof IdNode) {
			nd2 = lookupTable(nd2.toString());
			nd2 = ((ListNode) nd2).car();

		}
		if (nd1 instanceof ListNode) { // 첫 번째 operand가 리스트인지 검사(수식인지 검사)
			nd1 = runBinary((ListNode) nd1); // 리스트이면 먼저 연산을 해서 값을 얻어야하기 때문에 nd1에 대해 재귀호출
		}

		if (nd2 instanceof ListNode) { // 두 번째 operand가 리스트인지 검사(수식인지 검사)
			nd2 = runBinary((ListNode) nd2); // 리스트이면 먼저 연산을 해서 값을 얻어야하기 때문에 nd2에 대해 재귀호출
		}

		/* 각각의 operand의 value를 Integer로 저장 */
		Integer i = ((IntNode) nd1).getValue();
		Integer j = ((IntNode) nd2).getValue();

		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.binType) { // +,-,*,=등 모든binarynode에 대한 동작 구현
		// +,-,/ 등에 대한 바이너리 연산 동작 구현

		/* 산술연산 - 연산 값: int */
		case PLUS:
			return new IntNode(Integer.toString(i + j)); // 더하기 연산 후 IntNode로 반환
		case MINUS:
			return new IntNode(Integer.toString(i - j)); // 뺄셈 연산 후 IntNode로 반환
		case DIV:
			return new IntNode(Integer.toString(i / j)); // 나누기 연산 후 IntNode로 반환
		case TIMES:
			return new IntNode(Integer.toString(i * j)); // 곱하기 연산 후 IntNode로 반환

		/* 관계연산 - 연산 값: boolean */
		case LT:
			if (i < j) { // 첫 번째 operand가 두 번째 operand보다 작으면 true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // 아니면 false

		case GT:
			if (i > j) { // 첫 번째 operand가 두 번째 operand보다 크면 true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // 아니면 false

		case EQ:
			if (i.equals(j)) { // 두 operand가 서로 같으면 true
				return BooleanNode.TRUE_NODE;
			}
			return BooleanNode.FALSE_NODE; // 아니면 false

		default:
			break;
		}
		return null;
	}
}
