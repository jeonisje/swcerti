package 프로원정대.day3.PriorityQueue강의;

import java.io.*;
import java.util.*;

//Priority Queue
//-> default : min heap
//-> 정렬된 형태가 아닙니다.

//키워드 : [우선 순위] 
//-> 어떤 조건에 맞는 요소들이 많이 있을때 -> 이에 맞는 가장 높은 우선순위인것만을 찾을려고할때
//시간복잡도 : logN
//-> TreeMap보다 훨씬 빠른 logN
//balancing 과정을 생각해보면 됩니다. 
//-> RBT -> 뒤집어 엎고 이런게 많습니다. (recoloring, 삼촌노드랑도 비교해야하고, 움직여야 하고)
// --> tree (node) 
//-> PQ -> 얘는 balancing 과정이 굉장히 simple
// --> basic한 구성이 [배열]

//** 만약 TM과 PQ를 둘다 쓸수 있다 -> PQ를 사용하는것이 항상 빠를겁니다.


public class Main {
 static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 static StringTokenizer st;
 
	public static void main(String[] args) throws IOException {
		PriorityQueue<Integer>pq = new PriorityQueue<>();
		
		// add : logN
		pq.add(2);
		pq.add(5);
		pq.add(3);
		pq.add(4);
		pq.add(1);

		// remove : logN
		// 중간 삭제 가능하나, 쓸일 없을겁니다. (쓰지 마세요)
		System.out.println(pq.remove());
		
		// 가장 높은 우선순위인 친구를 return (삭제 X)
		System.out.println(pq.peek());
		
		// iteration도 할 필요 없음
	}
}