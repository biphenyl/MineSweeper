import java.util.Random;

public class Ground {

	private int width;
	private int height;
	private int[][] map; // 0: close  1: mine  2: open  3: flag
	private int[][] mineNumber;
	private boolean[][] expanded;

	public Ground(int height, int width, int num) {
		this.width = width;
		this.height = height;
		expanded = new boolean[height][width];
		mineNumber = new int[height][width];
		
		generateMap(num);
		map[height/2][width/2] = 3;
		
		countMine();
	}
	public void generateFlag() {
		boolean succeed = false;
		Random rand = new Random();
		while (!succeed) {
			int x = rand.nextInt(height);
			int y = rand.nextInt(width);
			if (map[x][y] != 1) {//not pn mine
				map[x][y] = 3;//flag
				succeed = true;
			}
		}	
	}
	
	private void generateMap(int num) {
		Random rand = new Random();
		
		map = new int[height][width];

		while (num > 0) {
			int x, y;
			boolean succeed = false;

			while (!succeed) {
				x = rand.nextInt(height);
				y = rand.nextInt(width);
				if (map[x][y] == 0) {
					map[x][y] = 1;
					succeed = true;
				}
			}
			--num;
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				map[i][j] = 0;
				map[i][width-1-j] = 0;
				map[height-1-i][j] = 0;
				map[height-1-i][width-j-1] = 0;
			}
		}
	}
	
	public void generateflag() {
		boolean succeed=false;
		Random rand = new Random();
		int x, y;
		
		while (!succeed) {
			x = rand.nextInt(height);
			y = rand.nextInt(width);
			
			if (map[x][y] != 1) {//not pn mine
				map[x][y] = 3;//flag
				succeed = true;
			}	
		}	
	}

	private void countMine() {
		
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int sum = 0;

				for (int x = -1 ; x < 2; ++x) {
					for (int y = -1; y < 2; ++y) {
						if (x + i >= 0 && x + i < height && y + j >= 0 && y + j < width && map[x+i][y+j] == 1)
							++sum;
					}
				}
				mineNumber[i][j] = sum;
			}
		}				
	}

	public void expand(int x, int y) {
		if (x >= 0 && x < height && y >= 0 && y < width && expanded[x][y] == false) {
			if (mineNumber[x][y] == 0) {
				expanded[x][y] = true;
				map[x][y] = 2;
				expand(x + 1, y);
				expand(x - 1, y);
				expand(x, y + 1);
				expand(x, y - 1);
				expand(x + 1, y + 1);		
				expand(x + 1, y - 1);
				expand(x - 1, y + 1);
				expand(x - 1, y - 1);
			} else{
				if(map[x][y]!=3)
					map[x][y] = 2;
			}
		}
	}

	public void sweep(int x, int y) {
		map[x][y] = 2;
		countMine();
	}
	
	public void cleanFlag(int x, int y){
		if(map[x][y]==3){
			map[x][y] = 2; 
		}
	}

	public int[][] getMap() {
		return map;
	}
	
	public int[][] getMineNumber() {
		return mineNumber;
	}
	
	public int getMapXY(int x, int y){
		return map[x][y];
	}
	
	public int getMineNumXY(int x, int y){
		return mineNumber[x][y];
	}
/*
	public static void main(String[] args) {
		Ground g = new Ground(30, 30, 100);

		for (int i = 0; i < 30; ++i) {
			for (int j = 0 ; j < 30 ; ++j)
				System.out.print(g.getMap()[i][j] + " ");
			System.out.println("");
		}
		System.out.println("");


		g.expand(0, 0);
		g.expand(29, 29);
		g.expand(0, 29);
		g.expand(29, 0);

		for (int i = 0; i < 30; ++i) {
			for (int j = 0 ; j < 30 ; ++j)
			{
				if(g.getMap()[i][j] == 2)
				{
					if(g.getMineNumber()[i][j] == 0)
						System.out.print("  ");
					else
						System.out.print(g.getMineNumber()[i][j]+" ");					
				}
				else
					System.out.print("* ");
			}
			System.out.println("");
		}
		System.out.println("");

		for (int i = 0; i < 30; ++i) {
			for (int j = 0 ; j < 30 ; ++j)
				System.out.print(g.getMineNumber()[i][j] + " ");
			System.out.println("");
		}
		System.out.println("");
	}
*/
}
