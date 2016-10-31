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
	private int mazeRowCount = 10; //迷宫行数
	private int mazeColCount = 12; //迷宫列数
	private int width = 10;   //迷宫单元格宽度
	//迷宫边框颜色
	private static final Color WALL_COLOR = Color.black;
	//0点位置
	private int zeroX = width;
	private int zeroY = width;
	private Random random = null;
	//已经被访问的单元格
	private Cell[] visitedCells = null;
	private int visitedCount = 0;
	private int cellCount = 0;
	/*
	 * 迷宫map[列数][行数],就是x轴与y轴map[x][y]
	 * 坐标0点为画布的左上角,0点向右为x的正方向,0点向下表示y的正方向
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
		//初始化迷宫
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
		//画出迷宫
		paintMaze(g);
		//画出路径
		paintRoad(g);
	}
	
	private void createMaze() {
		//随机开始dian
		initMaze(map[random.nextInt(mazeColCount)][random.nextInt(mazeRowCount)]);
		//设置开始结束点
		map[0][mazeRowCount-1].walls[Cell.BOTTOM]
				= Cell.NOWALL;
		map[mazeColCount-1][0].walls[Cell.TOP]
				= Cell.NOWALL;
	}
	private void initMaze(Cell theCell){
		//单元格全部访问完毕,则创建完毕
		if(visitedCount == cellCount)
			return;
		if(!isVisited(theCell)){
			theCell.visited = true;
			visitedCells[visitedCount++] = theCell;
		}
		//得到有效的相邻单元格
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
		//如果邻近的单元格都被访问了
		if(neibCount==0){
			initMaze(visitedCells[random.nextInt(visitedCount)]);
			return ;
		}
		//随机得到下一个单元格
		nextCell = neighbours[random.nextInt(neibCount)];
		//去掉单元格和选择的邻近单元格之间的墙
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
	 * 单元格是否被访问过
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
		//先深搜出路径,输出map.col,map.row的序列,然后在依据这个序列画出路径
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
		Cell cell = map[0][mazeRowCount-1];   //设置起始点
		sc.push(cell);
		while(!cellIsFinally(cell)){
			//弹多的情况
			//和getbian的问题
			if(cell!=null)
				cell = (Cell)GetAround(cell);
			if(cell==null)
				//如果到了死胡同
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
		 * 返回的cell按照左,上,右,下的顺序
		 * */
		//先判断cell的位置
		for(int y=0;y<mazeColCount;y++)
			for(int x=0;x<mazeRowCount;x++){
				if(map[x][y].equals(thecell)){  //在map中找到thecell的位置
					if(thecell.walls[Cell.LEFT]==Cell.NOWALL
							&&map[x-1][y].roadVisited==false){
						//必须是没有墙,而且那边的单元格没有被访问过的
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
			System.out.println("是最后一个点");
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
		//单元格的像素位置
		int x = 0;
		int y = 0;
		//单元格在迷宫中的行和列
		int col = 0;   //列
		int row = 0;   //行
		//单元格被访问标记
		boolean visited = false;
		//单元格在road中是否被访问标记
		boolean roadVisited = false;
		/*
		 * 单元格墙体状态,0:无墙,1:有墙
		 * 表示顺序是:左,上,右,下,逆时针旋转顺序
		 * */
		int[] walls = new int[]{HAVEWALL,
				HAVEWALL,HAVEWALL,HAVEWALL};
		//根据单元格位置画出单元格
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





