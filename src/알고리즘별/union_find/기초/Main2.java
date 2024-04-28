package 알고리즘별.union_find.기초;

public class Main2 {
	
	
	static void solution() {
		UnionFind uf = new UnionFind();
		
		uf.union(1, 3);
		uf.print();
		uf.union(1, 4);
		uf.print();
		uf.union(2, 1);
		uf.union(5, 6);
		uf.print();
		uf.union(6, 7);
		uf.print();
		
		if(uf.find(1) == uf.find(7)) System.out.println("같은 그룹");
		else System.out.println("다른 그룹");
	}
	
	public static void main(String[] args) {
		solution();
	}
}


class UnionFind {
	int[] arr = new int[10];
	
	public UnionFind() {
		for(int i=0; i<10; i++) {
			arr[i] = i;
		}
	}
	
	int find(int a) {
		if(arr[a] == a) return a;
		
		//int parent = find(arr[a]);
		return arr[a] = find(arr[a]);
	}
	
	void union(int a, int b) {
		int t1 = find(a);
		int t2 = find(b);
		
		if(t1 == t2)  return;
		arr[t2] = t1;
	}
	
	void print() {
		
		for(int n : arr) {
			System.out.print(n + " ");
		}
		System.out.println();
		System.out.println("----------------------------");
		
	}
}