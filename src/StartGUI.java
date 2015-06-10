import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;


public class StartGUI extends JFrame {

	private StartGUI frame;
	private JPanel contentPane;
	private JTextField numberOfMine_t, width_t, height_t;
	private JButton buttonStart, buttonLeave;
	private JLabel label1, label2, label3, label4, label5, label6;
	private JComboBox numberSelect;
	private int numberOfMine, width, height, numberOfPlayer;
	/**
	 * Create the frame.
	 */
	public StartGUI() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(760, 390, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		buttonStart = new JButton("開始");
		buttonStart.setBounds(100, 210, 80, 25);
		buttonStart.addActionListener(new StartListener());
		contentPane.add(buttonStart);
		
		buttonLeave = new JButton("離開");
		buttonLeave.setBounds(220, 210, 80, 25);
		buttonLeave.addActionListener(new LeaveListener());
		contentPane.add(buttonLeave);
		
		label1 = new JLabel("請選擇人數");
		label1.setBounds(112, 40, 72, 25);
		contentPane.add(label1);
		
		label2 = new JLabel("請輸入地圖大小");
		label2.setBounds(88, 75, 96, 25);
		contentPane.add(label2);
		
		label3 = new JLabel("請輸入地雷數");
		label3.setBounds(100, 110, 84, 25);
		contentPane.add(label3);
		
		label5 = new JLabel("(地圖大小: 10*10 ~ 30*30)");
		label5.setBounds(90, 140, 150, 25);
		contentPane.add(label5);
		
		label5 = new JLabel("(地圖大小/5<地雷數<地圖大小/3)");
		label5.setBounds(90, 165, 200, 25);
		contentPane.add(label5);
		
		
		String[] options = {"2", "3", "4"};
		numberSelect = new JComboBox(options);
		numberSelect.setBounds(200, 40, 40, 25);
		contentPane.add(numberSelect);
		
		label4 = new JLabel("*");
		label4.setBounds(240, 75, 10, 25);
		contentPane.add(label4);
		
		width_t = new JTextField();
		width_t.setBounds(200, 75, 40, 25);
		contentPane.add(width_t);
		width_t.setColumns(10);
		
		height_t = new JTextField();
		height_t.setColumns(10);
		height_t.setBounds(250, 75, 40, 25);
		contentPane.add(height_t);
		
		numberOfMine_t = new JTextField();
		numberOfMine_t.setBounds(200, 110, 50, 25);
		contentPane.add(numberOfMine_t);
		numberOfMine_t.setColumns(3);
		
	}

	public void run(){
		
		this.setVisible(true);	
	}
	
	private void startGame()
	{
		GUI gui = new GUI(numberOfPlayer, width, height, numberOfMine);
		gui.run();
		
		this.setVisible(false);
	}
	
	class StartListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton button =(JButton) e.getSource();
			numberOfPlayer = Integer.valueOf((String) numberSelect.getSelectedItem()).intValue();
			try{
				width = Integer.valueOf(width_t.getText()).intValue();
				height = Integer.valueOf(height_t.getText()).intValue();
				numberOfMine = Integer.valueOf(numberOfMine_t.getText()).intValue();
				
				if(width > 30 || width < 10 || height > 30 || height < 10)
					JOptionPane.showMessageDialog(button.getParent() , "地圖大小必須介於 10*10 ~ 30*30");
				else if(numberOfMine > width*height/3 || numberOfMine < width*height/5)
					JOptionPane.showMessageDialog(button.getParent() , "地雷數必須介於 地圖大小/5 ~ 地圖大小/3");
				else 
					startGame();
			}
			catch(NumberFormatException exception){
				JOptionPane.showMessageDialog(button.getParent() , "請輸入數字");
			}
		}
		
	}
	class LeaveListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}
	
	
}
