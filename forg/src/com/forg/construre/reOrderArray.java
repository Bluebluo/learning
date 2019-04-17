package data.construre;

public class reOrderArray {

	public static void main(String[] args) {
		int[] arr = {1,2,3,4,5,6,7,8,9};
		Stack<Integer> stack1 = new Stack<>();
		Stack<Integer> stack2 = new Stack<>();
		for(int a: arr) {
			if(a%2==0) {
				stack1.push(a);
			}else {
				stack2.push(a);
			}
		}
		
	}
}
