package 알고리즘별.segmentTree;

public class SegmentTree {
	long tree[]; //각 원소가 담길 트리
    int treeSize; // 트리의 크기

    public SegmentTree(int arrSize){
        
        // 트리 높이 구하기
        int h = (int) Math.ceil(Math.log(arrSize)/ Math.log(2));
		// 높이를 이용한 배열 사이즈 구하기
        this.treeSize = (int) Math.pow(2,h+1);
        // 배열 생성
        tree = new long[treeSize];
    }
	// arr = 원소배열, node = 현재노드, start = 현재구간 배열 시작, start = 현재구간 배열 끝
    public long init(long[] arr, int node, int start,int end){
        
        // 배열의 시작과 끝이 같다면 leaf 노드이므로
        // 원소 배열 값 그대로 담기
        if(start == end){
            return tree[node] = arr[start];
        }
		
        // leaf 노드가 아니면, 자식노드 합 담기
        return tree[node] = init(arr,node*2,start,(start+ end)/2) + init(arr,node*2+1,(start+end)/2+1,end);
    }
}
