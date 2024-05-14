package 프로원정대.day2.연쇄폭탄;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		int[] directY = {-1, 1, 0, 0};
		int[] directX = {0, 0, -1, 1};
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		int size = N * N + 1;		
		Node[] bomb = new Node[size];
		int[] exploded = new int[size];
		
		int count = N * N;
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				int num = Integer.parseInt(st.nextToken());;
				map[i][j] = num;
				
				Node node = new Node(i, j);
				bomb[num] = node;
			}
		}
		
		int ans = 0;
		for(int k=1; k<size; k++) {
			if(exploded[k] == 1) continue;
			
			exploded[k]= 1;
			count--;
			for(int i=0; i<4; i++) {
				int ny = bomb[k].y + directY[i];
				int nx = bomb[k].x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;				
				if(exploded[map[ny][nx]] == 1) continue;
				
				exploded[map[ny][nx]] = 1;
				count--;
			}
			
			if(count == 0) {
				ans = k;
				break;
			}
		}
		
		System.out.println(ans + "초");
		return;
		
		
	}
	
	static class Node {
		int y;
		int x;
		public Node(int y, int x) {
			this.y = y;
			this.x = x;
		}
		
	}
}
