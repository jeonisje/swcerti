package 프로원정대.day3.SortLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		Node[] nodes = new Node[N];
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			nodes[i] = new Node();			
			nodes[i].num = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {			
			nodes[i].ch = st.nextToken().charAt(0);
		}
		
		Arrays.sort(nodes, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {				
				return Integer.compare(o1.num, o2.num);
			}			
		});
		
		for(int i=0; i<N; i++) 
			System.out.print(nodes[i].num + " ");
		
		System.out.println();
		
		Arrays.sort(nodes, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {				
				return Integer.compare(o2.num, o1.num);
			}			
		});
		
		for(int i=0; i<N; i++) 
			System.out.print(nodes[i].num + " ");
		
		System.out.println();
		
		Arrays.sort(nodes, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {	
				if(o1.num % 2 == o2.num % 2) { 
					if(o1.num == o2.num) return Character.compare(o1.ch, o2.ch);
					return  Integer.compare(o1.num, o2.num);
				}
				if(o1.num == o2.num) return Character.compare(o1.ch, o2.ch);
				return Integer.compare(o1.num % 2, o2.num % 2);
			}			
		});
		
		for(int i=0; i<N; i++) 
			System.out.print(nodes[i].num + " ");
		
		System.out.println();
		for(int i=0; i<N; i++) 
			System.out.print(nodes[i].ch + " ");
		
	}
	
	static class Node {
		int num;
		char ch;
		public Node() {};
		public Node(int num, char ch) {		
			this.num = num;
			this.ch = ch;
		}
		
	}
}
