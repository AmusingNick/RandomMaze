import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Stack;

import javax.swing.JPanel;


public class CreateMaze extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mazeRowCount = 10; //�Թ�����
	private int mazeColCount = 12; //�Թ�����
	private int width = 10;   //�Թ���Ԫ����
	//�Թ��߿���ɫ
	private static final Color WALL_COLOR = Color.black;
	//0��λ��
	private int zeroX = width;
	private int zeroY = width;
	private Random random = null;
	//�Ѿ������ʵĵ�Ԫ��
	private Cell[] visitedCells = null;
	private int visitedCount = 0;
	private int cellCount = 0;
	/*
	 * �Թ�map[����][����],����x����y��map[x][y]
	 * ����0��Ϊ���������Ͻ�,0������Ϊx��������,0�����±�ʾy��������
	 * */
	Cell map[][] = null;
	Cell mapRoad[][] = null;
	public void init(){
		mazeRowCount = 20;
		mazeColCount = 20;
		width = 20;
		cellCount = mazeColCount*mazeRowCount;
		visitedCells = new Cell[cellCount];
		random = new Random();
		//��ʼ���Թ�
		Cell cell ;
		map = new Cell[mazeColCount][mazeRowCount];
		for(int y=0;y<mazeColCount;y++)
			for(int x=0;x<mazeRowCount;x++){
				cell = new Cell();
				cell.x = zeroX+x*width;
				cell.y = zeroY+y*width;
				cell.col = x;
				cell.row = y;
				map[x][y] = cell;
			}
	}
	public void paint(Graphics g){
		createMaze();
		//�����Թ�
		paintMaze(g);
		//����·��
		paintRoad(g);
	}
	
	private void createMaze() {
		//�����ʼdian
		initMaze(map[random.nextInt(mazeColCount)][random.nextInt(mazeRowCount)]);
		//���ÿ�ʼ������
		map[0][mazeRowCount-1].walls[Cell.BOTTOM]
				= Cell.NOWALL;
		map[mazeColCount-1][0].walls[Cell.TOP]
				= Cell.NOWALL;
	}
	private void initMaze(Cell theCell){
		//��Ԫ��ȫ���������,�򴴽����
		if(visitedCount == cellCount)
			return;
		if(!isVisited(theCell)){
			theCell.visited = true;
			visitedCells[visitedCount++] = theCell;
		}
		//�õ���Ч�����ڵ�Ԫ��
		int neibCount = 0;
		Cell[] neighbours = new Cell[4];
		Cell nextCell;
		if(theCell.col-1>=0
				&&!(nextCell = map[theCell.col-1][theCell.row]).visited){
			neighbours[neibCount++] = nextCell;
		}
		if(theCell.row-1>=0
				&&!(nextCell = map[theCell.col][theCell.row-1]).visited){
			neighbours[neibCount++] = nextCell;
		}
		if(theCell.col+1<this.mazeColCount
				&&!(nextCell = map[theCell.col+1][theCell.row]).visited){
			neighbours[neibCount++] = nextCell;
		}
		if(theCell.row+1<this.mazeRowCount
				&&!(nextCell = map[theCell.col][theCell.row+1]).visited){
			neighbours[neibCount++] = nextCell;
		}
		//����ڽ��ĵ�Ԫ�񶼱�������
		if(neibCount==0){
			initMaze(visitedCells[random.nextInt(visitedCount)]);
			return ;
		}
		//����õ���һ����Ԫ��
		nextCell = neighbours[random.nextInt(neibCount)];
		//ȥ����Ԫ���ѡ����ڽ���Ԫ��֮���ǽ
		//neighbour left
		if(nextCell.col < theCell.col){
			nextCell.walls[Cell.RIGHT] = Cell.NOWALL;
			theCell.walls[Cell.LEFT] = Cell.NOWALL;
		}
		else if(nextCell.row < theCell.row){
			nextCell.walls[Cell.BOTTOM] = Cell.NOWALL;
			theCell.walls[Cell.TOP] = Cell.NOWALL;
		}
		else if(nextCell.col >theCell.col){
			nextCell.walls[Cell.LEFT] = Cell.NOWALL;
			theCell.walls[Cell.RIGHT] = Cell.NOWALL;
		}
		else if(nextCell.row > theCell.row){
			nextCell.walls[Cell.TOP] = Cell.NOWALL;
			theCell.walls[Cell.BOTTOM] = Cell.NOWALL;
		}
		initMaze(nextCell);
	}
	/*
	 * ��Ԫ���Ƿ񱻷��ʹ�
	 * */
	private boolean isVisited(Cell cell){
		if(visitedCount == 0)
			return false;
		
		for(int i=0;i<visitedCount;i++)
			if(visitedCells[i] == cell)
				return true;
		return false;
	}
	
	private void paintMaze(Graphics g){
		for(int y=0;y<mazeColCount;y++)
			for(int x=0;x<mazeRowCount;x++){
				map[x][y].paint(g);
			}
	}
	
	private void paintRoad(Graphics g){
		//�����ѳ�·��,���map.col,map.row������,Ȼ��������������л���·��
		Stack<Cell> sc = getRoad();
		g.setColor(Color.red);
		for(int i=0;i<sc.size();){
			int beginX = sc.get(i).x+width/2;
			int beginY = sc.get(i).y+width/2;
			int rearX=(mazeColCount-1)*width+width/2;
			int rearY=width/2;
			i++;
			if(i<sc.size()){
				rearX = sc.get(i).x+width/2;
				rearY = sc.get(i).y+width/2;
			}
			g.drawLine(beginX, beginY, rearX, rearY);
		}	
	}

	private Stack<Cell> getRoad(){
		Stack<Cell> sc = new Stack<Cell>();         
		map[0][mazeRowCount-1].roadVisited = true;
		Cell cell = map[0][mazeRowCount-1];   //������ʼ��
		sc.push(cell);
		while(!cellIsFinally(cell)){
			//��������
			//��getbian������
			if(cell!=null)
				cell = (Cell)GetAround(cell);
			if(cell==null)
				//�����������ͬ
				cell = sc.pop();
			else{
				cell.roadVisited = true;
				sc.push(cell);
				System.out.println("cell.row:"+cell.row
						+" cell.col:"+cell.col);
			}
		}
		return sc;
	}
	
	private Object GetAround(Cell thecell){
		/*
		 * ���ص�cell������,��,��,�µ�˳��
		 * */
		//���ж�cell��λ��
		for(int y=0;y<mazeColCount;y++)
			for(int x=0;x<mazeRowCount;x++){
				if(map[x][y].equals(thecell)){  //��map���ҵ�thecell��λ��
					if(thecell.walls[Cell.LEFT]==Cell.NOWALL
							&&map[x-1][y].roadVisited==false){
						//������û��ǽ,�����Ǳߵĵ�Ԫ��û�б����ʹ���
						return map[x-1][y];
					}
					else if(thecell.walls[Cell.TOP]==Cell.NOWALL
							&&map[x][y-1].roadVisited==false){
						return map[x][y-1];
					}
					else if(thecell.walls[Cell.RIGHT]==Cell.NOWALL
							&&map[x+1][y].roadVisited==false){
						return map[x+1][y];
					}
					else if(thecell.walls[Cell.BOTTOM]==Cell.NOWALL
							&&map[x][y+1].roadVisited==false){
						return map[x][y+1];
					}
				}
			}	
		return null;
	}
	
	private boolean cellIsFinally(Cell c){
		if(c==null)
			return false;
		if((c.row==0)&&(c.col==(mazeColCount-1))){
			System.out.println("�����һ����");
			return true;
		}
		else 
			return false;
	}
	
	class Cell{
		static final int LEFT = 0;
		static final int TOP = 1;
		static final int RIGHT = 2;
		static final int BOTTOM = 3;
		static final int NOWALL = 0;
		static final int HAVEWALL = 1;
		
		int state =0;
		//��Ԫ�������λ��
		int x = 0;
		int y = 0;
		//��Ԫ�����Թ��е��к���
		int col = 0;   //��
		int row = 0;   //��
		//��Ԫ�񱻷��ʱ��
		boolean visited = false;
		//��Ԫ����road���Ƿ񱻷��ʱ��
		boolean roadVisited = false;
		/*
		 * ��Ԫ��ǽ��״̬,0:��ǽ,1:��ǽ
		 * ��ʾ˳����:��,��,��,��,��ʱ����ת˳��
		 * */
		int[] walls = new int[]{HAVEWALL,
				HAVEWALL,HAVEWALL,HAVEWALL};
		//���ݵ�Ԫ��λ�û�����Ԫ��
		void paint(Graphics g){
			g.setColor(WALL_COLOR);
			int x1=0,y1=0,x2=0,y2=0;
			for(int i=0;i<4;i++){
				if(walls[i] == NOWALL)
					continue;
				switch(i){
				//left
				case LEFT:
					x1 = this.x;
					y1 = this.y;
					x2 = x1;
					y2 = y1+width;
					break;
				//top
				case TOP:
					x1 = this.x;
					y1 = this.y;
					x2 = x1+width;
					y2 = y1;
					break;
				//right
				case RIGHT:
					x1 = this.x+width;
					y1 = this.y;
					x2 = x1;
					y2 = y1+width;
					break;
				case BOTTOM:
					x1 = this.x;
					y1 = this.y +width;
					x2 = x1+width;
					y2 = y1;
					break;
				}
				g.drawLine(x1, y1, x2, y2);
			}
		}
		boolean equals(Cell c){
			if(c.row==this.row && c.col==this.col)
				return true;
			else
				return false;
		}
	}
}





