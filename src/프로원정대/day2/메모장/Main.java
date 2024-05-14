package 프로원정대.day2.메모장;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	static ArrayDeque<Character> leftQ = new ArrayDeque<>();
	static ArrayDeque<Character> rightQ = new ArrayDeque<>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		String s  = st.nextToken();
		
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			
			switch (c) {
				case '<':
					left();
					break;
				case '>':
					right();
					break;
				case 'b':
					back();
					break;
				case 'd':
					delete();
					break;
				case 'h':
					home();
					break;
				case 'e':
					end();
					break;
					
				default:
					write(c);
					break;
			}
			
		}
		
		while(!leftQ.isEmpty()) {
			char c = leftQ.pollFirst();
			System.out.print(c);
		}
		
		while(!rightQ.isEmpty()) {
			char c = rightQ.pollFirst();
			System.out.print(c);
		}
		
	}
	
	static void left() {
		if(leftQ.isEmpty()) return;
		char c = leftQ.pollLast();
		rightQ.addFirst(c);
	}
	
	static void right() {
		if(rightQ.isEmpty()) return;
		char c = rightQ.pollFirst();
		leftQ.addLast(c);
	}
	
	static void back() {
		if(leftQ.isEmpty()) return;
		leftQ.pollLast();
	}
	
	static void delete() {
		if(rightQ.isEmpty()) return;
		rightQ.pollFirst();
	}
	
	static void home() {
		if(leftQ.isEmpty()) return;
		while(!leftQ.isEmpty()) {
			char c = leftQ.pollLast();
			rightQ.addFirst(c);
		}
	}
	
	static void end() {
		if(rightQ.isEmpty()) return;
		while(!rightQ.isEmpty()) {
			char c = rightQ.pollFirst();
			leftQ.addLast(c);
		}
	}
	
	static void write(char c) {
		leftQ.add(c);
	}
}


/*
A<<<<<B>>>>S<<V>>>h>>>VBdhdQ


QVAVB
*/