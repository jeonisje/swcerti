package 강사자료.dfs.해밀턴회로;

import java.io.*;
import java.util.*;

public class Main {
	
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st; 
    
    static int N;
    static int[][] mat; 
    static int[] visited; 
    static int MIN = Integer.MAX_VALUE;
    
    static void dfs(int now, int cnt, int sum) {
    	if(now == 0 && cnt == N) {
    		if(sum < MIN)
    			MIN = sum;
    		return; 
    	}
    	for(int next = 0; next < N; next++) {
    		if(mat[now][next] == 0)
    			continue;
    		if(visited[next] == 1)
    			continue; 
    		if(sum + mat[now][next] > MIN)
    			continue; 
    		visited[next] = 1;
    		dfs(next, cnt + 1, sum + mat[now][next]); 
    		visited[next] = 0; 
    	}
    }

    public static void main(String[] args) throws IOException {
    	N = Integer.parseInt(br.readLine());
    	mat = new int[N][N];
    	visited = new int[N]; 
    	for(int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
    		for(int j = 0; j < N; j++) {
    			mat[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	// visited[0] = 1; 
    	dfs(0, 0, 0); 
    	System.out.println(MIN);
    }
}