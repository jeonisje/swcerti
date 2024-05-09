package 멘토링권장문제.도로건설.안나와엘사;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[][] map;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
			
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		map = new int [N][N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String s = st.nextToken();
			for(int j=0; j<N; j++) {
				char c = s.charAt(j);
				if(c == '_') map[i][j] = 1;
				if(c == '#') map[i][j] = 0;
			}			
		}
		
		
		st = new StringTokenizer(br.readLine());
		
		int startRow1 = Integer.parseInt(st.nextToken());
		int startCol1 = Integer.parseInt(st.nextToken());
		int startRow2 = Integer.parseInt(st.nextToken());
		int startCol2 = Integer.parseInt(st.nextToken());
		
		
		System.out.println(bfs(startRow1, startCol1, startRow2, startCol2));
	}
	
	static int bfs(int startRow1, int startCol1, int startRow2, int startCol2) {
		int[][][] visited = new int[N][N][2];
		
		ArrayDeque<Node> q = new ArrayDeque<Node>();
		q.add(new Node(0, startRow1, startCol1));
		q.add(new Node(1, startRow2, startCol2));
		
		visited[startRow1][startCol1][0] = 1;
		visited[startRow2][startCol2][1] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.type ==  0) {
				if(visited[cur.row][cur.col][1] != 0) {
					return Math.max(visited[cur.row][cur.col][0], visited[cur.row][cur.col][1]) - 1;  
				}
			} else {
				if(visited[cur.row][cur.col][0] != 0) {
					return Math.max(visited[cur.row][cur.col][0], visited[cur.row][cur.col][1]) - 1;  
				}
			}
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directY[i];
				int nextCol = cur.col + directX[i];
				
				if(nextRow < 0 || nextCol < 0 || nextRow >= N || nextCol >= N) continue;
				if(map[nextRow][nextCol] == 0) continue;
				if(visited[nextRow][nextCol][cur.type] != 0) continue;
				
				q.add(new Node(cur.type, nextRow, nextCol));
				visited[nextRow][nextCol][cur.type]  = visited[cur.row][cur.col][cur.type] + 1; 
			}
		}
		
		
		return 0;
	}
	
	static class Node {
		int type;
		int row;
		int col;
		public Node(int type, int row, int col) {		
			this.type = type;
			this.row = row;
			this.col = col;
		}
		
	}
}


/*


5
___#_
___#_
##___
__#__
_____
0 0 4 0


*/

