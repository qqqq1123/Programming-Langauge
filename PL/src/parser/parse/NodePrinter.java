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

	/* listNode�� ���� ���ó�� �Լ� */
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) { // EMPTYLIST�̸� �� ����Ʈ�� sb�� append �� �Լ� ����
			System.out.print("( )"); // �� ��ȣ ��� 
			return;
		}
		if (listNode == ListNode.ENDLIST) { // ENDLIST�̸� �Լ� ����
			return;
		}

		if (!(listNode.car() instanceof QuoteNode)) { // head�� QuoteNode ���� �˻� => QuoteNode�� �ƴϸ�
			printNode(listNode.car()); // ������ �� �� �κ��� ���ڷ� printNode ȣ��

		}

		if (!(listNode.cdr().car() instanceof QuoteNode)) { // tail�� head�� QuoteNode ���� �˻� => QuoteNode�� �ƴϸ�
			printList(listNode.cdr());// cdr�� list�̹Ƿ� tail�� ���ڷ� �־� printList ȣ��
		}

	}

	/* �Է����� ���� ���鿡 ���� ���ó�� �Լ� */
	private void printNode(Node node) {
		if (node == null) /* �Է����� ���� node�� null�̸� �Լ� ���� */
			return;

		if (node instanceof ListNode) { /* node�� ����Ʈ���� �˻� */
			ListNode ln = (ListNode) node; // ListNode ln�� ListNode�� node ����

			if (ln.car() instanceof QuoteNode) { // ����Ʈ����ε�, head�� QuoteNode �̸�
				System.out.print(ln.car()); // head �ܼ� ��� 
				printList(ln.cdr()); // tail�� ���ڷ� printList ȣ��

			}

			else { // ����Ʈ����ε�, head�� QuoteNode�� �ƴ� ���
				System.out.print("( "); // ��ȣ �����ֱ� + ��� ����
				printList(ln); // ����Ʈ��带 ���ڷ� printList ȣ��
				System.out.print(") "); // ��ȣ �ݾ��ֱ� + ��� ����
			}
		}

		else { /* node�� ����Ʈ�� �ƴ� ��� */
			System.out.print("[" + node + "]"); // node �ܼ� ��� 
			System.out.print(" "); // ��� ����
		}

	}

	public void prettyPrint() {
		printNode(root);

	}
	
}