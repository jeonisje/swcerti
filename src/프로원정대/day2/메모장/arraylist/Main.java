package 프로원정대.day2.메모장.arraylist;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static ArrayList<Character> leftString = new ArrayList<>();
	static ArrayList<Character> rightString = new ArrayList<>();
	
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
		
		for(char c : leftString) {
			System.out.print(c);
		}
		
		for(int i = rightString.size() - 1; i>=0; i--) {
			System.out.print(rightString.get(i));
		}
	}
	
	static void left() {
		if(leftString.isEmpty()) return;
		int lastIndex = leftString.size() - 1;
		char c = leftString.get(lastIndex);
		rightString.add(c);
		leftString.remove(lastIndex);
	}
	
	static void right() {
		if(rightString.isEmpty()) return;
		int lastIndex = rightString.size() - 1;
		char c = rightString.get(lastIndex);
		leftString.add(c);
		rightString.remove(lastIndex);
	}
	
	static void back() {
		if(leftString.isEmpty()) return;
		int lastIndex = leftString.size() - 1;
		leftString.remove(lastIndex);
	}
	
	static void delete() {
		if(rightString.isEmpty()) return;
		int lastIndex = rightString.size() - 1;
		rightString.remove(lastIndex);
	}
	
	static void home() {
		if(leftString.isEmpty()) return;
		for(int i = leftString.size() - 1; i>=0; i--) {
			char c = leftString.get(i);
			rightString.add(c);
		}
		leftString.clear();
	}
	
	static void end() {		
		if(rightString.isEmpty()) return;
		for(int i = rightString.size() - 1; i>=0; i--) {
			char c = rightString.get(i);
			leftString.add(c);
		}
		rightString.clear();
	}
	
	static void write(char c) {
		leftString.add(c);
	}
}



/*
A<<<<<B>>>>S<<V>>>h>>>VBdhdQ


QVAVB
*/