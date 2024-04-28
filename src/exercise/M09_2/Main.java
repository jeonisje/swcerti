package exercise.M09_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	static int N;
	static Map<String, Node> checkMap = new HashMap<>();
	static TreeMap<Node, Boolean> resultMap = new TreeMap<>();
	
	public static void main(String[] args) throws IOException {
		//BufferedReader br = new BufferedReader(new FileReader("src/exercise/M09_2/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String name = st.nextToken();
			
			if(checkMap.containsKey(name)) {
				Node oldNode = checkMap.get(name);
				Node newNode = new Node(oldNode.name, oldNode.cnt + 1);
				resultMap.remove(oldNode);
				resultMap.put(newNode, true);				
				checkMap.replace(name, newNode);
			} else {
				Node newNode = new Node(name, 1);
				checkMap.put(name, newNode);
				resultMap.put(new Node(name, 1), true);
			}
		}
		
		System.out.println(resultMap.firstKey().name);
	}
	
}

class Node implements Comparable<Node>{
	String name;
	int cnt;
	
	public Node(String name, int cnt) {
		this.name = name;
		this.cnt = cnt;
	}

	@Override
	public int compareTo(Node o) {
		if(this.cnt > o.cnt) return -1;
		if(this.cnt < o.cnt) return 1;
		return this.name.compareTo(o.name);
	}
	
	
}
