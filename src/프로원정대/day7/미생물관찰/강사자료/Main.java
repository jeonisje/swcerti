 package 프로원정대.day7.미생물관찰.강사자료;

 import java.io.*;
 import java.util.*;

 // 미생물 관찰

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
 	
 	static int[][] visited;
 	
 	static int N, M; 
 	static String[] map; 
 	static int MAX = Integer.MIN_VALUE; 
 	
 	static void ff(int y, int x) {
 		// 여기서 해야 하는것
 		// #1. 이 개체의 모든 공간을 기록
 		// #2. 이 개체의 크기
 		
 		ArrayDeque<Node>q = new ArrayDeque<>();
 		q.add(new Node(y, x));
 		
 		//int[][] visited = new int[N][M];
 		visited[y][x] = 1; 
 		
 		int cnt = 0; 
 		
 		while(!q.isEmpty()) {
 			Node now = q.remove();
 			// now가 빠져나오는 개수만큼 = 이 개체의 크기
 			cnt++; 
 			for(int i = 0; i < 4; i++) {
 				int ny = now.y + ydir[i];
 				int nx = now.x + xdir[i];
 				if(ny < 0 || nx < 0 || ny >= N || nx >= M)
 					continue;
 				if(visited[ny][nx] == 1)
 					continue;
 				// 퍼질때, 똑같은 개체의 일부로만 퍼져야 합니다.
 				if(map[ny].charAt(nx) != map[y].charAt(x))
 					continue;
 				visited[ny][nx] = 1;
 				q.add(new Node(ny, nx)); 
 			}
 		}
 		// int de = 1;
 		// 이 개체의 크기가 최대 크기인지 확인
 		MAX = Math.max(MAX, cnt); 
 	}
 	
 	public static void main(String[] args) throws IOException {
 		st = new StringTokenizer(br.readLine());
 		N = Integer.parseInt(st.nextToken());
 		M = Integer.parseInt(st.nextToken());
 		
 		map = new String[N]; 
 		
 		for(int i = 0; i < N; i++) {
 			map[i] = br.readLine(); 
 		}
 		
 		// 얘를 전역으로 빼서, 이미 찾은 개체는 기록되어있도록 할겁니다.
 		visited = new int[N][M]; 
 		
 		// (0,0) 부터 모든 칸들을 탐색하면서,
 		// -> 이미 찾은 개체가 아닌 새로운 개체의 일부를 발견한다면 
 		// -> 여기서에부터 ff을 해서 현재 개체의 크기를 판단할겁니다.
 		// --> (이 개체의 전체를 기록해볼것이다.)
 		//int cnt = 0; 
 		// index 0 : 'A'
 		// index 1 : 'B'
 		int[]cnt = new int[2]; 
 		for(int i = 0; i < N; i++) {
 			for(int j = 0; j < M; j++) {
 				if(map[i].charAt(j) != '_' && visited[i][j] == 0) {
 					// 개체의 일부가 있는 곳이다!
 					ff(i, j); 
 					//cnt++; // 하나의 새로운 개체를 찾았다!
 					cnt[map[i].charAt(j) - 'A']++; 
 				}
 			}
 		}
 		//System.out.println(cnt);
 		System.out.println(cnt[0] + " " + cnt[1]);
 		System.out.println(MAX);
 	}
 }