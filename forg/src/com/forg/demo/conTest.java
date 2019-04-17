package data.construre;

public class conTest {

	public conTest() {
		System.out.println("构造方法");
	}
	
	public static void test() {
		System.out.println("某种静态方法");
	}
	
	public static void main(String[] args) {
		conTest t = new conTest();
		test();
	}
}
