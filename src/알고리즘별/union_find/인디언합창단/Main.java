package 알고리즘별.union_find.인디언합창단;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static char[] parent;
	static int[] belonged;
	
	static char find(char a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(char a, char b) {
		char p1 = find(a);
		char p2 = find(b);
		
		if(p1 == p2) return;
		parent[p2] = p1;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		belonged = new int[200];
		
		parent = new char[200];
		for(char i='A'; i<='Z'; i++) {
			parent[i] = i;
		}
		int notBelongCount = 26;
		int groupCount = 0;
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			char a = st.nextToken().charAt(0);
			char b = st.nextToken().charAt(0);			
				
			char p1 = find(a);
			char p2 = find(b);
			
			if(p1 == p2) continue;			
			
			int check = 0;
			if(belonged[a] == 0 && belonged[b] == 0) {
				groupCount++;
				check = 2;
			} else if(belonged[a] == 1 && belonged[b] == 1) {
				groupCount--;
			} else {
				check = 1;
			}
			
			
			notBelongCount = notBelongCount - check;
			
			union(a, b);
			belonged[a] = 1;
			belonged[b] = 1;
			
		}	
		
		
		System.out.println(groupCount);
		System.out.println(notBelongCount);
	}
}


/*
 * 
6
Z Y
Y W
Z T
R O
M T
T R



10
A B
B E
E G
G A
Z Y
Y W
Z T
R O
M T
T R


26
A B
C D
E F
G H
I J
K L
M N
O P
Q R
S T
U V
W X
Y Z
Z A
C B
E D
G F
I H
K J
M L
O N
Q P
S R
U T
W V
Y X

*/

