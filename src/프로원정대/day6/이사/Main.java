package 프로원정대.day6.이사;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, R, K;
	static ArrayList<Integer>[] graph;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		graph = new ArrayList[N+1];
		
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
 		}
 		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			
			graph[from].add(to);
			graph[to].add(from);
		}
		
		st = new StringTokenizer(br.readLine());
		R = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		System.out.println(bfs());
	}
	
	static int bfs() {
		int count = 1;
		int[] visited = new int[N+1];
		Arrays.fill(visited, -1);
		
		
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(R);
		visited[R] = 0;
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			if(visited[cur] >= K) continue;
			
			for(int next : graph[cur]) {
				if(visited[next] != -1) continue;
				q.add(next);
				visited[next] = visited[cur] + 1;
				count++;
				//System.out.println(next);
			}
			
		}
		
		
		return count;
	}
}


/*


5 6
1 2
1 3
1 4
2 3
2 4
3 4
5 1

1

*/