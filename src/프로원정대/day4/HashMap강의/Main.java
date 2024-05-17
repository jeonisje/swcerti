package 프로원정대.day4.HashMap강의;

import java.io.*;
import java.util.*;

// hashMaP
// - TREEMAP과 사용방법이 거의 동일합니다.
// - 둘은 완전히 다른 구조를 가지고 있습니다.
// - treemap vs hashmap 연관되서 생각하시면 안됩니다.
// --> treemap 대신 hashmap 또는 hashmap 대신 treemap

// hashmap = hashing 함수를 만드는게 가장 중요한 역할을 합니다. (시간 효율성을 높이는 역할)

// 키워드 : [기록] 
// 사용처 : 항상 DAT의 fallback용도
// --> 문자열, 너무 큰 수, 음수

// 시간복잡도 : 삽입, 삭제, 접근 : O(1) + @ (배열의 O(1)보다는 느리다)

// 주의사항:
// #1. hashCode (getHash()) -> 반드시 중복 없는 고유의 번호를 낼수 있는 수식을 만들어 낼수 있어야 합니다.
// #2. ** 절대로 필요한 상황이 아니라면 iteration 하지 않는것을 권장


public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
	static StringTokenizer st;
	
	static class Node {
		// 좌표의 범위가 1~100까지 라고 한다면?
		// (1,1)~(100,100)
		int y;
		int x;
		Node(int y, int x) {
			this.y = y;
			this.x = x; 
		}
		
		@Override
		public int hashCode() {
			// 최대한 중복되지 않을 숫자로 구성을 할수 있어야 합니다.
			return y * 100 + x; // 모든 노드는 hashCode = 1
		}
		
		@Override
		public boolean equals(Object o) {
			Node ot = (Node) o;
			return this.y == ot.y && this.x == ot.x;
		}
	}
	
	static int getHash(int y, int x) {
		return y * 100 + x; 
	}
	
	public static void main(String[] args) throws IOException {
		HashMap<Integer, String>hm = new HashMap<>();
		hm.put(4, "four");
		hm.put(5, "five");
		hm.put(1, "one");
		hm.put(2, "two");
		hm.put(3, "three");
		
		// 정렬을 유지하지 않는 map
		// treemap에서 사용하던 ~Key를 통한 구간? 조건부? 탐색은 불가능
		HashMap<String, Integer>hmm = new HashMap<>();
		hmm.put("four", 4);
		hmm.put("five", 5);
		hmm.put("one", 1);
		hmm.put("two", 2);
		hmm.put("three", 3);
		hmm.put("three", 33);
		
		System.out.println("four".hashCode());
		System.out.println("five".hashCode());
		
		int d = 1; 
		
		// 명령어 자체는 treeMap과 동일
		// access :
		System.out.println(hm.get(1));
		
		// remove :
		hm.remove(3);
		
		// iteration : 
		for(Map.Entry<Integer, String> ent : hm.entrySet()) {
			System.out.println(ent.getKey() + " " + ent.getValue());
		}
	
		// =======================================================
		// Class를 넣어볼겁니다.
		HashMap<Node, Integer>hhm = new HashMap<>(); 
		// 지금 아무것도 안헀는데 삽입 잘 되고 있습니다. 
		// 문제 #2. Map = 고유의 key를 가지는 구조 -> 중복이 허용되서는 안되는데, 중복이 들어가고 있습니다.
		hhm.put(new Node(1, 1), 1);
		hhm.put(new Node(1, 100), 2);
		hhm.put(new Node(100, 1), 3);
		hhm.put(new Node(1, 1), 4);
		
		System.out.println(new Node(1,1).hashCode());
		System.out.println(new Node(1,1).hashCode());
		System.out.println(new Node(1,1).equals(new Node(1,1)));
		
		int de = 1;
		// 문제 #1. class를 사용했을때, access에 문제가 생기고 있다.
		System.out.println(hhm.get(new Node(1, 100)));
		
		// ==========================================================
		
		// ** hashmap을 쓸때 모든 Key는 항상 Integer로 사용하는것을 권장합니다. 
		// 절대로 중복되는 숫자가 안나와야 합니다.
		// 여러분들이 class를 만들어서 넣는것은 권장하지는 않습니다.
		// --> 최적화 : new로 어떤 객체를 생성하는것 = 내부적으로 생각보다 시간 오래걸립니다.
		// --> 배열도 계속해서 new -> 때려넣는거 이런거 생각보다 시간 많이 걸립니다.
		
		HashMap<Integer, Integer>hm2 = new HashMap<>(); 
		// (1,1)
		int hashCode = getHash(1,1); 
		// System.out.println(hashCode);
		hm2.put(hashCode, 1);

		// ** 문자열은 왠만해서 hashmap의 key로 사용하지 않는것을 권장.
		// String = hashCode를 내부적으로 만드는 과정에서 O(문자열의 길이)
		// bucket에서 찾아갈때마다 최악의 경우 O(문자열 길이)
		// 시간이 기하급수적으로 늘어납니다.
	}
}