package 강사자료.분할정복.mergeSort;

import java.util.*;
import java.io.*;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int[] arr;
	static int n;
	
	static void printarr(int start, int end) {
		for(int i = start; i <= end; i++)
			System.out.print(arr[i] + " ");
	}

	static void mergesort(int start, int end) {
		if (start == end)
			return;
		int mid = (start + end) / 2;
		mergesort(start, mid);
		mergesort(mid + 1, end);
		merge(start, end);
		printarr(start, end);
		System.out.println();
	}
	
	static void merge(int start, int end) {
		int mid = (start + end) / 2;
		int left = start;
		int right = mid + 1;
		int idx = left;
		int[] temp = new int[n + 1];
		while (left <= mid && right <= end) {
			if (arr[left] > arr[right]) {
				temp[idx] = arr[right];
				idx++;
				right++;
			} else {
				temp[idx] = arr[left];
				idx++;
				left++;
			}
		}
		if (left > mid) {
			for (int i = right; i <= end; i++) {
				temp[idx] = arr[i];
				idx++;
			}
		} else {
			for (int i = left; i <= mid; i++) {
				temp[idx] = arr[i];
				idx++;
			}
		}
		for (int i = start; i <= end; i++)
			arr[i] = temp[i];

	}

	public static void main(String[] args) throws IOException {
		n = Integer.parseInt(br.readLine());
		arr = new int[n + 1];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < n; i++)
			arr[i] = Integer.parseInt(st.nextToken());
		// 0부터 n-1까지 sort
		mergesort(0, n - 1);
	}
}