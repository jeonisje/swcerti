package 멘토링권장문제.전국통일;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	//static int MAX = 100_001;
	static int N, K;
	static int[][] map;
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	
	static ArrayList<Node>[] locations;
	
	static int[] parent;
	
	static int sequence;
	static int total;
		
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		sequence = 0;
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N+1][N+1];
		parent = new int[K+1];
		locations = new ArrayList[K+1];
		for(int i=0; i<K+1; i++) {
			parent[i] = i;
			locations[i] = new ArrayList<>();
		}
		
		for(int i=0; i<K; i++) {
			st = new StringTokenizer(br.readLine());
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			
			sequence++;
			map[row][col] = sequence;
			locations[sequence].add(new Node(row, col));
			total++;
			
			for(int k=0; k<4; k++) {
				int nr = row + directY[k];
				int nc = col + directX[k];
				
				if(nr < 1 | nc < 1 || nr >= N+1 || nc >= N+1) continue;
				if(map[nr][nc] == 0) continue;
				
				union(sequence, map[nr][nc]);
			}
		}
		
		int day = 0;
		while(total > 1) {
			day++;
			for(int i=1; i<=sequence; i++) {
				ArrayList<Node> loc = locations[i];
				locations[i] =  new ArrayList<>();
				
				//int pId = find(i);
				
				for(Node node : loc) {
					for(int k=0; k<4; k++) {
						int nr = node.row + directY[k];
						int nc = node.col + directX[k];
						
						if(nr < 1 | nc < 1 || nr >= N+1 || nc >= N+1) continue;
						if(map[nr][nc] != 0) continue;
						map[nr][nc] = i;
						locations[i].add(new Node(nr, nc));
					}
				}
			}
			
			for(int i=1; i<=sequence; i++) {
				//if(find(i) != i) continue;
				for(int j=0; j<locations[i].size(); j++) {
					Node node = locations[i].get(j);
					for(int k=0; k<4; k++) {
						int nr = node.row + directY[k];
						int nc = node.col + directX[k];
						
						if(nr < 1 | nc < 1 || nr >= N+1 || nc >= N+1) continue;
						if(map[nr][nc] == 0) continue;
							
						int pId = find(map[nr][nc]);	
						
						union(i, pId);
					}
				}
			}
		}
			
		System.out.println(day);
		
		return;
	}
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		total--;
		parent[pb] = pa;
		/*
		if(locations[pa].size() >= locations[pb].size()) {
			parent[pb] = pa;
			locations[pa].addAll(locations[pb]);		
		} else {
			parent[pa] = pb;
			locations[pb].addAll(locations[pa]);
		}
		*/
	}
	
	static class Node {
		int row;
		int col;
		public Node(int row, int col) {		
			this.row = row;
			this.col = col;
		}
	}
}
