package 멘토링권장문제.전국통일;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	//static int MAX = 100_001;
	static int N, K;
	static int[][] map;
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	
	static int[] parent;
	
	static int sequence;
	static int total;
		
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		map = new int[N+1][N+1];
		
		
	}
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		parent[pb] = pa;
	}
	
	static class Node {
		int id;
		int row;
		int col;
		public Node(int id, int row, int col) {		
			this.id = id;
			this.row = row;
			this.col = col;
		}
	}
}
