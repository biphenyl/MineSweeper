import java.util.Random;

public class Ground {

	private int width;
	private int height;
	private int[][] map; // 0: close   1: mine   2: open
	private int[][] mineNumber;
	private boolean[][] expanded;

	public Ground(int width, int height, int num) {
		this.width = width;
		this.height = height;
		expanded = new boolean[width][height];
		
		generateMap(num);
		countMine();
	}

	private void generateMap(int num) {
		Random rand = new Random();
		
		map = new int[width][height];

		while (num > 0) {
			int x, y;
			boolean succeed = false;

			while (!succeed) {
				x = rand.nextInt(width);
				y = rand.nextInt(height);
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
				map[i][height-1-j] = 0;
				map[width-1-i][j] = 0;
				map[width-1-i][height-j-1] = 0;
			}
		}
	}

	private void countMine() {
		mineNumber = new int[width][height];

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				int sum = 0;

				for (int x = -1 ; x < 2; ++x) {
					for (int y = -1; y < 2; ++y) {
						if (x + i >= 0 && x + i < width && y + j >= 0 && y + j < height && map[x+i][y+j] == 1)
							++sum;
					}
				}
				mineNumber[i][j] = sum;
			}
		}				
	}

	public void expand(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height && expanded[x][y] == false) {
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
			} else
				map[x][y] = 2;
		}
	}

	public void sweep(int x, int y) {
		map[x][y] = 2;
		countMine();
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
