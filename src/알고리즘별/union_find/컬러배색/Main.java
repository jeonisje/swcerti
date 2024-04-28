package 알고리즘별.union_find.컬러배색;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static HashMap<String, Integer> colorMap;
	static int[] parent;
	static ArrayList<String>[] colorList;
	
	static int MAX;
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		//ArrayList<String> colorsA = colorList[pa];
		ArrayList<String> colorsB = colorList[pb];
		
		colorList[pa].addAll(colorsB);
		parent[pb] = pa;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		MAX = N * 2 - 1;
		
		colorMap = new HashMap<>();
		parent = new int[MAX];
		colorList = new ArrayList[MAX];
		
		for(int i=0; i<MAX; i++) {
			parent[i] = i;
			colorList[i] = new ArrayList<>();
		}
		
		int idx = 0;
		for(int i=0; i<N;i++) {
			st = new StringTokenizer(br.readLine());
			
			String color1 = st.nextToken();
			String color2 = st.nextToken();
			
			int color1Idx;
			int color2Idx;
			if(colorMap.containsKey(color1)) {
				color1Idx = colorMap.get(color1);
			} else {
				color1Idx = idx;
				colorMap.put(color1, idx);
				colorList[color1Idx].add(color1);
				idx++;
			}
			
			if(colorMap.containsKey(color2)) {
				color2Idx = colorMap.get(color2);
			} else {
				color2Idx = idx;
				colorMap.put(color2, idx);
				colorList[color2Idx].add(color2);
				idx++;
			}
			
			
			union(color1Idx, color2Idx);
			
		}
		
		st = new StringTokenizer(br.readLine());		
		String color = st.nextToken();
		
		int colorIdx = find(colorMap.get(color));
		Collections.sort(colorList[colorIdx]);
		for(String c : colorList[colorIdx]) {
			System.out.println(c);
		}
	}
}
