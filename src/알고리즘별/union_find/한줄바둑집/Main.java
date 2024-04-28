package 알고리즘별.union_find.한줄바둑집;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int W, N;
	
	static int[] parent;
	static int[] countByHouse;
	static int[] visited;
	static int houseCount;
	
	static int[] direct = {-1, 1};
	
	static int maxCount;
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int parent1 = find(a);
		int parent2 = find(b);
		
		if(parent1 == parent2) return;
		
		
		if(a == parent1 && b == parent2) {
			if(visited[b] == 1 && visited[a] == 1) 
				houseCount--;
			
		} else if(a != parent1 && b != parent2) {
			houseCount--;
		} else if(a == parent1 && b != parent2) {
			//houseCount--;
			if(visited[b] == 1 && visited[a] == 1) 
				houseCount--;
		}else {
			if(visited[b] == 1 && visited[a] == 1) 
				houseCount--;
		}
		
		
		int count = countByHouse[parent1] + countByHouse[parent2];
		maxCount = Math.max(count, maxCount);
		countByHouse[parent1] = count;	
		
		parent[parent2] = parent1;
	}
	
	static boolean check(int a, int direct) {
		if(a + direct < 0 || a + direct >= W) return false;
		if(visited[a + direct] == 1) return true;
		return false;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		W = Integer.parseInt(st.nextToken());
		
		
		parent = new int[W];
		countByHouse = new int[W];
		visited = new int[W];
		houseCount = 0;
		maxCount = 1;
		
		for(int i=0; i<W; i++) {
			parent[i] = i;
			countByHouse[i] = 1;
		}
		
		
		N = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			int n = Integer.parseInt(st.nextToken());
			
			boolean check = false;
			for(int j=0; j<2; j++) {				
				if(check(n, direct[j])) {
					union(n, n + direct[j]); 
					check = true;
					visited[n] = 1;
				} 
				
			}	
			visited[n] = 1;
			if(!check) houseCount++;
			
			
			
			System.out.println(houseCount + " " + maxCount);
		}	
	}
	
	
	
}
