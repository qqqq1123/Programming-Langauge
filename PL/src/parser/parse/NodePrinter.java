package parser.parse;

import java.io.FileWriter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
	
	private Node root;

	public NodePrinter(Node root) {
		this.root = root;
	}

	/* listNode에 대한 출력처리 함수 */
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) { // EMPTYLIST이면 빈 리스트를 sb에 append 후 함수 종료
			System.out.print("( )"); // 빈 괄호 출력 
			return;
		}
		if (listNode == ListNode.ENDLIST) { // ENDLIST이면 함수 종료
			return;
		}

		if (!(listNode.car() instanceof QuoteNode)) { // head가 QuoteNode 인지 검사 => QuoteNode가 아니면
			printNode(listNode.car()); // 원소의 맨 앞 부분을 인자로 printNode 호출

		}

		if (!(listNode.cdr().car() instanceof QuoteNode)) { // tail의 head가 QuoteNode 인지 검사 => QuoteNode가 아니면
			printList(listNode.cdr());// cdr은 list이므로 tail을 인자로 주어 printList 호출
		}

	}

	/* 입력으로 들어온 노드들에 대한 출력처리 함수 */
	private void printNode(Node node) {
		if (node == null) /* 입력으로 들어온 node가 null이면 함수 종료 */
			return;

		if (node instanceof ListNode) { /* node가 리스트인지 검사 */
			ListNode ln = (ListNode) node; // ListNode ln에 ListNode로 node 저장

			if (ln.car() instanceof QuoteNode) { // 리스트노드인데, head가 QuoteNode 이면
				System.out.print(ln.car()); // head 콘솔 출력 
				printList(ln.cdr()); // tail을 인자로 printList 호출

			}

			else { // 리스트노드인데, head가 QuoteNode가 아닌 경우
				System.out.print("( "); // 괄호 열어주기 + 띄어 쓰기
				printList(ln); // 리스트노드를 인자로 printList 호출
				System.out.print(") "); // 괄호 닫아주기 + 띄어 쓰기
			}
		}

		else { /* node가 리스트가 아닌 경우 */
			System.out.print("[" + node + "]"); // node 콘솔 출력 
			System.out.print(" "); // 띄어 쓰기
		}

	}

	public void prettyPrint() {
		printNode(root);

	}
	
}