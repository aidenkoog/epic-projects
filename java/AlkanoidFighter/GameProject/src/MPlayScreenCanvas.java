import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

public class MPlayScreenCanvas extends Canvas
{
	private Image img;
    private Graphics buffer;
    private Bar bar;
    private Stage st;
    Ball ball;
    MBallThread bThread;
        
    private final int TOP;
    private final int BOTTOM;
    private final int LEFT;
    private final int RIGHT;
    
    private Color[] brickColor = {Color.cyan, Color.red, Color.blue, Color.black, Color.white, Color.orange,
    								Color.darkGray, Color.magenta, Color.pink, Color.yellow};
    
	public MPlayScreenCanvas(JPanel multiplayScreenCenter)
	{
		setSize(600, 500);
		
		img = null;
		buffer = null;
		bar = new Bar();
		ball = new Ball();
		st = new Stage(8,8);
		bThread = new MBallThread();			
		setBackground(Color.green);			
		
		Insets insets = multiplayScreenCenter.getInsets();
		
		TOP = insets.top;
		LEFT = insets.left;
		BOTTOM = 440 - insets.bottom;
		RIGHT = 510 - insets.right;
	}
	class MBallThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				/*공 이동 방향*/
				ball.x += ball.xStep;
				ball.y += ball.yStep;
				
				/*벽 왼쪽*/
				if(ball.x <= LEFT)
				{
					ball.x = LEFT;
					ball.xStep = -ball.xStep;
				}
				/*벽 오른쪽*/
				if(ball.x >= RIGHT-ball.SIZE)
				{
					ball.x = RIGHT-ball.SIZE;
					ball.xStep = -ball.xStep;
				}
				/*벽 위쪽*/
				if(ball.y <= TOP)
				{
					ball.y = TOP;
					ball.yStep = -ball.yStep;
				}
				/*벽 아래쪽*/
				if(ball.y >= BOTTOM-ball.SIZE)
				{
					ball.y = BOTTOM-ball.SIZE;
					ball.yStep = -ball.yStep;
				}
				/*바 위쪽*/
				if(((ball.y+ball.SIZE/2)-400)>=0
						&& (((ball.x+ball.SIZE/2)>=bar.x) && ((ball.x+ball.SIZE/2)<=(bar.x+bar.WIDTH))))
				{
					ball.y = 400-ball.SIZE;
					ball.yStep = -ball.yStep;
				}
				
				/*벽돌 충돌*/
				OuterLoop:				
				for(int i=0; i<st.brick.length; i++)
				{
					for(int j=0; j<st.brick[i].length; j++)
					{
						/*블록 아래쪽*/
						if((ball.y+ball.SIZE/2)>(st.brick[i][j].y+st.brick[i][j].HEIGHT) &&
								(ball.y+ball.SIZE/2)<= st.brick[i][j].y+st.brick[i][j].HEIGHT+12	
							&& (ball.x+ball.SIZE/2)>st.brick[i][j].x && 
									(ball.x+ball.SIZE/2)<=(st.brick[i][j].x+st.brick[i][j].WIDTH))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.y = st.brick[i][j].y+st.brick[i][j].HEIGHT;
							ball.yStep = -ball.yStep;
							break OuterLoop;
							
						}
						/*블록 위쪽*/						
						else if((ball.y+ball.SIZE/2)<st.brick[i][j].y && 
								(ball.y+ball.SIZE/2)>=(st.brick[i][j].y+12) &&
							    (ball.x+ball.SIZE/2)>=st.brick[i][j].x && 
								(ball.x+ball.SIZE/2)<(st.brick[i][j].x+st.brick[i][j].WIDTH))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.y = st.brick[i][j].y-ball.SIZE;
							ball.yStep = -ball.yStep;
							break OuterLoop;							
						}	
						
						/*블록 오른쪽*/ 
						if((ball.x+ball.SIZE/2)>(st.brick[i][j].x+st.brick[i][j].WIDTH) && 
								(ball.x+ball.SIZE/2)<=(st.brick[i][j].x+st.brick[i][j].WIDTH+12)
							&& (ball.y+ball.SIZE/2)>=st.brick[i][j].y && 
									((ball.y+ball.SIZE/2)<st.brick[i][j].y+st.brick[i][j].HEIGHT))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.x = st.brick[i][j].x+st.brick[i][j].WIDTH;
							ball.xStep = -ball.xStep;
							break OuterLoop;
							
						}
						/*블록 왼쪽*/						
						else if((ball.x+ball.SIZE/2)<st.brick[i][j].x && 
								(ball.x+ball.SIZE/2)>=(st.brick[i][j].x-12) && 
								(ball.y+ball.SIZE/2)>st.brick[i][j].y && 
								(ball.y+ball.SIZE/2)<=(st.brick[i][j].y+st.brick[i][j].HEIGHT))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.x = st.brick[i][j].x-ball.SIZE;
							ball.xStep = -ball.xStep;
							break OuterLoop;							
						}						
					}
				}											
				repaint();
				
				try
				{
					sleep(10);
				}
				catch(InterruptedException e){}
			}
		}
	}
}
