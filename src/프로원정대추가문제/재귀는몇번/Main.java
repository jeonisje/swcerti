package 프로원정대추가문제.재귀는몇번;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, M;
	static int count = 0;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		recursive(0);
		System.out.println(count);
	}
	
	static void recursive(int level) {
		if(level > M) return;
		count++;
		for(int i=0; i<N; i++)
			recursive(level + 1);	
	}
}
