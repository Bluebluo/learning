package data.construre;

import java.util.Iterator;
public class Stack<Item> implements Iterable<Item>{

	private Node first;
	private int N;
	
	public Stack() {
		Stack<Item> stack;
	}
	
	@Override
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
	private class Node{
		Item item;
		Node next;
	}
	
	private class ListIterator implements Iterator<Item>{
		private Node current = first;
		public boolean hasNext() {
			return current != null;
		}
		public void remove() {};
		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	
	public void push(Item item) {
		Node node = new Node();
		node.next = first;
		node.item = item;
		first = node;
	}
	
	public Item pop() {
		Item item = first.item;
		first.next = null;
		first = first.next;
		return item;
	}

}
