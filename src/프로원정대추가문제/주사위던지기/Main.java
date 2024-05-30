package 프로원정대추가문제.주사위던지기;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, M;
	static int[] arr;
	static int[] visited;
 	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		arr = new int[N];
		visited = new int[7];
		
		if(M == 1) {
			recursive1(0);
		} else if(M == 2) {
			recursive2(0);
		} else if(M == 3) {
			recursive3(0);
		}
	}

	

	static void recursive1(int level) {
		if(level >= N) {
			for(int i=0; i<N; i++) {
				System.out.print(arr[i] +  " ");
			}
			System.out.println();
			return;
		}
		
		for(int i=1; i<=6; i++) {
			arr[level] = i;
			recursive1(level+1);
			arr[level] = 0;
		}	
	}

	static void recursive2(int level) {
		if(level >= N) {
			for(int i=0; i<N; i++) {
				System.out.print(arr[i] +  " ");
			}
			System.out.println();
			return;
		}
		
		for(int i=1; i<=6; i++) {
			if(level >= 1 && i < arr[level - 1]) continue;
			arr[level] = i;
			recursive2(level + 1);
			arr[level] = 0;
		}
		
	}
	
	static void recursive3(int level) {
		if(level >= N) {
			for(int i=0; i<N; i++) {
				System.out.print(arr[i] +  " ");
			}
			System.out.println();
			return;
		}
		
		for(int i=1; i<=6; i++) {
			if(visited[i] == 1) continue;
			arr[level] = i;
			visited[i] = 1;
			recursive3(level + 1);
			arr[level] = 0;
			visited[i] = 0;
		}
		
	}
	
}
