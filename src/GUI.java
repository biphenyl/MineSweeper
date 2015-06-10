import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

public class GUI extends JFrame
{

	private JPanel contentPane, leftPanel, rightPanel;
	private JLabel lbMovement, lbNowplayer;
	private ArrayList<Player> players;
	private GUI frame;
	private int state=2, mp, nowPlayer;	//state : 0=moving, 1=sweeping, 2=throwing dice
	private int playerNum, width, height, totalMine;
	private int strenth1, strenth2;
	private int dontCleanX, dontCleanY;
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
	private final int fightingPart3 = 5;
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("deprecation")
	public GUI(int playerNum, int height, int width, int totalMine)
	{
		this.playerNum = playerNum;
		this.width = width;
		this.height = height;
		this.totalMine = totalMine;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new CloseListener());
		setBounds(100, 100, 1200, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		leftPanel = new JPanel();
		leftPanel.setBounds(934, 33, 248, 920);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		
		diceButton = new JButton(icon.dice[4]);
		diceButton.setPressedIcon(icon.dice[0]);
		diceButton.setBounds(20, 811, 96, 96);
		diceButton.addActionListener(new DiceListener());
		leftPanel.add(diceButton);
		
		sweeperButton = new JButton(icon.sweeper);
		sweeperButton.setBounds(126, 811, 96, 96);
		sweeperButton.addActionListener(new SweepListener());
		leftPanel.add(sweeperButton);
	
		int[] yLabel = {33, 213, 393, 573};
		for(int i=0; i<playerNum; ++i){
			JLabel lb = new JLabel((i+1) + "P");
			lb.setFont(new Font("Arial", Font.PLAIN, 36));
			lb.setBounds(33, yLabel[i], 60, 56);
			guiComponents_label.add(lb);
			leftPanel.add(lb);
			
			lb = new JLabel("Score: 0");
			lb.setFont(new Font("Arial", Font.PLAIN, 24));
			lb.setBounds(33, yLabel[i]+70, 120, 27);
			guiComponents_label.add(lb);
			leftPanel.add(lb);
			
		}
		
		JLabel lblx = new JLabel("5x");
		lblx.setFont(new Font("新細明體", Font.PLAIN, 24));
		lblx.setBounds(33, 200, 57, 19);
		leftPanel.add(lblx);
		
		lbMovement = new JLabel("1P 請擲骰子");
		lbMovement.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbMovement.setBounds(14, 722, 208, 27);
		leftPanel.add(lbMovement);
		
		lbNowplayer = new JLabel("現在玩家是: 1P");
		lbNowplayer.setFont(new Font("華康新儷粗黑", Font.PLAIN, 24));
		lbNowplayer.setBounds(20, 664, 202, 45);
		leftPanel.add(lbNowplayer);
		
		rightPanel = new JPanel();
		rightPanel.setBounds(0, 33, 934, 920);
		contentPane.add(rightPanel);
		rightPanel.setLayout(null);
		
		int startX = (920-30*height)/2;
		int startY = (934-30*width)/2;
		for(int i=0; i<height; ++i)
		{
			for(int j=0; j<width; ++j)
			{
				MineButton btn = new MineButton(i, j);
				btn.setSize(30, 30);
				btn.setLocation(startY+j*30, startX+i*30);
				//btn.setEnabled(false);
				btn.addActionListener(new ButtonListener());
				btn.pos = i*width+j;
				guiComponents_btn.add(btn);
				rightPanel.add(btn);
			}
		}

		ground = new Ground(height, width, totalMine);	
		initPlayer();
		
		dice = new Dice();
		
		dontCleanY = -1;
		dontCleanX = -1;
		
		rePaint();
	}

	/**
	 * Launch the application.
	 */
	public void run()
	{
		try
		{
			//frame = new GUI();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
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
		players.get(1).setInitPos(height-2, 1);
		players.get(1).setIcon(icon.blueExIcon);
		if(playerNum>=3){
			players.get(2).setInitPos(1, width-2);
			players.get(2).setIcon(icon.greenIcon);
		}
		if(playerNum==4){
			players.get(3).setInitPos(height-2, width-2);
			players.get(3).setIcon(icon.yellowIcon);
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
		
		lbNowplayer.setText("現在玩家是: " + (nowPlayer+1) + "P");
		System.out.println("醬汁");
		
	}
	
	private void rePaint(){
		
		int[][] map = ground.getMap();
		int[][] mineNumber = ground.getMineNumber();
		int x = players.get(nowPlayer).getX();
		int y = players.get(nowPlayer).getY();
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
					//mb.setPressedIcon(icon.grayOldIcon);
				}
				else if(map[i][j]==1){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(new ImageIcon("pic/grayOldIcon.jpg"));
					
					/*if(moveable(i, j))
						mb.setPressedIcon(new ImageIcon("pic/explodeSmall.gif"));
					else
						mb.setPressedIcon(new ImageIcon("pic/grayOldIcon.jpg"));*/
					
				}
				else if(map[i][j]==2){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.whiteIcon[mineNumber[i][j]]);
					//mb.setPressedIcon(icon.whiteIcon[mineNumber[i][j]]);
				}
				else if(map[i][j]==3){
					mb = (MineButton)guiComponents_btn.get(i*width+j);
					mb.setIcon(icon.grayOldIconWithFlag);			
				}
			}
		}
		
		for(int i=0; i<playerNum; ++i)
		{
			mb = (MineButton)guiComponents_btn.get(players.get(i).getX()*width+players.get(i).getY());
			mb.setIcon(players.get(i).getIcon());
		}
		
		if(mp>0){
			//hLMove(x, y);
			
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
			getFlag();
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
		
		if(x>0 && y>0){
			mb = (MineButton)guiComponents_btn.get(x*width+y-width-1);
			mbState = ground.getMapXY(x-1, y-1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x-1) + " and " + (y-1));
			}
		}
		
		if(x>0){
			mb = (MineButton)guiComponents_btn.get(x*width+y-width);
			mbState = ground.getMapXY(x-1, y);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x-1) + " and " + (y));
			}
		}
		
		if(x>0 && y<width-1){
			mb = (MineButton)guiComponents_btn.get(x*width+y-width+1);
			mbState = ground.getMapXY(x-1, y+1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x-1) + " and " + (y+1));
			}
		}
		
		if(y>0){
			mb = (MineButton)guiComponents_btn.get(x*width+y-1);
			mbState = ground.getMapXY(x, y-1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x) + " and " + (y-1));
			}
		}
		
		if(y<width-1){
			mb = (MineButton)guiComponents_btn.get(x*width+y+1);
			mbState = ground.getMapXY(x, y+1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x) + " and " + (y+1));
			}
		}
		
		if(x<height-1 && y>0){
			mb = (MineButton)guiComponents_btn.get(x*width+y+width-1);
			mbState = ground.getMapXY(x+1, y-1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x+1) + " and " + (y-1));
			}
		}
		
		if(x<height-1){
			mb = (MineButton)guiComponents_btn.get(x*width+y+width);
			mbState = ground.getMapXY(x+1, y);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x+1) + " and " + (y));
			}
		}
		
		if(x<height-1 && y<width-1){
			mb = (MineButton)guiComponents_btn.get(x*width+y+width+1);
			mbState = ground.getMapXY(x+1, y+1);
			if(mbState==0 || mbState==1){
				mb.setIcon(sweepHL);
				System.out.println((x+1) + " and " + (y+1));
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
		if(ground.getMapXY(x, y)==1){
			updateScore(players.get(nowPlayer), 50);
		}
		
		MineButton mb = (MineButton)guiComponents_btn.get(x*+y);
		mb.setIcon(icon.whiteIcon[ground.getMineNumXY(x, y)]);
		ground.sweep(x, y);
		updateMP(--mp);
	}

	private void updateScore(Player p, int score)
	{
		p.addScore(score);
		JLabel lb = (JLabel)guiComponents_label.get(nowPlayer*2+1);
		lb.setText("Score: " + Integer.toString(p.getScore()));
		
		System.out.println("Score updated!");
	}
	
	private void updateMP(int n){
		lbMovement.setText("剩餘步數: " + mp);
	}
	
	private void fight(Player p1, Player p2){
		//show fight animation
		if(state==moving){
			fighter1 = p1;
			fighter2 = p2;
			lbMovement.setText("請" + Integer.toString(p1.getOrder()+1) +"P先擲骰子");
			state = fightingPart1;
		}
		else if(state==fightingPart1){
			lbMovement.setText("換" + Integer.toString(p2.getOrder()+1) +"P擲骰子");
			state = fightingPart2;
		}
		else if(state==fightingPart2){
			if(strenth1<strenth2){
				die(p1, p1.getX(), p1.getY());
				p2.addScore(p1.getScore()/2);
				p1.addScore(-(p1.getScore()/2));
				lbMovement.setText((fighter2.getOrder()+1) + "P獲勝！");
				
				nextTurn();
			}
			else if(strenth1>strenth2){
				die(p2, p2.getX(), p2.getY());
				p1.addScore(p2.getScore()/2);
				p2.addScore(-(p2.getScore()/2));
				lbMovement.setText((fighter1.getOrder()+1) + "P獲勝！");
				
				state = moving;
				mp = 0;
			}
			else{
				lbMovement.setText("平手！");
				nextTurn();
			}
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
			System.out.println((p.getOrder()+1) + "P killed by mine");
		}
		else {
			System.out.println((p.getOrder()+1) + "P killed by player");
		}
		updateScore(p, -100);
		
		final int x1 = x, y1 = y;
		dontCleanX = x;
		dontCleanY = y;
		int delay = 1000; 
		ActionListener exploder = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MineButton mb = (MineButton)guiComponents_btn.get(x1*width+y1);
				mb.setIcon(icon.whiteIcon[ground.getMapXY(x1, y1)]);
				mb.setRolloverIcon(null);
				
			}
		};
		Timer timer = new Timer(delay, exploder);
		timer.setRepeats(false);
		timer.start();
		
		p.respawn();
		mb = (MineButton)guiComponents_btn.get(p.getX()*width+p.getY());
		mb.setIcon(p.getIcon());
	}
	
	private void getFlag(){
		updateScore(players.get(nowPlayer), 200);
		ground.generateFlag();
	}
	
	private boolean victoryCheck()
	{
		if(players.get(nowPlayer).getScore()>=1000)
			return true;
		else 
			return false;
	}

	private void victory()
	{
		
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
            				mb.setIcon(players.get(nowPlayer).getIcon());
            			}	
        			}
        			else {
        				JOptionPane.showMessageDialog(frame,"請選擇可移動的格子");
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
            			JOptionPane.showMessageDialog(frame,"請選擇可清除的格子");
            		}
            		break;
            	case dicing:
            		JOptionPane.showMessageDialog(frame,"你必須先骰骰子");
            		break;
            	case fightingPart1:
            		JOptionPane.showMessageDialog(frame,"戰鬥中！請" + (fighter1.getOrder()+1) + "P擲骰子");
            	case fightingPart2:
            		JOptionPane.showMessageDialog(frame,"戰鬥中！請" + (fighter2.getOrder()+1) + "P擲骰子");
            	}
        		if(mp <= 0 && state != dicing)
        		{
        			rePaint();
        			nextTurn();
        			lbMovement.setText((nowPlayer+1) + "P 請擲骰子");
        		}
            }
            System.out.println(mp);
            
            if(victoryCheck())
            	victory();
        }

	}
	
	class DiceListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int temp = mp;
			if(state != dicing && state!=fightingPart1 && state!=fightingPart2)
				JOptionPane.showMessageDialog(frame,"請在地圖上移動");
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
			}
			else if(state==fightingPart2){
				strenth2 = temp;
				fight(fighter1, fighter2);
			}
		}
		
	}
	
	class SweepListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int x = players.get(nowPlayer).getX();
			int y = players.get(nowPlayer).getY();
			
			if(state == dicing){
				JOptionPane.showMessageDialog(frame,"你必須先骰骰子");
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
					JOptionPane.showMessageDialog(frame,"附近沒有可清除的格子");
				}
			}
			// sweeper-1
			// highlight sweep-able button
			// sweep
		}
		
		private boolean surroundSweep(int x, int y){
			int[][] opened = ground.getMap();
			if(opened[x-1][y-1]==0 || opened[x-1][y]==0 || opened[x-1][y+1]==0 || opened[x][y-1]==0 
					|| opened[x][y+1]==0 || opened[x+1][y-1]==0 || opened[x+1][y]==0 || opened[x+1][y+1]==0){
				return true;
			}
			else if(opened[x-1][y-1]==1 || opened[x-1][y]==1 || opened[x-1][y+1]==1 || opened[x][y-1]==1 
					|| opened[x][y+1]==1 || opened[x+1][y-1]==1 || opened[x+1][y]==1 || opened[x+1][y+1]==1){
				return true;
			}
			else 
				return false;
				
			
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
