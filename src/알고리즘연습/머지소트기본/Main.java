package 알고리즘연습.머지소트기본;

public class Main {
	static int[] sorted ;
	
	public static void main(String[] args) {
		int[] arr = {8, 2, 6, 4, 7, 3, 9, 5};	
		
		
		System.out.println("before => ");
		for(int i=0; i<arr.length; i++) {
			System.out.print(arr[i] + " ");			
		}
		System.out.println();
		mergeSort(arr);
		
		System.out.println("after => ");
		for(int i=0; i<arr.length; i++) {
			System.out.print(arr[i] + " ");			
		}
		
	}
	
	static void mergeSort(int[] arr) {
		sorted = new int[arr.length];
		mergeSort(arr, 0, arr.length - 1);
		//sorted = null;
		return;
	}
	
	static void mergeSort(int[] arr, int left, int right) {
		if(left == right) return;
		int mid = (left + right) / 2;
		
		mergeSort(arr, left, mid);
		mergeSort(arr, mid + 1, right);
		
		merge(arr, left, mid, right);
	}
	
	
	static void merge(int[] a, int left, int mid, int right) {
		int l = left;		// 왼쪽 부분리스트 시작점
		int r = mid + 1;	// 오른쪽 부분리스트의 시작점 
		int idx = left;		// 채워넣을 배열의 인덱스
		
		while( l <=mid && r <= right) {
			if(a[l] <= a[r]) {
				sorted[idx] = a[l];
				idx++;
				l++;
			} else {
				sorted[idx] = a[r];
				idx++;
				r++;
			}
		}
		
		if(l > mid) {
			while(r <= right) {
				sorted[idx] = a[r];
				idx++;
				r++;
			}
		}
		else {
			while(l <= mid) {
				sorted[idx] = a[l];
				idx++;
				l++;
			}
		}
		
		for(int i=left; i<=right; i++) {
			a[i] = sorted[i];
		}
	}
}


