import javax.swing.ImageIcon;

public class IconCollection {
	public final ImageIcon grayOldIcon = new ImageIcon("pic/grayOldIcon.jpg");
	public final ImageIcon grayOldIconWithFlag = new ImageIcon("pic/grayOldIconWithFlag.jpg");
	public final ImageIcon fullIcon = new ImageIcon("pic/fullIcon.JPG");
	public final ImageIcon greenExIcon = new ImageIcon("pic/greenExIcon.jpg");
	public final ImageIcon greenIcon2 = new ImageIcon("pic/greenIcon2.jpg");
	public final ImageIcon greenMoveExIcon = new ImageIcon("pic/greenMoveIcon.jpg");
	public final ImageIcon yellowIcon2 = new ImageIcon("pic/yellowIcon2.jpg");
	
	public final ImageIcon[] whiteIcon = new ImageIcon[9];
	
	public final ImageIcon flag = new ImageIcon("pic/flag.jpg");
	public final ImageIcon sweeper = new ImageIcon("pic/sweeper.png");
	public final ImageIcon sweeperTrue = new ImageIcon("pic/sweeperTrue.jpg");
	public final ImageIcon sweepableIcon = new ImageIcon("pic/sweepableIcon.jpg");
	public final ImageIcon sweeperOn = new ImageIcon("pic/sweeperOn.png");
	
	public final ImageIcon explode = new ImageIcon("pic/explode.gif");
    public final ImageIcon explodeSmall = new ImageIcon("pic/explodeSmall.gif");
    
    public final ImageIcon[] dice = new ImageIcon[7];
    
    public final ImageIcon[] hlGreenIcon = new ImageIcon[9];
    public final ImageIcon hlGrayOldIconWithFlag = new ImageIcon("pic/hlGrayOldIconWithFlag.jpg");
    
    public final ImageIcon[] blueExIcon = new ImageIcon[9];
    public final ImageIcon[] redIcon = new ImageIcon[9];
    public final ImageIcon[] greenIcon = new ImageIcon[9];
    public final ImageIcon[] yellowIcon = new ImageIcon[9];
    
    public IconCollection() {
    	whiteIcon[0] = new ImageIcon("pic/whiteIcon.JPG");
    	dice[0] = new ImageIcon("pic/dice96/dice.gif");
    	hlGreenIcon[0] = new ImageIcon("pic/hlGreenIcon.jpg");
    	blueExIcon[0] = new ImageIcon("pic/blueExIcon.jpg");
    	redIcon[0] = new ImageIcon("pic/redIcon.jpg");
    	greenIcon[0] = new ImageIcon("pic/greenIcon.jpg");
    	yellowIcon[0] = new ImageIcon("pic/yellowIcon.jpg");
    	
    	for(int i = 1 ; i < whiteIcon.length ; i++) {
    		whiteIcon[i] = new ImageIcon("pic/whiteIcon" + i + ".jpg");
    		hlGreenIcon[i] = new ImageIcon("pic/hlGreenIcon" + i + ".jpg");
    		blueExIcon[i] = new ImageIcon("pic/blueExIcon/blueExIcon" + i +".jpg");
    		redIcon[i] = new ImageIcon("pic/redIcon/redIcon" + i +".jpg");
    		greenIcon[i] = new ImageIcon("pic/greenIcon/greenIcon" + i +".jpg");
    		yellowIcon[i] = new ImageIcon("pic/yellowIcon/yellowIcon" + i +".jpg");
    	}
    	for(int i = 1 ; i < dice.length ; i++) {
    		dice[i] = new ImageIcon("pic/dice96/dice" + i + ".gif");
    	}
    }
 
}
