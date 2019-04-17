package data.construre;
//链表实现队列
public class QueueAbs<Item> {

	private Node node;
	
	private Node first;
	private Node last;
	private int n;
	private class Node{
		Item item;
		Node next;
	}
	
	public boolean isEmpty() {
		return first == null;
	}
	
	public void push(Item item) {
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		if(isEmpty()) first=last;
		else 			oldlast.next = last;
		n++;
//		System.out.println("last======="+last.item);
//		System.out.println("first======="+first.item);
//		System.out.println(n);
	}
	int i=1;
	public void pop(int k) {
		for(Node x =first;x!=null;x = x.next,i++) {
			if(i==k) {
//				System.out.println("i am going to pop"+x.item+" position "+i);
				x.next = x.next.next;
			}
		}
	}
	
	public Item pop() {
		Node oldfirst = first;
		first = first.next;
		//first.next = null;
//		System.out.println("the pop======"+first.item);
//		System.out.println("now the first ======="+first.item);
		return oldfirst.item;
	}
	
	public static void main(String[] args) {
		QueueAbs<String> test = new QueueAbs<>();
		test.push("sdf");
		test.push("31");
		test.push("65161");
		test.push("sdfsdfsdfsdf");
		test.push("*******");
		test.pop(3);
	}
}

