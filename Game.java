import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

public class Game {
	private static int siz;
	private static int mns;
	private static Box[][] minelayout;
	private static int numOpened=0;
	private static JFrame fr;
	private static GridLayout gr;
	public static boolean STARTED = false;

	/**
	 * 
	 * @param size is the length of the board being used
	 * @param mines is the number of mines to be put into the map.
	 */
	public Game(int size, int mines)
	{
		if(size<2)
			size = 30;
		
		int tot = size*size;
		if(mines>=tot)
			mines = (int)(tot/5); //arbitrary way to make the game nice
		
		siz = size;
		mns = mines;
		fr = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gr = new GridLayout(size,size);
		minelayout = new Box[size][size];
		fr.setLayout(gr);

		for(int i = 0;i<size;i++)
		{
			for(int j = 0;j<size;j++)
			{
				Box b = new Box(0,i,j); //need some default boxes
				fr.add(b);
				minelayout[i][j]=b;
			}
		}
		fr.setSize(25*size, 25*size);
		fr.setVisible(true);
	}
	

	
	/**
	 * makes a random map of mines (designated by -1)
	 * and numbers, where each number that is not -1 refers
	 * to the number of mines within one square of the number
	 * in all 8 directions.
	 * 
	 * the adjacent squares (max 3 by 3 grid) to the location (r1,c1) (in rows and columns) 
	 * should not have mines in order to start off the game well.
	 * 
	 * @param r1 the row value around which the mine map will be created
	 * @param
	 * 
	 * @return
	 */
	public static void makeMineMap(int r1, int c1)
	{
		
		int tot = siz*siz;
		numOpened = 0;
		int[][] map = new int[siz][siz];
		Set<Integer> mineSet = new HashSet<Integer>();
		//implementation assumes that   mines <<< sz^2
		
		//count describes how many "false" mines have been added so we later know how many to remove
		int count = 0;
		
		//adds in mine locations to the 3x3 around start point so they can't be added in by the random process
		for(int r = r1-1; r<=r1+1; r++)
		{
			for(int c= c1-1;c<=c1+1;c++)
			{
				if(r>=0 && r<siz && c>=0 && c<siz && (map[r][c]!=-1))
				{
					mineSet.add(r*siz + c);
					count++;
				}
			}
		}
		
		//puts in the random mine locations
		while(mineSet.size()<mns+count)
		{
			mineSet.add(new Integer((int)(Math.random()*tot)));
		}
		
		
		//removes the "fake" mine locations in order to make the area around (r1,c1) clear of mines.
		for(int r = r1-1; r<=r1+1; r++)
		{
			for(int c= c1-1;c<=c1+1;c++)
			{
				if(r>=0 && r<siz && c>=0 && c<siz)
				{
					mineSet.remove(r*siz + c);
				}
			}
		}
		
		
		
		//designates mines as -1
		Iterator<Integer> itr = mineSet.iterator();
		while(itr.hasNext())
		{
			int num = itr.next().intValue();
			int row = num/siz;
			int col = num%siz;
			
			//adds the number of mines in adjoining squares 
			//as it iterates
			for(int r = row-1; r<=row+1; r++)
			{
				for(int c= col-1;c<=col+1;c++)
				{
					if(r>=0 && r<siz && c>=0 && c<siz && (map[r][c]!=-1))
					{
						map[r][c]++;
					}
				}
			}
			
			map[row][col] = -1;
		}
		
		setMineMap(map);
	}
	
	/**
	 * Sets up the Gui and instance variables of each Box on the back end of the game.
	 * The game is never in this "unopened but ready to play" state for long: a user click
	 * triggers the creating and setting of the mine map, after which boxes are immediately opened.
	 * 
	 * @param map The map of mines (-1) and other integer values (0 to 8) that underlays the minesweeper game
	 */
	public static void setMineMap(int[][] map)
	{
		for(int i = 0;i<siz;i++)
		{
			for(int j = 0;j<siz;j++)
			{
				Box b = minelayout[i][j];
				b.setVal(map[i][j]);
				b.setText("");
				b.setOpened(false);
				b.unFlag();
			}
		}
		fr.repaint();
	}
	/**
	 * PRECONDITION: BOX AT r1, c1 HAS NO SURROUNDING MINES!!!
	 * 
	 * starts at a location r1,c1 and opens all adjacent squares that are either zeros
	 * or numbers. expand is then called on all the newly opened squares with value 0.
	 * 
	 * @param r1
	 * @param c1
	 */
	public static void expand(int r1, int c1)
	{
		for(int r = r1-1; r<=r1+1; r++)
		{
			for(int c= c1-1;c<=c1+1;c++)
			{
				
				if(r>=0 && r<siz && c>=0 && c<siz)
				{
					Box b = minelayout[r][c];
					if(!b.getOpened() && !b.isFlagged())
					{
						b.open();
						if(b.getVal()==0)
							expand(r,c);
					}
				}
			}
		}
	}
	
	public static void win()
	{
		System.out.println("You win! Click any tile to start a new game.");
		Game.STARTED=false;
	}
	
	public static void lose()
	{
		System.out.println("You lose!! Munchkin!! Click any tile to start a new game.");
		Game.STARTED=false;
	}
	
	public static int getSize()
	{
		return siz;
	}
	
	public static int getMines()
	{
		return mns;
	}
	
	public static Box[][] getMineLayout()
	{
		return minelayout;
	}
	
	public static void addToNumOpened()
	{
		numOpened++;
	}
	
	public static int getNumOpened()
	{
		return numOpened;
	}
}


