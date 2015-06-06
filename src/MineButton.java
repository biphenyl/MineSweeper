import javax.swing.JButton;


public class MineButton extends JButton
{
	public int x, y;
	public int pos;
	public int lastState;
	public MineButton(int x, int y)
	{
		this.x = x;
		this.y = y;
		lastState = 0;
	}
}
