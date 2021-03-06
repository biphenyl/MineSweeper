import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import javax.crypto.spec.IvParameterSpec;
import javax.management.MBeanAttributeInfo;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingConstants;

public class GUI extends JFrame
{

	private StartGUI father;
	private JPanel contentPane, rightPanel, leftPanel;
	private JLabel lbMovement, lbGameMsg, lbStrenth1, lbStrenth2;
	private ArrayList<Player> players;
	private GUI frame;
	private int state=2, mp, nowPlayer;	//state : 0=moving, 1=sweeping, 2=throwing dice
	private int playerNum, width, height, totalMine, sweepNum;
	private int strenth1, strenth2;
	private int dontCleanX, dontCleanY, volumeState;
	private long msgTime;
	private Player fighter1, fighter2;
	private ArrayList<JComponent> guiComponents_btn = new ArrayList<JComponent>(900);
	private ArrayList<JComponent> guiComponents_label = new ArrayList<JComponent>(100);
	private Dice dice;
	private Ground ground;
	private JButton diceButton, sweeperButton;
	private IconCollection icon = new IconCollection();
	
	private final int moving = 0;
	private final int sweeping = 1;
	private final int dicing = 2;
	private final int fightingPart1 = 3;
	private final int fightingPart2 = 4;
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("deprecation")
	public GUI(final StartGUI father, int playerNum, int height, int width, int totalMine)
	{
		this.playerNum = playerNum;
		this.width = width;
		this.height = height;
		this.totalMine = totalMine;
		this.father = father;
		sweepNum =  (height+width)*totalMine/(2*height*width);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new CloseListener());
		setBounds(100, 100, 1250, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 1200, 1000);
		background.setIcon(icon.bigMine);
		//contentPane.add(background);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		
		rightPanel = new JPanel();
		rightPanel.setBounds(934, 33, 248, 920);
		contentPane.add(rightPanel);
		rightPanel.setLayout(null);
		rightPanel.setOpaque(false);
		
		diceButton = new JButton(icon.dice[4]);
		diceButton.setPressedIcon(icon.dice[0]);
		diceButton.setBounds(20, 811, 96, 96);
		diceButton.addActionListener(new DiceListener());
		rightPanel.add(diceButton);
		
		sweeperButton = new JButton(icon.sweeper);
		sweeperButton.setBounds(126, 811, 96, 96);
		sweeperButton.addActionListener(new SweepListener());
		rightPanel.add(sweeperButton);
	
		int[] yLabel = {33, 153, 273, 393};
		for(int i=0; i<playerNum; ++i){
			JLabel lb = new JLabel((i+1) + "P");
			lb.setFont(new Font("Arial", Font.PLAIN, 36));
			lb.setBounds(33, yLabel[i], 60, 40);
			guiComponents_label.add(lb);
			rightPanel.add(lb);
			
			lb = new JLabel("分數: 0");
			lb.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
			lb.setBounds(33, yLabel[i]+40, 135, 27);
			guiComponents_label.add(lb);
			rightPanel.add(lb);
			
			lb = new JLabel("掃雷器: " + sweepNum);
			lb.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
			lb.setBounds(33, yLabel[i]+67, 135, 27);
			guiComponents_label.add(lb);
			rightPanel.add(lb);
		}
		
		JLabel lbMention = new JLabel("達到1000分");
		lbMention.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbMention.setBounds(33, yLabel[playerNum-1]+120, 300, 27);
		guiComponents_label.add(lbMention);
		rightPanel.add(lbMention);
		
		lbMention = new JLabel("          以獲得勝利！");
		lbMention.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbMention.setBounds(33, yLabel[playerNum-1]+155, 300, 27);
		guiComponents_label.add(lbMention);
		rightPanel.add(lbMention);
		
		lbMovement = new JLabel("1P 請擲骰子");
		lbMovement.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbMovement.setBounds(20, 751, 208, 27);
		rightPanel.add(lbMovement);
		
		lbStrenth2 = new JLabel("");
		lbStrenth2.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbStrenth2.setBounds(20, 711, 167, 27);
		rightPanel.add(lbStrenth2);
		
		lbStrenth1 = new JLabel("");
		lbStrenth1.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbStrenth1.setBounds(20, 671, 167, 27);
		rightPanel.add(lbStrenth1);
		
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 33, 934, 920);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		leftPanel.setOpaque(false);
		
		lbGameMsg = new JLabel("");
		lbGameMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lbGameMsg.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		lbGameMsg.setBounds(14, 1, 1071, 26);
		contentPane.add(lbGameMsg);
		
		JButton btnRestart = new JButton("重新開始");
		btnRestart.setBounds(1119, 1, 99, 27);
		btnRestart.addActionListener(new RestartListener());
		contentPane.add(btnRestart);
		
		final JButton btnMute = new JButton();
		btnMute.setIcon(icon.mute);
		btnMute.setBounds(1080, 1, 32, 32);
		btnMute.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				if(volumeState==1){
					SoundEffect.volume = SoundEffect.volume.MUTE;
					SoundEffect.BGM.loopStop();
					btnMute.setIcon(icon.volume);
					volumeState = 0;
				}
				else{
					SoundEffect.volume = SoundEffect.volume.ON;
					SoundEffect.BGM.alwaysPlay();
					btnMute.setIcon(icon.mute);
					volumeState = 1;
				}
			}
		});
		contentPane.add(btnMute);
		
		int startX = (920-30*height)/2;
		int startY = (934-30*width)/2;
		for(int i=0; i<height; ++i)
		{
			for(int j=0; j<width; ++j)
			{
				MineButton btn = new MineButton(i, j);
				btn.setSize(30, 30);
				btn.setLocation(startY+j*30, startX+i*30);
				btn.addActionListener(new ButtonListener());
				btn.pos = i*width+j;
				guiComponents_btn.add(btn);
				leftPanel.add(btn);
			}
		}

		ground = new Ground(height, width, totalMine);	
		initPlayer();
		
		dice = new Dice();
		
		SoundEffect.init();
		SoundEffect.volume = SoundEffect.Volume.ON;
		volumeState = 1;
		
		dontCleanY = -1;
		dontCleanX = -1;
		msgTime = new Date().getTime();
		
		rePaint();
	}

	/**
	 * Launch the application.
	 */
	public void run()
	{
		try
		{
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			BGMThread b = new BGMThread();
			b.start();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private void initPlayer()
	{
		players = new ArrayList<Player>(playerNum);
		for(int i=0; i<playerNum; ++i)
			players.add(new Player(i));
		
		players.get(0).setInitPos(1, 1);
		players.get(0).setIcon(icon.redIcon);
		players.get(0).addScore(970);
		players.get(0).addSweeper(sweepNum);
		
		JLabel lb = new JLabel();
		lb.setBounds(80, 35, 32, 32);
		lb.setIcon(players.get(0).getIcon()[0]);
		rightPanel.add(lb);
		
		players.get(1).setInitPos(height-2, 1);
		players.get(1).setIcon(icon.blueExIcon);
		players.get(1).addSweeper(sweepNum);
		
		lb = new JLabel();
		lb.setBounds(80, 155, 32, 32);
		lb.setIcon(players.get(1).getIcon()[0]);
		rightPanel.add(lb);
		
		if(playerNum>=3){
			players.get(2).setInitPos(1, width-2);
			players.get(2).setIcon(icon.greenIcon);
			players.get(2).addSweeper(sweepNum);
			
			lb = new JLabel();
			lb.setBounds(80, 275, 32, 32);
			lb.setIcon(players.get(2).getIcon()[0]);
			rightPanel.add(lb);
		}
		if(playerNum==4){
			players.get(3).setInitPos(height-2, width-2);
			players.get(3).setIcon(icon.yellowIcon);
			players.get(3).addSweeper(sweepNum);
			
			lb = new JLabel();
			lb.setBounds(80, 395, 32, 32);
			lb.setIcon(players.get(3).getIcon()[0]);
			rightPanel.add(lb);
		}
		
		nowPlayer = 0;
		
		ground.expand(1, 1);
		ground.expand(height-2, 1);
		if(playerNum>=3)
			ground.expand(1, width-2);
		if(playerNum==4)
			ground.expand(height-2, width-2);
	}
	
	private void nextTurn()
	{
		state = dicing;
		
		sweeperButton.setIcon(icon.sweeper);
		if(++nowPlayer >= playerNum)
			nowPlayer = 0;
		
		lbMovement.setText((nowPlayer+1) + "P 請擲骰子");
		System.out.println("醬汁");
		
	}
	
	private void rePaint(){
		
		int[][] map = ground.getMap();
		int[][] mineNumber = ground.getMineNumber();
		MineButton mb;
		// test to show mines
		for(int i=0; i<height; ++i){
			for(int j=0; j<width; ++j){
				if(i==dontCleanX && j==dontCleanY){
					dontCleanX = -1;
					dontCleanY = -1;
					continue;
				}
				if(map[i][j]==0){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.grayOldIcon);
				}
				else if(map[i][j]==1){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.grayOldIcon);					
				}
				else if(map[i][j]==2){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.whiteIcon[mineNumber[i][j]]);
				}
				else if(map[i][j]==3){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.grayOldIconWithFlag);			
				}
			}
		}
		
		for(int i=0; i<playerNum; ++i)
		{
			int x = players.get(i).getX();
			int y = players.get(i).getY();
			mb = (MineButton)guiComponents_btn.get(x*width+y);
			mb.setIcon(players.get(i).getIcon()[mineNumber[x][y]]);
		}
	}
	
	private void highLightMove(int x, int y)
	{
		MineButton mb;
		ImageIcon moveHL = icon.greenMoveExIcon;
		int mbState;
		
		if(x>0)
		{
			mbState = ground.getMapXY(x-1, y);
			mb = (MineButton)guiComponents_btn.get(x*width+y-width);
			if(mbState==0){
				mb.setIcon(moveHL);
			}
			else if(mbState==1){
				mb.setIcon(moveHL);
			}
			else if(mbState==2){
				mb.setIcon(icon.hlGreenIcon[ground.getMineNumXY(x-1, y)]);
			}
			else if(mbState==3){
				mb.setIcon(icon.hlGrayOldIconWithFlag);
			}
		}
		
		if(x<height-1)
		{
			mbState = ground.getMapXY(x+1, y);
			mb = (MineButton)guiComponents_btn.get(x*width+y+width);
			
			if(mbState==0){
				mb.setIcon(moveHL);
			}
			else if(mbState==1){
				mb.setIcon(moveHL);
			}
			else if(mbState==2){
				mb.setIcon(icon.hlGreenIcon[ground.getMineNumXY(x+1, y)]);
			}
			else if(mbState==3){
				mb.setIcon(icon.hlGrayOldIconWithFlag);
			}
		}
		
		if(y>0)
		{
			mbState = ground.getMapXY(x, y-1);
			mb = (MineButton)guiComponents_btn.get(x*width+y-1);
			if(mbState==0){
				mb.setIcon(moveHL);
			}
			else if(mbState==1){
				mb.setIcon(moveHL);
			}
			else if(mbState==2){
				mb.setIcon(icon.hlGreenIcon[ground.getMineNumXY(x, y-1)]);
			}
			else if(mbState==3){
				mb.setIcon(icon.hlGrayOldIconWithFlag);
			}
		}
		
		if(y<width-1)
		{
			mbState = ground.getMapXY(x, y+1);
			mb = (MineButton)guiComponents_btn.get(x*width+y+1);
			if(mbState==0){
				mb.setIcon(moveHL);
			}
			else if(mbState==1){
				mb.setIcon(moveHL);
			}
			else if(mbState==2){
				mb.setIcon(icon.hlGreenIcon[ground.getMineNumXY(x, y+1)]);
			}
			else if(mbState==3){
				mb.setIcon(icon.hlGrayOldIconWithFlag);
			}
		}
		
		for(int i=0; i<playerNum; ++i){
			Player p = players.get(i);
			if(moveable(p.getX(), p.getY())){
				mb = (MineButton)guiComponents_btn.get(p.getX()*width+p.getY());
				mb.setIcon(p.getIcon()[9]);
			}
		}
	}
	
	private boolean moveable(int x, int y)
	{
		Player p = players.get(nowPlayer);
		int rx, ry;
		rx = x - p.getX();
		ry = y - p.getY();
		
		if(rx == 0 && (ry == -1 || ry == 1))
			return true;
		else if(ry == 0 && (rx == -1 || rx ==1))
			return true;
		else 
			return false;
	}
	
	private void playerMove(int x, int y)
	{
		Player p = players.get(nowPlayer);
		
		if(ground.getMapXY(x, y)==1){
			die(p, x, y);
			ground.sweep(x, y);
			return;
		}
		else if(ground.getMapXY(x, y)==3){
			getFlag(x, y);
		}
		else{
			for(int i=0; i<playerNum; ++i){
				if(i==nowPlayer) 
					continue;
				
				Player enemy = players.get(i);
				if(x==enemy.getX() && y==enemy.getY()){
					fight(p, enemy);
					return;
				}
			}
		}
		
		p.setXY(x, y);
		System.out.println(x + "<move>" + y);
		updateMP(--mp);
		
		ground.expand(x, y);
	}
	
	private void highLightSweep(int x, int y){
		MineButton mb;
		ImageIcon sweepHL = icon.sweepableIcon;
		int mbState;
		
		rePaint();
		int[][] step = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
		
		for(int i=0; i<8; ++i){
			int a = x+step[i][0];
			int b = y+step[i][1];
			if(a>=0 && a<=height-1 && b>=0 && b<=width-1){
				mb = (MineButton)guiComponents_btn.get(a*width+b);
				mbState = ground.getMapXY(a, b);
				if(mbState==0 || mbState==1){
					mb.setIcon(sweepHL);
				}
			}
		}
	}
	
	private boolean sweepable(int x,  int y)
	{
		Player p = players.get(nowPlayer);
		int rx, ry;
		rx = x-p.getX();
		ry = y-p.getY();
		System.out.println("rx: " + rx + ", ry: " + ry);
		if(!(rx==0 && ry==0)){
			if((rx >= -1 && rx <= 1) && (ry >= -1 && ry <= 1)){
				if(ground.getMapXY(x, y)!=2 && ground.getMapXY(x, y)!=3)
					return true;
			}
		}
		return false;
	}
	
	
	private void sweep(int x, int y)
	{
		Player p = players.get(nowPlayer);
		if(p.getSweeperNumber()<=0){
			updateGameMsg(lbGameMsg, "你沒有掃雷器了");
			return;
		}
		
		if(ground.getMapXY(x, y)==1){
			updateScore(p, 30);
			updateGameMsg(lbGameMsg, "掃雷成功！　" + (players.get(nowPlayer).getOrder()+1) + "P得到30分");
		}
		else
			updateGameMsg(lbGameMsg, "沒掃到東西ㄏㄏ");
		
		p.addSweeper(-1);
		JLabel lb = (JLabel)guiComponents_label.get(nowPlayer*3+2);
		lb.setText("掃雷器: " + p.getSweeperNumber());
		System.out.println("sweeper:" + p.getSweeperNumber());
		MineButton mb = (MineButton)guiComponents_btn.get(x*width+y);
		mb.setIcon(icon.whiteIcon[ground.getMineNumXY(x, y)]);
		ground.sweep(x, y);
		updateMP(--mp);
	}

	private void updateScore(Player p, int score)
	{
		p.addScore(score);
		JLabel lb = (JLabel)guiComponents_label.get(p.getOrder()*3+1);
		lb.setText("分數: " + Integer.toString(p.getScore()));
		
		System.out.println("Score updated!");
	}
	
	private void updateMP(int n)
	{
		lbMovement.setText("剩餘步數: " + mp);
	}
	
	private void updateGameMsg(JLabel lb, String msg)
	{
		final JLabel templb = lb;
		lb.setText(msg);
		
		int delay = 7000; 
		ActionListener msgPlayer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(new Date().getTime()-msgTime>=7000)
					templb.setText("");
			}
		};
		Timer timer = new Timer(delay, msgPlayer);
		timer.setRepeats(false);
		timer.start();
		
		msgTime = new Date().getTime();
	}
	
	private void cleanStrenth(){
		int delay = 3000; 
		ActionListener msgPlayer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lbStrenth1.setText("");
				lbStrenth2.setText("");
			}
		};
		Timer timer = new Timer(delay, msgPlayer);
		timer.setRepeats(false);
		timer.start();
	}
	
	private void fight(Player p1, Player p2){
		//show fight animation
		if(state==moving){
			fighter1 = p1;
			fighter2 = p2;
			updateGameMsg(lbGameMsg, "開始戰鬥！請" + Integer.toString(p1.getOrder()+1) +"P先擲骰子");
			lbMovement.setText("");
			SoundEffect.BEFOREBATTLE.play();
			state = fightingPart1;
		}
		else if(state==fightingPart1){
			updateGameMsg(lbGameMsg, "換" + Integer.toString(p2.getOrder()+1) +"P擲骰子");
			
			state = fightingPart2;
		}
		else if(state==fightingPart2){
			if(strenth1<strenth2){
				die(p1, p1.getX(), p1.getY());
				updateScore(p2, p1.getScore()/2);
				updateScore(p1, -(p1.getScore()/2));
				updateGameMsg(lbGameMsg, (fighter2.getOrder()+1) + "P獲勝！ 進入下個回合");
			}
			else if(strenth1>strenth2){
				die(p2, p2.getX(), p2.getY());
				updateScore(p1, p2.getScore()/2);
				updateScore(p2, -(p2.getScore()/2));
				updateGameMsg(lbGameMsg, (fighter1.getOrder()+1) + "P獲勝！進入下個回合");
			}
			else{
				updateGameMsg(lbGameMsg, "平手！進入下個回合");
			}
			cleanStrenth();
			nextTurn();
			lbMovement.setText("請" + (nowPlayer+1) + "P擲骰子!");
			
			victoryCheck();
		}
	}
	
	private void die(Player p, int x, int y)
	{
		MineButton mb = (MineButton)guiComponents_btn.get(x*width+y);
		mb.setIcon(icon.explodeSmall);
		mb.setRolloverIcon(icon.explodeSmall);
		if(state==moving)
		{
			mp = 0;
			updateScore(p, -100);
			System.out.println((p.getOrder()+1) + "P killed by mine");
		}
		else {
			System.out.println((p.getOrder()+1) + "P killed by player");
		}
		SoundEffect.EXPLODE.play();
		
		final int x1 = x, y1 = y;
		dontCleanX = x;
		dontCleanY = y;
		
		int delay = 1000; 
		
		ActionListener exploder = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MineButton mb = (MineButton)guiComponents_btn.get(x1*width+y1);
				mb.setIcon(icon.whiteIcon[ground.getMineNumXY(x1, y1)]);
				mb.setRolloverIcon(null);
				
			}
		};
		Timer timer = new Timer(delay, exploder);
		timer.setRepeats(false);
		timer.start();
		
		p.respawn();
		mb = (MineButton)guiComponents_btn.get(p.getX()*width+p.getY());
		mb.setIcon(p.getIcon()[0]);
	}
	
	private void getFlag(int x, int y){
		ground.cleanFlag(x, y);
		updateScore(players.get(nowPlayer), 300);
		ground.generateFlag();
		
		JLabel lb;
		updateGameMsg(lbGameMsg, (players.get(nowPlayer).getOrder()+1) + "P搶到一個旗子，得到300分，所有人掃雷器獲得補充");
		for(int i=0; i<playerNum; ++i){
			if(i==nowPlayer)
				players.get(i).addSweeper(sweepNum);
			else 
				players.get(i).addSweeper(sweepNum/2);
			
			lb = (JLabel)guiComponents_label.get(i*3+2);
			lb.setText("掃雷器: " + players.get(i).getSweeperNumber());
		}
	}
	
	private void victoryCheck()
	{
		for(int i=0; i<playerNum; ++i){
			if(players.get(i).getScore()>=1000){
				victory(i);
				break;
			}
		}
	}

	private void victory(int pn)
	{
		System.out.println("Victory");
		int[][] map = ground.getMap();
		MineButton mb;
		for(int i=0; i<height; ++i){
			for(int j=0; j<width; ++j){
				if(map[i][j]==1){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.explodeSmall);
				}
			}
		}
		SoundEffect.BGM.loopStop();
		SoundEffect.VICTORY.play();
		
		int mType=JOptionPane.INFORMATION_MESSAGE;
		String options[] = {"重新開始", "離開"};
		int opt=JOptionPane.showOptionDialog(frame,(pn+1)+"P獲勝了！要重新開始嗎？","勝利！",
                JOptionPane.YES_NO_OPTION,mType, null, options, "取消");
		
		if(opt==JOptionPane.YES_OPTION){
			father.run();
			dispose();
		}
		else if(opt==JOptionPane.NO_OPTION){
			System.exit(0);
		}
	}

	class BGMThread extends Thread {
		@Override
		public void run() {
			
			SoundEffect.BGM.alwaysPlay();
		}
	}

	class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			MineButton mb = (MineButton) e.getSource();
            if (e.getSource() instanceof MineButton) 
            {
            	int x = mb.x;
        		int y = mb.y;
            	switch(state){
            	case moving:
	                if(moveable(x, y)){	
        				playerMove(x, y);
        				rePaint();
            			
            			System.out.println("mp: "+ mp + " state: " + state);
            			if(mp>0 && state==moving){
            				highLightMove(x, y);
            				mb.setIcon(players.get(nowPlayer).getIcon()[ground.getMineNumXY(x, y)]);
            			}	
        			}
        			else {
        				updateGameMsg(lbGameMsg, "請選擇可移動的格子");
					}
	                break;
            	case sweeping:
            		if(sweepable(x, y)){
            			sweep(x, y);
            			rePaint();
            			
            			if(mp>0){
            				Player p = players.get(nowPlayer);
            				highLightSweep(p.getX(), p.getY());
            			}
            		}
            		else{
            			updateGameMsg(lbGameMsg, "請選擇可清除的格子");
            		}
            		break;
            	case dicing:
            		updateGameMsg(lbGameMsg, "你必須先骰骰子");
            		break;
            	case fightingPart1:
            		updateGameMsg(lbGameMsg, "戰鬥中！請" + (fighter1.getOrder()+1) + "P擲骰子");
            		break;
            	case fightingPart2:
            		updateGameMsg(lbGameMsg, "戰鬥中！請" + (fighter2.getOrder()+1) + "P擲骰子");
            		break;
            	}
        		if(mp <= 0 && state != dicing)
        		{
        			rePaint();
        			nextTurn();
        		}
        		
        		victoryCheck();
            }
            System.out.println(mp);
            
        }

	}
	
	class DiceListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int temp = mp;
			if(state != dicing && state!=fightingPart1 && state!=fightingPart2){
				updateGameMsg(lbGameMsg, "請在地圖上移動");
			}
			else{
				diceButton.setIcon(icon.dice[0]);
				diceButton.setRolloverIcon(icon.dice[0]);
				temp = dice.throwDice();
				final int temp2 = temp;
				//change picture
				int delay = 300; 
				ActionListener diceRoller = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						diceButton.setIcon(icon.dice[temp2]);
						diceButton.setRolloverIcon(null);
						System.out.println("inner MP: " + mp);
					}
				};
				Timer timer = new Timer(delay, diceRoller);
				timer.setRepeats(false);
				timer.start();
				
			}
			System.out.println("outer MP: " + temp);
			if(state==dicing){
				mp = temp;
				state = moving;
				updateMP(mp);
				System.out.println("now player is " + (players.get(nowPlayer).getOrder()+1) + "P");
				highLightMove(players.get(nowPlayer).getX(), players.get(nowPlayer).getY());
			}
			else if(state==fightingPart1){
				strenth1 = temp;
				fight(fighter1, fighter2);
				lbStrenth1.setText((fighter1.getOrder()+1) + "P擲出: " + temp);
			}
			else if(state==fightingPart2){
				strenth2 = temp;
				fight(fighter1, fighter2);
				lbStrenth2.setText((fighter2.getOrder()+1) + "P擲出: " + temp);
			}
		}
		
	}
	
	class SweepListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			Player p = players.get(nowPlayer); 
			int x = p.getX();
			int y = p.getY();
			
			if(state == dicing){
				updateGameMsg(lbGameMsg, "你必須先骰骰子");
			}
			else if(state == sweeping){
				state = moving;
				sweeperButton.setIcon(icon.sweeper);
				rePaint();
				highLightMove(x, y);
			}
			else if(state == moving){
				if(surroundSweep(x, y)){
					state = sweeping;
					sweeperButton.setIcon(icon.sweeperOn);
					rePaint();
					highLightSweep(x, y);
				}
				else{
					updateGameMsg(lbGameMsg, "附近沒有可清除的格子");
				}
			}
		}
		
		private boolean surroundSweep(int x, int y){
			int[][] opened = ground.getMap();
			int[][] step = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
			
			for(int i=0; i<8; ++i){
				int a = x+step[i][0];
				int b = y+step[i][1];
				if(a>=0 && a<=height-1 && b>=0 && b<=width-1){
					if(opened[a][b]==0 || opened[a][b]==1)
						return true;
					
				}
			}
			return false;
		}
		
	}
	
	class RestartListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int mType=JOptionPane.INFORMATION_MESSAGE;
			String options[] = {"確認", "取消"};
			int opt=JOptionPane.showOptionDialog(frame,"確定要重新開始?","確認",
	                JOptionPane.YES_NO_OPTION,mType, null, options, "取消");
			
			if(opt==JOptionPane.YES_OPTION){
				SoundEffect.BGM.loopStop();
				father.run();
				dispose();
			}
		}
	}
	
	class CloseListener implements WindowListener
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			// TODO Auto-generated method stub
			int mType=JOptionPane.INFORMATION_MESSAGE;
			String options[] = {"確認", "取消"};
			int opt=JOptionPane.showOptionDialog(frame,"確定要離開?","確認",
	                JOptionPane.YES_NO_OPTION,mType, null, options, "取消");
			
			if(opt==JOptionPane.YES_OPTION)
				System.exit(0);
		}

		public void windowClosed(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		
	}	
}
