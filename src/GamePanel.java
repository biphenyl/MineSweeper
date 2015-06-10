import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class GamePanel extends JPanel
{
	@Override
	 protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    Image img;
	        try
			{
	        	img = ImageIO.read(getClass().getResource("pic/explode.gif"));
				g.drawImage(img, 0, 0, null);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
