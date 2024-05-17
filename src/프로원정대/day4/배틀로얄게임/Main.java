package 프로원정대.day4.배틀로얄게임;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		HashMap<String, Integer> pointByTeam = new HashMap<>();
		HashMap<String, Integer> countByTeam = new HashMap<>();
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String team = st.nextToken();
			int num = Integer.parseInt(st.nextToken());
			
			int point = pointByTeam.getOrDefault(team, 0);
			int count = countByTeam.getOrDefault(team, 0);
			
			pointByTeam.put(team, point + num);
			countByTeam.put(team, count + 1);
		}
		
		st = new StringTokenizer(br.readLine());
		
		String team1 = st.nextToken();
		String team2 = st.nextToken();
		
		System.out.println(countByTeam.get(team1) + " " + pointByTeam.get(team1));
		System.out.println(countByTeam.get(team2) + " " + pointByTeam.get(team2));
		
		if(pointByTeam.get(team1) > pointByTeam.get(team2)) System.out.println(team1);
		else System.out.println(team2);
	}
}

