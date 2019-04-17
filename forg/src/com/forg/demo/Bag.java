package data.construre;

import java.util.Iterator;

//通过实现iterable，使用泛型和迭代
//public interface Iterable<T>   #Iterable本身支持泛型
public class Bag<Item> implements Iterable<Item>{

	private Node first;
	
	//根据Bag的特性，编写api
	public Bag() {
		Bag<Item> bag;
	} 
	
	public void add(Item item) {
		Node oldNode = first;
		first = new Node();
		first.item = item;
		first.next = oldNode;
	}
	
	@Override
	public Iterator<Item> iterator() {
		return new ListIterator();
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
	
	public class Node{
		Item item;
		Node next;
	}
	
	
}