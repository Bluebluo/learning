package data.construre;

import java.util.Iterator;
/*
 * 链表的实现
 */
public class LinkedList<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int N;
	
	public LinkedList() {
		LinkedList<Item> linkedList;
	}
	
	//从表头插入节点
	public void push(Node node) {
		Node oldNode = first;
		first = node;
		first.next = oldNode;
		N++;
	}
	
	//从表头删除节点
	public void pop() {
		first = first.next; 
		N--;
	}
	
	//从表尾插入节点
	public void add(Node node) {
		last.next = node;
		N++;
	}
	
	//链表长度
	public int size() {
		return N;
	}
	
	//链表是否为空
	public boolean isEmpty() {
		return N == 0;
	}
	
	private class Node{
		Item item;
		Node next;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item>{
		private Node current = first;
		public boolean hasNext() {
			return first != null;
		}
		public void remove() {};
		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	
	public static void main(String[] args) {
	}

	
}
