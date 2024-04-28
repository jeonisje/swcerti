package training1.lv16_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N = 4;
	static int[] arrA = new int[N];
	static int[] arrB = new int[N];

	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			arrA[i] = Integer.parseInt(st.nextToken());
		}
		
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			arrB[i] = Integer.parseInt(st.nextToken());
		}
		
		for(int i=0; i<N; i++) {
			int result = arrA[i] + arrB[N-i-1];
			System.out.print(result + " ");
		}
	}
}
