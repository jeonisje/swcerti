package 프로원정대.day2.인터넷;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	static ArrayDeque<String> backQ = new ArrayDeque<>();
	static ArrayDeque<String> frontQ = new ArrayDeque<>();
	static String current;
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			char cmd = st.nextToken().charAt(0);
			
			switch (cmd) {
			case 'A' :
				String site  = st.nextToken();
				access(site);
				break;
				
			case 'B' :
				back();
				break;
			case 'F' :
				front();
				break;
				
			case 'C' :
				compress();
				break;


			default:
				break;
			}
		}
		
		System.out.println(current);
		
		if(backQ.isEmpty()) {
			System.out.println("none");
		} else {
			while(!backQ.isEmpty()) {
				System.out.print(backQ.pollLast() + " ");
			}
			System.out.println();
		}
		
		if(frontQ.isEmpty()) {
			System.out.println("none");
		} else {
			while(!frontQ.isEmpty()) {
				System.out.print(frontQ.pollLast() + " ");
			}
			
		}
		
	}
	
	static void access(String site) {
		if(current != null)  {
			backQ.add(current);
		}
		frontQ.clear();
		current = site;
	}

	static void back() {
		if(backQ.isEmpty()) return;
		
		String bSite = backQ.pollLast();
		frontQ.add(current);
		current = bSite;		
	}
	
	static void front() {
		if(frontQ.isEmpty()) return;
		String fSite = frontQ.pollLast();
		backQ.add(current);
		current = fSite;			
	}
	
	static void compress() {
		ArrayList<String> sites = new ArrayList<>();
		String s = "";
		while(!backQ.isEmpty()) {
			String bSite = backQ.pollFirst();
			if(s.endsWith(bSite)) continue;
			sites.add(bSite);
			s = bSite;
		}
		
		for(String site : sites) {
			backQ.add(site);			
		}
	} 
}
