package 연습문제.승강제리그._2;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		ArrayDeque<Node2> q = new ArrayDeque<>();
		q.add(new Node2(1, 3));
		q.add(new Node2(0, 3));
		q.add(new Node2(2, 5));
		q.add(new Node2(3, 4));
		q.add(new Node2(4, 3));
		//Collections.sort((List<Node2>) q, (o1, o2) -> o1.score == o2.score ? Integer.compare(o1.idx, o2.idx) : Integer.compare(o1.score, o2.score));
		
		while(!q.isEmpty()) {
			Node2 node = q.pollFirst();
			System.out.println("idx : " + node.idx + ", socre : " + node.score);
		}
	}
	
	
	

}

class Node2 {
	int idx;
	int score;
	public Node2(int idx, int score) {		
		this.idx = idx;
		this.score = score;
	}
}