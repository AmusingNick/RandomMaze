import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;


public class mouseL extends MouseAdapter{
	JFrame j;
	CreateMaze cm = new CreateMaze();
	public mouseL(JFrame j, CreateMaze cm) {
		super();
		this.j = j;
		this.cm = cm;
	}
	@Override
	public void mouseClicked(MouseEvent e){
		j.remove(cm);
		j.repaint();
		cm= new CreateMaze();
		cm.init();
		j.add(cm,BorderLayout.CENTER);
		j.validate();
	}
}
