import javax.swing.ImageIcon;

public class Player {

	private int x;
	private int y;
	private int score;
	private int sweeperNumber;
	private int order;
	private int[] initPos = new int[2];
	private ImageIcon icon;

	public Player(int order) {
		x = 0;
		y = 0;
		this.order = order;
		score = 0;
		sweeperNumber = 10;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getScore() {
		return score;
	}

	public int getSweeperNumber() {
		return sweeperNumber;
	}
	
	public ImageIcon getIcon(){
		return icon;
	}
	
	public int getOrder(){
		return order;
	}
	
	public void respawn(){
		setXY(initPos[0], initPos[1]);
	}

	public void walk(int dx, int dy) {
		x += dx;
		y += dy;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setInitPos(int x, int y){
		initPos[0] = x;
		initPos[1] = y;
		this.x = x;
		this.y = y;
	}
	
	public void setIcon(ImageIcon imgName){
		icon = imgName;
	}
	
	public void addScore(int s) {
		score += s;
		if (score < 0) score = 0;
	}

	public void addSweeper(int n) {
		if (n < 0) return;
		sweeperNumber += n;
	}

	public int robbed(int n) {
		if (sweeperNumber <= 0 || n <= 0) return -1;

		if (sweeperNumber < n) n = sweeperNumber;
		sweeperNumber -= n;

		return n;
	}

	public int sweep() {
		if (sweeperNumber == 0) return -1;
		else {
			--sweeperNumber;
			return 1;
		}
	}

}
