import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Jf extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CreateMaze cm = new CreateMaze();//����һ��JPanel
	Jf(){
		this.setSize(1000,500);
		cm.init();
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(cm,BorderLayout.CENTER);
		JButton jb = new JButton("����һ��");
		jb.addMouseListener(new mouseL(this,cm));
		this.add(jb,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		//��ͼ����:�޷����
		//�����߳�,ʹ�仭����~!!
		new Jf();
	}
}
