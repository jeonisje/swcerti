package 프로원정대.day3.TreeMap강의.기본;

import java.io.*;
import java.util.*;

// TreeMap
// - key가 정렬된 형태를 띄는 map 자료구조
// - key, value pair로 이루어져 있고, key를 통해 value에 접근 가능하다.
// - key는 고유하다. (중복을 허용하지 않는다.)

// 키워드 : [정렬] [우선 순위]
// -> PQ와 많이 혼돈하기 쉬운 구조 [우선 순위]

// 시간복잡도 : 모든 명령어의 시간복잡도 : logN (size 제외)
// ** logN이지만... 주의해야하는 사항들은 있긴 합니다. 

// 어떨때 쓰는가? : 
// 명령어들 : 

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    
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
		tm.remove("three");
		int de = 1;
		
		// 추가
		// B. five=55
		// -> 이미 존재하는 key라면 -> 새로 들어온거로 덮어씌워집니다.
		// -> ** map에서 key는 항상 [고유]합니다. (unique)
		// ---> 중복된 key는 허용되지 않습니다.
		tm.put("five", 55);
	}
}