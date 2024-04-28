package 알고리즘별.union_find.춘추전국시대;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int N, T;
		
	static char[] parent;
	
	static int[] popular;
	static int[] popularByUnion;
	static int[] countByUnion;
	static int[] belonged;
	
	static int surived;
	
	static char find(char a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(char a, char b) {
		char p1 = find(a);
		char p2 = find(b);
		
		if(p1 == p2) return;
		
		int popular1 = getPopular(a, p1);
		int popular2 = getPopular(b, p2);
		
		int count1 = getCount(a, p1);
		int count2 = getCount(b, p2);
		
		popularByUnion[p1] = popular1 + popular2;
		countByUnion[p1] = count1 + count2;
		parent[p2] = p1;
	}
	
	static void fight(char a, char b) {
		char p1 = find(a);
		char p2 = find(b);
		
		int loseCount = 0;
		
		if(popularByUnion[p1] > popularByUnion[p2]) {
			loseCount =  countByUnion[p2];
		} else {
			loseCount = countByUnion[p1];
		}
		
		surived = surived - loseCount;
	}
	
	static int getPopular(char a, char parent) {
		return parent == a ? popular[a] : popularByUnion[parent];
	}
	
	static int getCount(char a, char parent) {
		return parent == a ? 1: countByUnion[parent];
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());

		surived = N;
		
		parent = new char[100];
		popular = new int[100];
		popularByUnion = new int[100];
		countByUnion = new int[100];
		belonged = new int[100];
		
		for(char i = 'A'; i<='Z'; i++) {
			parent[i] = i;
		}
		
		st = new StringTokenizer(br.readLine());
		char cIdx = 'A';
		for(int i=0; i<N; i++) {		
			int num = Integer.parseInt(st.nextToken());
			popular[cIdx] = num;
			popularByUnion[cIdx] = num;
			countByUnion[cIdx] = 1;
			cIdx++;
		}
		
		st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<T; i++) {
			st = new StringTokenizer(br.readLine());
			String cmd = st.nextToken();
			char a = st.nextToken().charAt(0);
			char b = st.nextToken().charAt(0);
			
			switch(cmd) {
				case  "alliance" :
					union(a, b);
					break;
				case "war" :
					fight(a, b);
					break;
			}
		}
		
		System.out.println(surived);
	}
	
	

}

/*
 
 
3
38481 86027 89663 
2
war A B
war B C

1


4
73739 32454 52466 45463 
3
alliance A C
alliance B A
war B D

3

*/


