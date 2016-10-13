import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;



public class Box extends JButton {
	private Integer val;
	private int row;
	private int col;
	private boolean opened;
	private boolean flagged;

	public Box(Integer v, int r, int c)
	{
		//super(v.toString());
		super();
		val = v;
		row=r;
		col=c;
		opened = false;
		flagged = false;
		addMouseListener( new MouseListener()
		{
			boolean pressed;
			boolean rightclick;
			
			public void mouseClicked(MouseEvent m)
			{
				//System.out.println("c");
				/**
				if(!Game.STARTED)
				{
					Game.makeMineMap(row, col);
					Game.STARTED = true;
				}
				
				
				Box b = (Box)(m.getSource());
				if(!b.getOpened())
				{
					
					if(SwingUtilities.isLeftMouseButton(m))
					{
						b.setText(val.toString());
						b.setOpened(true);
						int v = b.getVal();
						if(v==-1)
							System.out.println("You lose!! Munchkin!!");
						
						if(v==0)
						{
							int r1 = b.getRow();
							int c1 = b.getCol();
							Game.expand(r1,c1);
						}
					}
				}
				**/
			}

			@Override
			public void mousePressed(MouseEvent e) {
				pressed = true;
				if(SwingUtilities.isRightMouseButton(e))
					rightclick = true;
				else
					rightclick = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Box b = (Box)e.getComponent();
				
				if(pressed)
				{
					if(rightclick)
					{
						if(!b.opened)
						{
							if(!b.flagged)
							{
								b.flagged = true;
								b.setIcon(new ImageIcon("src/flag.png"));
								//b.setText("f");
								
							}
							else
							{
								b.unFlag();
							}
						}
						
					}
					else //left click
					{
						if(!Game.STARTED)
						{
							Game.makeMineMap(row, col);
							Game.STARTED = true;
						}
						if(!b.opened)
						{
							b.open();
							
						}
					}
				}
				pressed = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				pressed = true;
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pressed = false;
				
			}
			
			
		});
	}
	
	public int getVal()
	{
		return val;
	}
	
	public void setVal(int v)
	{
		val = v;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public void setOpened(boolean o)
	{
		opened = o;
	}
	
	public boolean getOpened()
	{
		return opened;
	}
	
	public void open()
	{
		setText(val.toString());
		setOpened(true);
		Game.addToNumOpened();
		if(Game.getNumOpened()+Game.getMines() 
				== Game.getSize()*Game.getSize())
			Game.win();
		if(val==-1)
			Game.lose();
		
		else if(val==0)
		{
			int r1 = getRow();
			int c1 = getCol();
			Game.expand(r1,c1);
		}
	}
	
	public boolean isFlagged()
	{
		return flagged;
	}
	
	public void unFlag()
	{
		flagged = false;
		setIcon(null);
	}
}
