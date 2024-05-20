package 프로원정대.day5.binarySearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	
	static int[] array;
	static int N;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());		
		N = Integer.parseInt(st.nextToken());
		array = new int[N];
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			array[i] = Integer.parseInt(st.nextToken());
		}
		
		Arrays.sort(array);
		
		st = new StringTokenizer(br.readLine());		
		int K = Integer.parseInt(st.nextToken());
		//int[] targets = new int[K]; 
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<K; i++) {
			int target = Integer.parseInt(st.nextToken());
			binarySearch(target);
		}
	}
	
	static void binarySearch(int target) {
		int start = 0;
		int end = array.length - 1;
		
		while(start <= end) {
			int mid = (start + end) / 2;
			
			if(array[mid] == target) {
				System.out.print("O");
				return;
			} else if (array[mid] < target) {
				start = mid + 1;				
			} else {
				end = mid - 1;
			}
			
			
		}
		System.out.print("X");
	}
}	
