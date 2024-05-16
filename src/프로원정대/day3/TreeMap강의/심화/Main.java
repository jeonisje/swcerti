package 프로원정대.day3.TreeMap강의.심화;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

//TreeMap
//- key가 정렬된 형태를 띄는 map 자료구조
//- key, value pair로 이루어져 있고, key를 통해 value에 접근 가능하다.
//- key는 고유하다. (중복을 허용하지 않는다.)

//키워드 : [정렬] [우선 순위] [조건부 탐색]
//조건부 탐색이라는것 : x보다 (크거나 같거나 작거나) 중 ~ 가장 (크거나 작은거)
//-> PQ와 많이 혼돈하기 쉬운 구조 [우선 순위]

//시간복잡도 : 모든 명령어의 시간복잡도 : logN (size 제외)
//** logN이지만... 주의해야하는 사항들은 있긴 합니다. 

//어떨때 쓰는가? :
//-> 정렬이 있는 구조가 필요하다
//-> 조건부 탐색에 대한 query가 존재한다
//-> 정렬이 되었을때, 일부분에 대한 정보를 빠르게 확인할 필요가 있다.

//주의 사항 : 
//#1. 조건부 탐색을 하기 위해서는 [완벽 오름차순] 구조로 관리해야 한다.
//#2. self-balancing tree
//--> 삭제, 삽입이 되건, 모든 경우에 트리 구조가 비틀어질겁니다.
//--> 양이 많아질수록, log(N)이라고 하지만, 체감보다 많이 느려질겁니다...
//--> 만약 삽입되는 요소의 양 또는 호출횟수이 너무 많다면, 조금 기피 해야 합니다.
//--> 예) 삽입및 삭제의 호출수가 너무 많다
//#3. ** class를 key로 쓸때, 중복되는 값이 많은 경우, 안쓰는것을 권장
//--> 생각하기에 이거 말고 생각할수 있는게 없다면.... 시간초과 날 가능성이 높습니다.

public class Main {
 static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 static StringTokenizer st;
 
 static class Person implements Comparable <Person>{
 	int age; // 16
 	int height; // 171
 	
 	Person(int age, int height) {
 		this.age = age;
 		this.height = height; 
 	}
 	@Override public int compareTo(Person o) {
 		if(this.age < o.age) return -1;
 		if(this.age > o.age) return 1;
 		if(this.height < o.height) return -1;
 		if(this.height > o.height) return 1;
 		return 0;
 	}
 }
 
	public static void main(String[] args) throws IOException {
		// TreeMap<Key, Value>
		TreeMap<String, Integer>tm = new TreeMap<>();
		// 삽입 = put(key, value)
		tm.put("four", 4);
		tm.put("five", 5);
		tm.put("one", 1);
		tm.put("two", 2);
		tm.put("three", 3);
		
		// access = get(key)
		// key를 통해서 -> value에 접근이 가능하다.
		System.out.println(tm.get("one"));
		
		// remove = remove(key)
		//tm.remove("three");
		int de = 1;
		
		// 추가
		// B. five=55
		// -> 이미 존재하는 key라면 -> 새로 들어온거로 덮어씌워집니다.
		// -> ** map에서 key는 항상 [고유]합니다. (unique)
		// ---> 중복된 key는 허용되지 않습니다.
		tm.put("five", 55);
		
		// ===============================================================
		// treemap 심화 기능들
		// 조건부 탐색
		TreeMap<Integer, String>tmm = new TreeMap<>(); 
		tmm.put(4, "four");
		tmm.put(5, "five");
		tmm.put(1, "one");
		tmm.put(2, "two");
		tmm.put(3, "three");
		
		int target = 3; 
		// 3보다 큰 key중 제일 작은 key는 무엇인가요? (또는 제익 작은 key의 값은 무엇인가요?)
		// Returns the least key strictly greater than the given key, or null if there is no such key.
		
		// target보다 큰 key중 가장 작은 key
		System.out.println(tmm.higherKey(target));
		// target 이상의 key중 가작 작은 key
		System.out.println(tmm.ceilingKey(target));
		// target보다 작은 key중 가장 큰 key
		System.out.println(tmm.lowerKey(target));
		// target 이하의 key중 가장 큰 key
		System.out.println(tmm.floorKey(target));
		
		// 문자열도 동일하게 찾아낼 수 있다.
		System.out.println(tm.higherKey("five"));
		System.out.println(tm.ceilingKey("five"));
		
		// ==========================================================
		// TreeMap에 custom Key
		TreeMap<Person, Integer>ptm = new TreeMap<>(); 
		ptm.put(new Person(16, 170), 16);
		ptm.put(new Person(33, 175), 33);
		ptm.put(new Person(24, 172), 24);
		ptm.put(new Person(24, 176), 24);
		ptm.put(new Person(24, 166), 24);
		
		// 이런식으로 class가 key로 들어가는 경우를 많이 쓸겁니다.
		// ** 위에서 본 조건부 탐색 ~Key() -> 
		// 반드시 "완벽 오름차순을 유지하는 형태로 comparable / comparator 짜주셔야 합니다.
		// 완벽 오름차순 : class의 요소들이 모두 오름차순으로 되도록 만들어줘야 합니다.
		
		// 지금 보여드린거처럼,
		// 만약에 요소들이 완벽 오름차순을 유지하지 못한다면 -> 
		// 조건부 탐색이 문제에서 주어지더라도, treemap을 사용할수 없다 (효율적으로)
		
		// 나이는 20이상인 사람 중 키가 가장 작은 사람을 찾고 싶다.
		Person now = ptm.ceilingKey(new Person(20, -1));
		System.out.println(now.age + " " + now.height);
		
		// =================================================
		
		// iterator
		// treemap.keySet() -> treemap에 존재하는 key들을 모두 set으로 변환해서 가져옵니다.
		Iterator it = tm.keySet().iterator();
//		while(it.hasNext()) {
//			// it.next() -> object로 나옵니다.
//			String key = (String) it.next(); 
//			System.out.println(key + " " + tm.get(key));
//		}
//		
//		// set = 고유집합
//		// -> 중복된 값들을 허용하지 않는 동적배열?
//		TreeSet<Integer>ts = new TreeSet<>();
//		ts.add(1);
//		ts.add(4);
//		ts.add(4);
//		ts.add(5);
//		ts.add(3);
		
		// 주의 : -> 반드시 필요한 경우가 아니라면 -> iteration 하지 마세요
		// iteration 가능은 합니다.
		// 단, 여태까지 봐왔던 모든 구조는 내부적으로 "배열로 구성" 
		// 순차적으로 다음 노드를 찾아가는 과정 = 배열보다 메모리를 찾아가는 속도가 더 필요하기 때문에
		// -> 더 오래걸릴겁니다. 
		
		// O(N) + @
		for(Map.Entry<Integer, String>ent : tmm.entrySet()) {
			System.out.println(ent.getKey() + " " + ent.getValue()); 
		}
		
		// =====================================================
		
		// treemap의 일부분만 확인
		// 1 2 3 4 5 (tmm) -> key가 2 이상이면서, 5보다 작은 key는 몇개가 있어? 
		// -> treemap의 일부 map 만 분리해서 return하는 subMap
		// logN
		// subMap(fromKey, toKey) 
		// -> fromkey = 포함 (이상), toKey = 미포함 (미만)
		System.out.println(tmm.subMap(2,  5+1).size());
		
		// 문자열 key 
		// subMap(fromKey, bool1, toKey, bool2)
		// bool1 = true면, fromkey 포함, 아니면 미포함
		// bool2 = true면 toKey포함, 아니면 미포함
		System.out.println(tm.subMap("f", true, "three", true));
	}
}
