package training1.lv165_6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	static TreeMap<Node, Boolean> map = new TreeMap<>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		String s = st.nextToken();
		
		for(int i=0; i<s.length(); i++) {
			Node node = new Node(String.valueOf(s.charAt(i)), i);
			map.put(node, true);
		}
		
		System.out.println(map.lastKey().idx);
		System.out.println(map.firstKey().idx);
		
		
	}
	
}

class Node implements Comparable<Node> {
	String s;
	int idx;
	
	
	public Node(String s, int idx) {
		this.s = s;
		this.idx = idx;
	}
	
	@Override
	public int compareTo(Node o) {
		return this.s.compareTo(o.s);
	}
	
	
}
