package test;

import java.util.TreeMap;

public class Test {
	public static void main(String[] args) {
		Student s1 = new Student(1, 100);
		Student s2 = new Student(2, 100);
		Student s3 = new Student(3, 200);
		Student s4 = new Student(4, 300);
		Student s5 = new Student(5, 500);
		Student s6 = new Student(6, 200);
		
		TreeMap<Student, Integer> treeMap = new TreeMap<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.score, o2.score));
		treeMap.put(s1, 1);
		treeMap.put(s2, 1);
		treeMap.put(s3, 1);
		treeMap.put(s4, 1);
		treeMap.put(s5, 1);
		treeMap.put(s6, 1);
		
		Student s = treeMap.ceilingKey(new Student(300, 6));
		System.out.println(s.id + " " + s.score);
	}
}

class Student {
	int id;
	int score;
	public Student(int id, int score) {	
		this.id = id;
		this.score = score;
	}
	
}
