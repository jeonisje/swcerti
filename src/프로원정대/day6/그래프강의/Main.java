package 프로원정대.day6.그래프강의;

import java.io.*;
import java.util.*;

/*
4 5
1 2
0 1
0 2
1 3
2 3
 */

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;

	
	static int N, E; 
	// #1. 인접 행렬
	static int[][] am;
	// #2. 인접 리스트
	static ArrayList<Integer>[] al; 
	
	public static void main(String[] args) throws IOException {
		// 입력
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 노드 개수
		E = Integer.parseInt(st.nextToken()); // 간선 개수
		am = new int[N][N]; // 행렬, 리스트의 크기는 문제를 잘 읽고 설정해주셔야 합니다.
		al = new ArrayList[N];
		// 배열 index : 정수인데.. value = 리스트
		// -> 연결된 리스트를 활성화를 시켜줘야 합니다
		for(int i = 0; i < N; i++)
			al[i] = new ArrayList<>(); 
		
		// 간선 정보 입력
		for(int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken()); // 출발지 
			int to = Integer.parseInt(st.nextToken()); // 도착지
			
			// 연결시키기
			// -> 단방향 연결
			am[from][to] = 1; 
			al[from].add(to); // from에서 to로 가는 길이 있다!
			
			// -> 양방향 연결
			am[to][from] = 1; 
			al[to].add(from); // to -> from으로 가는 길이 있다! 
		}
		
		// 인접 행렬 출력
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.print(am[i][j] + " ");
			}
			System.out.println();
		}
		
		// 리스트 출력
		for(int i = 0; i < N; i++) {
			System.out.print(i + "번 노드에서 갈 수 있는 노드들 : ");
			for(int j = 0; j < al[i].size(); j++) {
				System.out.print(al[i].get(j) + " ");
			}
			System.out.println();
		}
	}
}