import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Jf extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CreateMaze cm = new CreateMaze();//这是一个JPanel
	Jf(){
		this.setSize(1000,500);
		cm.init();
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(cm,BorderLayout.CENTER);
		JButton jb = new JButton("再来一次");
		jb.addMouseListener(new mouseL(this,cm));
		this.add(jb,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		//绘图错误:无法解决
		//加入线程,使其画出来~!!
		new Jf();
	}
}
