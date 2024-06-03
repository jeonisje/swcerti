package 프로원정대추가문제.영상압축._01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[][] map;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String s = st.nextToken();
			for(int j=0; j<N; j++) {
				map[i][j] = s.charAt(j) - '0';
			}
		}


		dc(0, 0, N);
	}
	static void dc(int y, int x, int size) {
		if(check(y, x, size)) {
			System.out.print(map[y][x]);
		} else {
			System.out.print("(");
			int half = size / 2;
			dc(y, x, half);
			dc(y, x + half, half);
			dc(y + half, x, half);
			dc(y + half, x + half, half);
			System.out.print(")");
		}
		
	}
	static boolean check(int y, int x, int size) {
		int cur = map[y][x];
		for(int i=y; i<y+size; i++) {
			for(int j=x; j<x+size; j++) {
				if(cur != map[i][j]) return false;
			}
		}
		
		return true;
	}
}
