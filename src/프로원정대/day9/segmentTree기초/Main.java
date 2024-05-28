package 프로원정대.day9.segmentTree기초;

public class Main {
	
	static int[] arr = {30, 40, 75, 34, 97, 12, 5, 24};
	static int N = 8;
	static int[] tree = new int[N*4];
	public static void main(String[] args) {
		// init(벼앨의 시작 index, 배열의 끝 index, root번호)
		initTree(0, N-1, 1);
		
		//query(배열의 시작 index, 배열의 끝 index, root 번호)
		
		System.out.println(query(0, N-1, 1, 1, 5));
		
	}
	private static int initTree(int start, int end, int node) {
		if(start == end) {
			// leaf node 도달
			return tree[node] = arr[start];
		}
		int mid = (start + end) / 2;
		
		int left = initTree(start, mid, node * 2);	// 왼쪽 구성
		int right = initTree(mid+1, end, node * 2 + 1);  	// 오른쪽 구성
		tree[node] = Math.max(left, right);
		return tree[node];
	}
	
	static int query(int start, int end, int node, int left, int right) {
		if(end < left ||  right < start) return Integer.MIN_VALUE;
		if(left <= start && end <= right) {
			// 완벽하게 포함하는 구간 
			return tree[node];
		}
		int mid = (start + end) / 2;
		int lv = query(start, mid, node * 2, left, right);
		int rv = query(mid+1, end, node*2 + 1, left, right);
		
		return Math.max(lv, rv);
	}
}	
