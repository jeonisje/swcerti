package 강사자료.분할정복.같은이진트리거리;

import java.io.*;
import java.util.*;

public class Main {
	
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int[] tree;
	static int depth;
	static int n; 
	static int result = 0; 
	
	static int dfs(int idx) {
		// leaf node까지 갔다면
		if(idx * 2 >= depth) {
			result += tree[idx]; 
			return tree[idx];
		}
		int left = dfs(idx * 2);
		int right = dfs(idx * 2 + 1); 
		// 보정값 누적
		result += Math.abs(left - right);
		// 노드의 값 누적
		result += tree[idx];
		// 더 큰쪽으로 아래에서 맞춘 값을 현 노드에 전달하여 현재 레벨까지의 최대값 갱신
		return tree[idx] + Math.max(left, right);
	}

	
	public static void main(String[] args) throws IOException {
		n = Integer.parseInt(br.readLine());
		depth = (int) Math.pow(2, n+1) -1;
		// System.out.println(depth);
		tree = new int[depth + 1]; 
		st = new StringTokenizer(br.readLine());
		// 2번 노드부터 입력받음	
		for(int i = 2; i <= depth; i++)
			tree[i] = Integer.parseInt(st.nextToken()); 
		dfs(1); 
		
//		for(int i = 0; i <= depth; i++)
//			System.out.print(tree[i] + " ");
//		System.out.println();
		System.out.println(result);
	}
}