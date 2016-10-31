import java.awt.Graphics;


public class Pause implements Runnable{
	Graphics g;
	
	public Pause(Graphics g) {
		super();
		this.g = g;
	}

	@Override
	public void run(){
		int i=0,j=400;
		while(true){
			System.out.println("asdasd");
			g.drawLine(0, j--, i++, 0);
			if(j==0&&i==400)
				break;
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
