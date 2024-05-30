package 프로원정대추가문제.level꺄지재귀동작;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		recursive(0);
	}
	
	static void recursive(int level) {
		if(level > N) return;
		System.out.print(level);
		
		recursive(level + 1);	
		recursive(level + 1);
	}
}
