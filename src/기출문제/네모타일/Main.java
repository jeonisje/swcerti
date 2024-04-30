package 기출문제.네모타일;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;
	
class UserSolution {	
	final int MAX_LOCATION = 50_001;
	final int KEY_MAKER = 100_000;
	final int NUT = 10;
	
	final int[][] keybyLocation1 = {{10, 0, 20}, {0, 30, 0}, {40, 0, 50}};
	final int[][] keybyLocation2 = {{10000, 0, 1000}, {0, 100, 0}, {10, 0, 1}};
	final int[] keyConstant = {0, 10000, 1000, 100, 10, 1};
	
	
	int N;
	int[][] map;
	int[][] tileMap;
	
	int locationId;
	
	int[] usedLocation;
	
	HashMap<Integer, TreeSet<Location>> wallMap;	
	HashMap<Integer, Location> locationIdByTile;
	
	public void init(int N, int[][] mInfo) {
		this.N = N;
		this.map = mInfo;
		
		tileMap = new int[N][N];
		
		locationId = 0;
		locationIdByTile = new HashMap<>();
		usedLocation = new int[MAX_LOCATION];		
		wallMap = new HashMap<>();
		
		setWall();		
      	return; 
	}
	
	void setWall() {
		for(int i=0; i<N-2; i++) {
			int startJ = 0;
			int endJ = N - 2;
			if(i % 2 == 1) {
				startJ = 1;
				endJ = N - 3;
			}
			
			for(int j=startJ; j < endJ; j+=2) {
				int key = getWallKey(i, j);
				if(key != -1) {
					locationId++;				
					TreeSet<Location> set = wallMap.getOrDefault(key, new TreeSet<Location>((o1, o2) -> o1.row == o2.row ? Integer.compare(o1.col, o2.col) : Integer.compare(o1.row, o2.row)));
					Location loc = new Location(locationId, i, j, key);
					set.add(loc);
					wallMap.put(key, set);
				}
			}
		}
	}
	
	int getWallKey(int row, int col) {
		int key = 0;
		int count = 0;
		
		for(int i=row; i<row+3; i++) {
			for(int j=col; j<col+3; j++) {
				if(map[i][j] == 0) continue;
				if(map[i][j] < 10) {
					key += (map[i][j] + keybyLocation1[i-row][j-col]) * KEY_MAKER;
					count++;
				} else {
					key += (map[i][j]  - 10) * keybyLocation2[i-row][j-col];
				}
				
				if(count >= 2) return -1;
			}
		}
		
		return key;
	}

	public int addRectTile(int mID, int[][] mTile) {
		int ret = -1;
		int tileKey = getTileKey(mTile);
		
		int minRow = Integer.MAX_VALUE;
		int minCol = Integer.MAX_VALUE;
		Location minLoc = null;
		int key = -1;
		if(tileKey > KEY_MAKER) {
			TreeSet<Location> set1 = wallMap.get(tileKey);
			if(set1 != null) {
				for(Location loc : set1) {
					//if(usedLocation[loc.id] == 1) continue;
					if(!match(loc, mTile)) continue;
					if(!isAvailable(loc)) continue;
					
					//if(loc.row < minLoc.r) {
						minRow = loc.row;
						minCol = loc.col;
						minLoc = loc;
						key = tileKey;
						break;
					//}
				}
			}
			
			int nutLoc = tileKey / KEY_MAKER / 10;
			int tileKey2 = tileKey % (KEY_MAKER);
			
			for(int i=1; i<=5; i++) {
				int newTileKey = tileKey2 + (i * keyConstant[nutLoc]);
				TreeSet<Location> set2 = wallMap.get(newTileKey);
				if(set2 != null) {
					for(Location loc : set2) {
						//if(usedLocation[loc.id] == 1) continue;
						if(!match(loc, mTile)) continue;
						if(!isAvailable(loc)) continue;
						
						if(loc.row < minRow) {
							minRow = loc.row;
							minCol = loc.col;
							minLoc = loc;
							key = newTileKey;
							break;
						} else if(loc.row == minRow) {
							if(loc.col < minCol) {
								minRow = loc.row;
								minCol = loc.col;
								minLoc = loc;	
								key = newTileKey;
								break;
							}
						}
					}
				}
			}			
					
		} else {
			TreeSet<Location> set = wallMap.get(tileKey);
			if(set != null) {
				for(Location loc : set) {
					//if(usedLocation[loc.id] == 1) continue;
					if(!match(loc, mTile)) continue;
					if(!isAvailable(loc)) continue;					
					//if(loc.row < minRow) {
						minRow = loc.row;
						minCol = loc.col;
						minLoc = loc;			
						key = tileKey;
						break;
					//}
				}
			}
		}
		
		
		if(minLoc != null) {
			ret = minLoc.row * 10_000 + minLoc.col;
			//usedLocation[minLoc.id] = mID;
			locationIdByTile.put(mID, minLoc); 
			wallMap.get(key).remove(minLoc);
			setTile(minLoc, mID);
		}
		
		return ret;
	}
	
	int getTileKey(int[][] tile) {
		int key = 0;
		
		if(tile[0][0] > 10) {
			key +=  tile[0][0] * KEY_MAKER; 
		} else {
			key +=  tile[0][0] * 10_000;
		}
		if(tile[0][2] > 10) {
			key +=  (tile[0][2] - 10 + 20) * KEY_MAKER; 
		} else {
			key +=  tile[0][2] * 1_000;
		}
		if(tile[1][1] > 10) {
			key +=  (tile[1][1] - 10 + 30) * KEY_MAKER; 
		} else {
			key +=  tile[1][1] * 100;
		}
		
		if(tile[2][0] > 10) {
			key +=  (tile[2][0] - 10 + 40) * KEY_MAKER; 
		} else {
			key +=  tile[2][0] * 10;
		}
		if(tile[2][2] > 10) {
			key +=  (tile[2][2] - 10 + 50) * KEY_MAKER; 
		} else {
			key +=  tile[2][2];
		}
		
		/*
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(tile[i][j] == 0) continue;
				if(tile[i][j] > 10) {
					key += (tile[i][j] + keybyLocation1[i][j] - 10) * KEY_MAKER;
					
				} else {
					key += tile[i][j] * keybyLocation2[i][j];
				}
			}
		}*/
		
		return key;
	}
	
	boolean match(Location loc, int[][] tile) {
		int matched = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(tile[i][j] == 0) continue;
				matched += check(map[loc.row + i][loc.col + j], tile[i][j]);
			}
		}
		
		return matched == 5 ? true : false;
	}
	
	int check(int a, int b) {
		if(a > NUT && b > NUT) return 1;
		if(a % NUT == b % NUT) return 1;
		
		return 0;
	}
	
	boolean isAvailable(Location loc) {
		for(int i=loc.row; i<loc.row + 3; i++) {
			for(int j=loc.col; j<loc.col + 3; j++) {
				if(tileMap[i][j] != 0) return false;
			}
		}
		return true;
	}
	
	void setTile(Location loc, int tileId) {
		for(int i=loc.row; i<loc.row + 3; i++) {
			for(int j=loc.col; j<loc.col + 3; j++) {
				tileMap[i][j] = tileId;
			}
		}
	}
	
	public void removeRectTile(int mID) {
		if(!locationIdByTile.containsKey(mID)) return;
		
		Location loc = 	locationIdByTile.get(mID);
		
		usedLocation[loc.id] = 0;
		locationIdByTile.remove(mID); 
		setTile(loc, 0);
		
		
		wallMap.get(loc.key).add(loc);
			
		return;
    }
	
	class Location {
		int id;
		int row;
		int col;
		int key;
		public Location(int id, int row, int col, int key) {
			super();
			this.id = id;
			this.row = row;
			this.col = col;
			this.key = key;
		}
	}
}

public class Main {
	
	private static final int MAX_N 		= 999;
	private static final int MAX_SHAPE 	= 10;
	
	private static BufferedReader br;
	private static StringTokenizer st;

	private static final int CMD_INIT 			= 0;
	private static final int CMD_ADD 			= 1;
	private static final int CMD_REMOVE			= 2;
	
	private static int[] dy = { 0, 0, 1, 2, 2 };
	private static int[] dx = { 0, 2, 1, 0, 2 };
	
	static int mSeed;
	static int pseudo_rand() {
	    mSeed = mSeed * 214013 + 2531011;
	    return (mSeed >> 16) & 0x7fff;
	}
	 
	static int[] Shape = { 1, 2, 3, 4, 5, 11, 12, 13, 14, 15 };
	static int[][] Info = new int[MAX_N][MAX_N];
	static int[][] Data = new int[3][3]; 
	
	static int N, mID; 
	
	static void info_init() {
	    for (int i = 0; i < 3; i++) 
	        for (int k = 0; k < 3; k++) 
	            Data[i][k] = 0;

	    for (int i = 0; i < MAX_N; i++) 
	        for (int k = 0; k < MAX_N; k++) 
	            Info[i][k] = 0;
	}
	
	static void make_info() {
	    for (int y = 0; y < N; y += 2) {
	        for (int x = 0; x < N; x += 2) {
	            Info[y][x] = Shape[pseudo_rand() % MAX_SHAPE];
	            if (y + 1 < N && x + 1 < N) Info[y + 1][x + 1] = Shape[pseudo_rand() % MAX_SHAPE];
	        }
	    }
	}
	
	static void make_info_1() throws Exception {
	    for (int y = 0; y < N; y++) {
	    	st = new StringTokenizer(br.readLine());
	        for (int x = 0; x < N; x++) {
	            Info[y][x] = Integer.parseInt(st.nextToken());
	        }
	    }
	}
	
    private static UserSolution userSolution = new UserSolution();

    private static boolean run(BufferedReader br) throws Exception {

		int query_num, sample_1; 
		
		st = new StringTokenizer(br.readLine(), " ");
		query_num = Integer.parseInt(st.nextToken());
		sample_1 = Integer.parseInt(st.nextToken());
		
		int ans;
		boolean ok = false; 
		
		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				mSeed = Integer.parseInt(st.nextToken());
				if(sample_1 == 1)
					make_info_1();
				else
					make_info();
				mID = 0;
				userSolution.init(N, Info); 
				ok = true;
				break;
			case CMD_ADD:
				mID++;
				for(int i = 0; i < 5; i++) 
					Data[dy[i]][dx[i]] = Integer.parseInt(st.nextToken());
				int ret = userSolution.addRectTile(mID, Data);
				ans = Integer.parseInt(st.nextToken());
				print(q, "addRectTile", ans, ret, mID, Data);
				if(ans != ret)
					ok = false; 
				break;
			case CMD_REMOVE:
				int id = Integer.parseInt(st.nextToken());
				userSolution.removeRectTile(id); 
				print(q, "removeRectTile", q, q, id);
				break;
			default:
				ok = false;
				break;
			}
		}
		return ok;
	}
    
    static void print(int q, String cmd, int ans, int ret, Object...o) {
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }
    
    
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\네모타일\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}