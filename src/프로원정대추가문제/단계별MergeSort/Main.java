package 프로원정대추가문제.단계별MergeSort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[] arr;
	static int[] sorted;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		arr = new int[N];
		sorted = new int[N];
		 st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		mergeSort(0, N-1);
	}
	
	static void mergeSort(int left, int right) {
		if(left == right) return;
		
		int mid = (left + right) / 2;
		
		mergeSort(left, mid);
		mergeSort(mid + 1, right);
		
		merge(left, mid, right);
	}

	static void merge(int left, int mid, int right) {
		int l = left;
		int r = mid + 1;
		int idx = left;			
		
		while(l <= mid && r <= right) {
			if(arr[l] <= arr[r]) {
				sorted[idx] = arr[l];
				l++;
				idx++;
			} else {
				sorted[idx] = arr[r];
				r++;
				idx++;
			}
		}
		
		if(l > mid) {
			while(r <= right) {
				sorted[idx] = arr[r];
				r++;
				idx++;
			}
		} else {
			while(l <= mid) {
				sorted[idx] = arr[l];
				l++;
				idx++;
			}
		}
		
		for(int i = left; i <= right; i++) {		
			arr[i] = sorted[i];
			System.out.print(arr[i] + " ");
		}
		
		System.out.println();
	}

}
