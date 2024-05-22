package 프로원정대.day7.floodfill강의;

import java.io.*;
import java.util.*;

// flood fill
// -> 2차원 공간에서의 bfs다 라고 생각.

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static class Node {
		int y;
		int x;
		Node(int y, int x) {
			this.y = y;
			this.x = x; 
		}
	}
	static int[] ydir = {-1, 1, 0, 0};
	static int[] xdir = {0, 0, -1, 1}; 
	
	// 5 x5 짜리 맵을 채우는 flood fill
	static void ff(int y, int x) {
		// #1. 준비단계
		ArrayDeque<Node>q = new ArrayDeque<>();
		q.add(new Node(y, x));
		// 2차원 공간 => 항상 양방향
		int[][] visited = new int[5][5]; 
		// 시작 좌표를 기록
		visited[y][x] = 1; 
		
		// #2. 동작단계
		while(!q.isEmpty()) {
			// 얘가 지금 내가 방문하고 있는 좌표
			Node now = q.remove(); 
			// 이 좌표로부터 갈 수 있는 노드를 큐에 삽입
			// -> 갈수 있는 노드에 대한 정보 = 항상 문제에서 주어집니다.
			// #1. 이동 방향 (어떤 방향으로 퍼지는가?) -> 상하좌우
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i]; 
				// 방향배열 사용 -> 그렇다면 가장 먼제 해야하는 것. 
				if(ny < 0 || nx < 0 || ny >= 5 || nx >= 5)
					continue;
				if(visited[ny][nx] > 0)
					continue;
				visited[ny][nx] = visited[now.y][now.x]+1 ;
				q.add(new Node(ny, nx)); 				
			}
			int de = 1; 
		}
	}
	
	public static void main(String[] args) throws IOException {
		// bfs(시작노드)
		// 이제 노드가 아닙니다. -> 좌표를 가지고 있어야 합니다.
		ff(0, 0);
	}
}