import javax.swing.ImageIcon;

public class IconCollection {
	public final ImageIcon blueExIcon = new ImageIcon("pic/blueExIcon.jpg");
	public final ImageIcon grayOldIcon = new ImageIcon("pic/grayOldIcon.jpg");
	public final ImageIcon fullIcon = new ImageIcon("pic/fullIcon.JPG");
	public final ImageIcon greenExIcon = new ImageIcon("pic/greenExIcon.jpg");
	public final ImageIcon greenIcon = new ImageIcon("pic/greenIcon.jpg");
	public final ImageIcon greenIcon2 = new ImageIcon("pic/greenIcon2.jpg");
	public final ImageIcon greenMoveExIcon = new ImageIcon("pic/greenMoveIcon.jpg");
	public final ImageIcon redIcon = new ImageIcon("pic/redIcon.jpg");
	public final ImageIcon yellowIcon = new ImageIcon("pic/yellowIcon.jpg");
	public final ImageIcon yellowIcon2 = new ImageIcon("pic/yellowIcon2.jpg");
	
	public final ImageIcon whiteIcon = new ImageIcon("pic/whiteIcon.JPG");
	public final ImageIcon whiteIcon1 = new ImageIcon("pic/whiteIcon1.jpg");
	public final ImageIcon whiteIcon2 = new ImageIcon("pic/whiteIcon2.jpg");
	public final ImageIcon whiteIcon3 = new ImageIcon("pic/whiteIcon3.jpg");
	public final ImageIcon whiteIcon4 = new ImageIcon("pic/whiteIcon4.jpg");
	public final ImageIcon whiteIcon5 = new ImageIcon("pic/whiteIcon5.jpg");
	public final ImageIcon whiteIcon6 = new ImageIcon("pic/whiteIcon6.jpg");
	public final ImageIcon whiteIcon7 = new ImageIcon("pic/whiteIcon7.jpg");
	public final ImageIcon whiteIcon8 = new ImageIcon("pic/whiteIcon8.jpg");
	
	public final ImageIcon flag = new ImageIcon("pic/flag.jpg");
	
	public final ImageIcon explode = new ImageIcon("pic/explode.gif");
    public final ImageIcon explodeSmall = new ImageIcon("pic/explodeSmall.gif");
    
    public final ImageIcon diceRolling = new ImageIcon("pic/diceRolling.gif");
    public final ImageIcon dice1 = new ImageIcon("pic/dice1.gif");
    public final ImageIcon dice2 = new ImageIcon("pic/dice2.gif");
    public final ImageIcon dice3 = new ImageIcon("pic/dice3.gif");
    public final ImageIcon dice4 = new ImageIcon("pic/dice4.gif");
    public final ImageIcon dice5 = new ImageIcon("pic/dice5.gif");
    public final ImageIcon dice6 = new ImageIcon("pic/dice6.gif");
    
    public final ImageIcon getWhiteIcon(int n){
    	ImageIcon tmp = new ImageIcon();
    	switch(n){
    		case 0:
    			tmp = whiteIcon;
    			break;
    		case 1:
    			tmp = whiteIcon1;
    			break;
    		case 2:
    			tmp = whiteIcon2;
    			break;
    		case 3:
    			tmp = whiteIcon3;
    			break;
    		case 4:
    			tmp = whiteIcon4;
    			break;
    		case 5:
    			tmp = whiteIcon5;
    			break;
    		case 6:
    			tmp = whiteIcon6;
    			break;
    		case 7:
    			tmp = whiteIcon7;
    			break;
    		case 8:
    			tmp = whiteIcon8;
    			break;
    	}
    	return tmp;
    }
    public final ImageIcon getDice(int n){
    	ImageIcon tmp = new ImageIcon();
    	switch(n){
    		case 1:
    			tmp = dice1;
    			break;
    		case 2:
    			tmp = dice2;
    			break;
    		case 3:
    			tmp = dice3;
    			break;
    		case 4:
    			tmp = dice4;
    			break;
    		case 5:
    			tmp = dice5;
    			break;
    		case 6:
    			tmp = dice6;
    			break;
    	}
    	return tmp;
    }
}
