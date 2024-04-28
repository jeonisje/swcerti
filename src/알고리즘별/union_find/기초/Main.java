package 알고리즘별.union_find.기초;

public class Main {
	static int[] parent;
	
	public static boolean union(int x, int y) {
		x = find(x);
		y = find(y);
		
		if(x == y) return false;
		
		if(x<=y) parent[y]= x;
		else parent[x] = y;
		return true;
	}
	
	public static int find(int x) {
		if(parent[x] == x) return x;
		return find(parent[x]);
	}
	
	public static void parentPrint() {
		System.out.print("[ ");
		for (int i = 1; i < parent.length; i++) {
			System.out.print(parent[i]+ " ");
		}
		System.out.println("]");
	}
	
	public static void main(String[] args) {
		int n = 5;
		parent = new int[n+1];
		for(int i=0; i<n+1; i++) parent[i] = i;
		
		union(1, 2);
		parentPrint();
		union(3, 4);
		parentPrint();
		union(3, 5);
		parentPrint();
		
		System.out.println("find(2): " + find(2));
		System.out.println("find(4): " + find(4));
		
		union(2, 4);
		parentPrint();
		System.out.println("find(4): " + find(4));
	}
}
