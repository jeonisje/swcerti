package 기출문제.계통수;

public class Test {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		int sum = 0;
		for(int i=0; i<50000; i++) {
			sum += i;
		}
		
		System.out.println(sum);
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}
