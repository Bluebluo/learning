package data.construre;

public class tryyy {
	public static void main(String[] args) {
		
		
		System.out.println(1);
		try {
			throw new Exception("err");
		}catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e);
		}
		System.out.println(2);
	}

}
