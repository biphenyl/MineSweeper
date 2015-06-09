import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.IvParameterSpec;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private JLabel lbMovement;
	private ArrayList<Player> players = new ArrayList<Player>(4);
	private GUI frame;
	private int state=2, mp, nowPlayer, colored;	//state : 0=moving, 1=sweeping, 2=throwing dice
	private ArrayList<JComponent> guiComponents_btn = new ArrayList<JComponent>(900);
	private ArrayList<JComponent> guiComponents_label = new ArrayList<JComponent>(100);
	private Dice dice;
	private Ground ground;
	private JButton diceButton;
	private IconCollection icon = new IconCollection();
	
	private final int moving = 0;
	private final int sweeping = 1;
	private final int dicing = 2;
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("deprecation")
	public GUI()
	{
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
		
/*<<<<<<< HEAD
		JLabel testLabel = new JLabel("HI");
		testLabel.setBounds(14, 800, 60, 60);
		testLabel.setIcon(icon.diceRolling);
		testLabel.setVisible(true);
		leftPanel.add(testLabel);
		guiComponents_label.add(testLabel);
		
		diceButton = new JButton("Dice");
		diceButton.setBounds(14, 888, 99, 27);
=======
		//JLabel testLabel = new JLabel("HI");
		//testLabel.setBounds(14, 800, 60, 60);
		//testLabel.setIcon(icon.diceRolling);
		//testLabel.setVisible(false);
		//leftPanel.add(testLabel);
		//guiComponents_label.add(testLabel);*/
		
		diceButton = new JButton(icon.dice[4]);
		diceButton.setPressedIcon(icon.dice[0]);
		diceButton.setBounds(20, 865, 50, 50);
//>>>>>>> 2c91c9e45314aabfd5cc45d24e971804a8d1f37c
		diceButton.addActionListener(new DiceListener());
		leftPanel.add(diceButton);
		
		JButton sweeper = new JButton(icon.flag);
		sweeper.setBounds(123, 865, 50, 50);
		leftPanel.add(sweeper);
	
		int[] yLabel = {33, 213, 393, 573};
		for(int i=0; i<4; ++i){
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
		lbMovement.setBounds(17, 825, 208, 27);
		leftPanel.add(lbMovement);
		
		rightPanel = new JPanel();
		rightPanel.setBounds(0, 33, 934, 920);
		contentPane.add(rightPanel);
		rightPanel.setLayout(null);
		
		for(int i=0; i<30; ++i)
		{
			for(int j=0; j<30; ++j)
			{
				MineButton btn = new MineButton(i, j);
				btn.setSize(30, 30);
				btn.setLocation(j*30, i*30);
				//btn.setEnabled(false);
				btn.addActionListener(new ButtonListener());
				btn.pos = i*30+j;
				guiComponents_btn.add(btn);
				rightPanel.add(btn);
			}
		}

		ground = new Ground(30, 30, 300);	
		initPlayer();
		
		dice = new Dice();
		rePaint();
	}

	/**
	 * Launch the application.
	 */
	public void run()
	{
		try
		{
			frame = new GUI();
			frame.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private void initPlayer()
	{
		for(int i=0; i<4; ++i)
			players.add(new Player());
		
		players.get(0).setInitPos(1, 1);
		players.get(0).setIcon(icon.redIcon);
		players.get(1).setInitPos(28, 1);
		players.get(0).setIcon(icon.blueExIcon);
		players.get(2).setInitPos(1, 28);
		players.get(0).setIcon(icon.greenIcon);
		players.get(3).setInitPos(28, 28);
		players.get(0).setIcon(icon.yellowIcon);
		
		nowPlayer = 0;
		
		ground.expand(1, 1);
		ground.expand(28, 1);
		ground.expand(1, 28);
		ground.expand(28, 28);
	}
	
	private void nextTurn()
	{
		state = dicing;
		if(++nowPlayer >= 4)
			nowPlayer = 0;
		System.out.println("醬汁");
		
		JLabel lb = (JLabel)guiComponents_label.get(0);
		lb.setIcon(icon.dice[0]);
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
	
	private void rePaint(){
		
		int[][] map = ground.getMap();
		int[][] mineNumber = ground.getMineNumber();
		int x = players.get(nowPlayer).getX();
		int y = players.get(nowPlayer).getY();
		MineButton mb;
		// test to show mines
		for(int i=0; i<30; ++i){
			for(int j=0; j<30; ++j){
				if(map[i][j]==0){
					mb = (MineButton)guiComponents_btn.get(i*30+j);
					mb.setIcon(icon.grayOldIcon);
					mb.setPressedIcon(icon.grayOldIcon);
				}
				else if(map[i][j]==1){
					mb = (MineButton)guiComponents_btn.get(i*30+j);
					mb.setIcon(new ImageIcon("pic/grayOldIcon.jpg"));
					
					if(moveable(i, j))
						mb.setPressedIcon(new ImageIcon("pic/explodeSmall.gif"));
					else
						mb.setPressedIcon(new ImageIcon("pic/grayOldIcon.jpg"));
					
				}
				else if(map[i][j]==2){
					mb = (MineButton)guiComponents_btn.get(i*30+j);
					
					mb.setIcon(icon.whiteIcon[mineNumber[i][j]]);
					mb.setPressedIcon(icon.whiteIcon[mineNumber[i][j]]);
/*=======

					if(mineNumber[i][j]!=0){
						mb.setIcon(icon.whiteIcon[mineNumber[i][j]]);
						mb.setPressedIcon(icon.whiteIcon[mineNumber[i][j]]);
					}
					else{ 
						mb.setIcon(icon.whiteIcon[0]);
						mb.setPressedIcon(icon.whiteIcon[0]);
					}
>>>>>>> 2c91c9e45314aabfd5cc45d24e971804a8d1f37c*/
				}
				else if(map[i][j]==3){
					mb = (MineButton)guiComponents_btn.get(i*30+j);
					mb.setIcon(icon.flag);
					mb.setIcon(icon.flag);
				}
			}
		}
		
		for(int i=0; i<4; ++i)
		{
			mb = (MineButton)guiComponents_btn.get(players.get(i).getX()*30+players.get(i).getY());
			mb.setIcon(icon.yellowIcon);
		}
		
		if(mp>0){
			//hLMove(x, y);
			
		}
		
	}
	
	private void hLMove(int x, int y)
	{
		MineButton mb;
		colored=0;
		int mined = 0;
		ImageIcon moveHL = icon.explodeSmall;
		if(x>0)
		{
			mb = (MineButton)guiComponents_btn.get(x*30+y-30);
			mb.setIcon(moveHL);
			if(ground.getMapXY(x-1, y)==1){
				mb.setPressedIcon(icon.redIcon);
				mined++;
			}
			colored++;
			
		}
		
		if(x<29)
		{
			mb = (MineButton)guiComponents_btn.get(x*30+y+30);
			mb.setIcon(moveHL);
			if(ground.getMapXY(x+1, y)==1){
				mb.setPressedIcon(icon.explodeSmall);
				mined++;
			}
			colored++;
		}
		
		if(y>0)
		{
			mb = (MineButton)guiComponents_btn.get(x*30+y-1);
			mb.setIcon(moveHL);
			if(ground.getMapXY(x, y-1)==1){
				mb.setPressedIcon(icon.explodeSmall);
				mined++;
			}
			colored++;
		}
		
		if(y<29)
		{
			mb = (MineButton)guiComponents_btn.get(x*30+y+1);
			mb.setIcon(moveHL);
			if(ground.getMapXY(x, y+1)==1){
				mb.setPressedIcon(icon.explodeSmall);
				mined++;
			}
			colored++;
		}
		
		System.out.println("mine:" + mined);
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
			die(p);
			ground.sweep(x, y);
			return;
		}
		else if(ground.getMapXY(x, y)==3){
			getFlag();
		}
		
		p.setXY(x, y);
		System.out.println(x + " " + y);
		lbMovement.setText("剩餘步數: " + --mp);
		
		ground.expand(x, y);
	}
	
	private void die(Player p)
	{
		
		System.out.println("NOOOOOO!");
		
		int x = p.getX();
		int y = p.getY();
		
		MineButton mb = (MineButton)guiComponents_btn.get(x*30 + y);
		mb.setIcon(icon.redIcon);
		
		/*try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		System.out.println(x + " LA " + y);
		
		mp=0;
	
		p.respawn();
		p.addScore(-100);
		
	}
	
	private void getFlag(){
		
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
            	
        		if(state == moving)
                {
        			if(moveable(x, y))
        			{	
        				playerMove(x, y);
        				/*players.get(nowPlayer).setXY(x, y);
            			System.out.println(x + " " + y + " " + mb.pos);
            			lbMovement.setText("剩餘步數: " + --mp);
            			for(int i=0; i< colored; i++)
            				coloredButtons.get(i).setIcon(icon.whiteIcon[0]);	//should write a erase method*/
            			rePaint();
            			
            			if(mp>0){
            				//hLMove(x, y);
            				mb.setIcon(players.get(nowPlayer).getIcon());
            			}	
        			}
        			else {
        				JOptionPane.showMessageDialog(frame,"請選擇可移動的格子");
					}
                }
            	else if(state == sweeping)
            	{
            		mb.setIcon(icon.whiteIcon[0]);
            	}
            	else if(state == dicing)
            	{
            		JOptionPane.showMessageDialog(frame,"你必須先骰骰子");
            	}
        		
        		if(mp <= 0 && state != dicing)
        		{
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
			if(state != dicing)
				JOptionPane.showMessageDialog(frame,"請在地圖上移動");
			else {
/*<<<<<<< HEAD
				//change picture
				
				//give number
				
				mp = dice.throwDice();
=======*/
				
				//give number and change dice's picture
				mp = dice.throwDice();
				JButton diceButton = (JButton) e.getSource();
				diceButton.setIcon(icon.dice[mp]);
				
//>>>>>>> 2c91c9e45314aabfd5cc45d24e971804a8d1f37c
				state = moving;
				lbMovement.setText("剩餘步數: " + mp);
				//hLMove(players.get(nowPlayer).getX(), players.get(nowPlayer).getY());
				
				JLabel lb = (JLabel)guiComponents_label.get(0);
				lb.setIcon(new ImageIcon("pic/dice/d000" + mp + ".gif"));
				System.out.println(lb.getText());
				//change picture
				
			}
			
		}
		
	}
	
	class SweepListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			
			// sweeper-1
			// highlight sweep-able button
			// sweep
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
