package 알고리즘별.segmentTree.지옥철공황장애;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int MAX = 50_001;
	static int N, D;
	static int[] tree;
	static int[] arr;
	static int count;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		
		arr = new int[MAX];
		tree = new int[MAX*4];
		count = 0;
		int maxLoc = 0;
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			arr[a] = b;
			maxLoc = Math.max(maxLoc, a);
		}
		
		initTree(1, maxLoc, 1);
		
		for(int i=0; i<maxLoc-D; i++) {
			if(i < D) continue;
			if(arr[i] == 0) continue;
			int leftMax = query(1, maxLoc, 1, i-D, i);
			int rightMax = query(1, maxLoc, 1, i, i+D);
			int comp = arr[i];
			if(leftMax >= comp * 2 && rightMax >= comp * 2) {
				count++;
			}
		}
		
		System.out.println(count);
	}

	private static void initTree(int start, int end, int node) {
		if(start == end) {
			tree[node] = arr[start];
			return;
		}
		int mid = (start + end) / 2;
		initTree(start, mid, node*2);
		initTree(mid+1, end, node*2+1);
		tree[node] = Math.max(tree[node*2], tree[node*2+1]);
	}
	
	private static int query(int start, int end, int node, int left, int right) {
		if(end < left  || right < start) return 0;
		if(left <= start && end <= right) return tree[node];
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return Math.max(lv,  rv);
	}
}



/*


86 869
3078 914
1690 914
3221 987
8363 410
7323 732
4302 563
6297 296
9023 616
7351 468
6181 953
1943 640
206 146
2231 132
6965 987
3661 653
3662 620
4667 767
7222 502
2346 290
9541 190
7376 886
397 251
2380 664
6441 221
4054 477
3841 702
9697 796
3154 378
8091 846
3384 168
3737 663
3270 768
4045 860
6218 836
8338 476
4491 345
107 224
972 445
9705 780
7976 528
3364 182
2470 469
2639 316
3236 775
2118 576
6428 926
7280 77
6180 977
5279 64
6807 332
524 761
7871 257
3757 417
4348 296
1073 676
8220 712
1057 342
8323 805
1774 831
6337 858
7415 841
4897 966
3393 366
2213 457
1935 868
3741 381
4425 82
8175 215
1270 682
5837 652
9572 871
3588 959
4524 182
4713 727
5441 694
4492 67
8991 929
4310 879
4189 59
4476 230
9597 96
5403 424
5273 287
7259 34
533 691
2223 755



32
*/


