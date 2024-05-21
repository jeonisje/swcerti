package 프로원정대.day6.네트워크바이러스;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	static int N, E;
	static ArrayList<Integer>[] graph;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		N = Integer.parseInt(br.readLine());
		E = Integer.parseInt(br.readLine());
		
		graph = new ArrayList[N+1];
		
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
 		}
 		
		for(int i=0; i<E; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			
			graph[from].add(to);
			graph[to].add(from);
		}
		
		System.out.println(bfs(1));
	}
	
	static int bfs(int start) {
		int count = 0;
		int[] visied = new int[N+1];
		
		
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(start);
		visied[start] = 1;
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			for(int next : graph[cur]) {
				if(visied[next] == 1) continue;
				q.add(next);
				visied[next] = 1;
				count++;
			}
			
		}
		
		
		return count;
	}
}
