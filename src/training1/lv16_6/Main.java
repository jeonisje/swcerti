package training1.lv16_6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = 6;
		int[] arr = new int[N];
		
		
		for(int i=0; i<N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());			
		}		
		
		int sum = arr[0];
		System.out.print(sum + " ");
		
		for(int i=1; i<N; i++) {			
			sum += 	arr[i];
			System.out.print(sum + " ");
		}
	}
	
}
