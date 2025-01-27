import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

public class DemoCanvas extends Canvas
{
    private Image img;
    private Graphics buffer;
    private Bar bar;
    private Stage st;
    Ball ball;
    DemoBallThread demoBThread;
        
    private final int TOP;
    private final int BOTTOM;
    private final int LEFT;
    private final int RIGHT;
    
    private Color[] brickColor = {Color.cyan, Color.red, Color.blue, Color.black, Color.white, Color.orange,
    								Color.darkGray, Color.magenta, Color.pink, Color.yellow};
    
    private int brokenBricks = 0;
    private int row = 6;
    private int col = 11;
    private String scoreStr;
    private String brickNumStr;
    private int userScore = 0;
    
	/*생성자*/
	public DemoCanvas(JPanel menuScreenCenter)
	{
		setSize(600, 500);
		
		brickNumStr = "0";
		scoreStr = null;
		img = null;
		buffer = null;
		bar = new Bar();
		ball = new Ball();
		st = new Stage(row, col);
		demoBThread = new DemoBallThread();	
		this.setBackground(null);
		brickInit();
		
		Insets insets = menuScreenCenter.getInsets();
		
		TOP = insets.top;
		LEFT = insets.left;
		BOTTOM = 180 - insets.bottom;
		RIGHT = 595 - insets.right;		
		
	}		
	/*벽돌 초기화*/
	public void brickInit()
	{
		st.stageDemo();		
	}
	public void reSet()
	{
		st = new Stage(row, col);
	}
	public void paint(Graphics g)
    {
		if(img == null)
    		img = createImage(600, 500);
    	if( buffer == null )
    		buffer = img.getGraphics();
		
		/*백 버퍼 지움*/
    	buffer.clearRect(0, 0, 600,500);
    	
    	bar.x = ball.x;    	
    	    	
    	/*벽돌*/
    	for(int i=0; i<st.brick.length; i++)
    	{
    		for(int j=0; j<st.brick[i].length; j++)
    		{
    			if(st.temp[i][j] == st.brick[i][j])
    			{
    				st.brick[i][j].HEIGHT = st.brick[i][j].WIDTH = 0;
    				st.brick[i][j].x = st.brick[i][j].y = 700;
    			}
    			else
    			{
    				buffer.setColor(brickColor[i]);
    				//buffer.setColor(brickColor[(int)(Math.random()*10)]);
    				buffer.fillRect(st.brick[i][j].x, st.brick[i][j].y, 
    								st.brick[i][j].WIDTH, st.brick[i][j].HEIGHT);  
    			}
    		}
    	}    	
    	
    	/*공*/
    	buffer.setColor(Color.blue);
    	buffer.fillOval(ball.x, ball.y, ball.SIZE, ball.SIZE);    	

    	/*바*/
    	buffer.setColor(Color.black);
    	if(bar.x-40 <= RIGHT-bar.WIDTH)
    		buffer.fillRect(bar.x-40, BOTTOM-5, bar.WIDTH, bar.HEIGHT);    	
    	else
    		buffer.fillRect(RIGHT-bar.WIDTH, BOTTOM-5, bar.WIDTH, bar.HEIGHT); 
    	
    	buffer.setFont(new Font("SanSerif", Font.BOLD, 15));
    	buffer.drawString("DEMO", 275, 230);
    	
    	buffer.setFont(new Font("SanSerif", Font.PLAIN, 15));
    	brickNumStr = String.format(" %d / %d ", brokenBricks, row*col);
    	buffer.drawString(brickNumStr, 10, BOTTOM+30);
    	scoreStr = String.format("SCORE : %d", userScore);
    	buffer.drawString(scoreStr, 10, BOTTOM+48);
    	
    	/*가상화면에 그려진 그림 복사*/
    	g.drawImage(img, 0, 0, this);
    }

	public void update(Graphics g)
	{
		paint(g);
	}	
	public void menuScreenThreadSet()
	{
		demoBThread.start();
	}	
	class DemoBallThread extends Thread
	{
		public void run()
		{
			while(true)
			{		
				if(brokenBricks == row*col)
				{
					reSet();
					brickInit();
					ball.x = 239;
					ball.y = 375;
					brokenBricks = 0;
				}
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
				
				/*벽돌 충돌*/
				OuterLoop:				
				for(int i=0; i<st.brick.length; i++)
				{
					for(int j=0; j<st.brick[i].length; j++)
					{						
						/*블록 아래쪽*/
						if((ball.y+ball.SIZE/2)>(st.brick[i][j].y+st.brick[i][j].HEIGHT) &&
								(ball.y+ball.SIZE/2)<= st.brick[i][j].y+st.brick[i][j].HEIGHT+12	
							&& (ball.x+ball.SIZE/2)>st.brick[i][j].x-12 && 
									(ball.x+ball.SIZE/2)<=(st.brick[i][j].x+st.brick[i][j].WIDTH+12))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.y = st.brick[i][j].y+st.brick[i][j].HEIGHT;
							ball.yStep = -ball.yStep;
							brokenBricks++;
							userScore += 8;
							break OuterLoop;
							
						}
						/*블록 위쪽*/						
						else if((ball.y+ball.SIZE/2)<st.brick[i][j].y && 
								(ball.y+ball.SIZE/2)>=(st.brick[i][j].y-12) &&
							    (ball.x+ball.SIZE/2)>=st.brick[i][j].x-12 && 
								(ball.x+ball.SIZE/2)<(st.brick[i][j].x+st.brick[i][j].WIDTH+12))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.y = st.brick[i][j].y-ball.SIZE;
							ball.yStep = -ball.yStep;
							brokenBricks++;
							userScore += 100;
							break OuterLoop;							
						}	
						
						/*블록 오른쪽*/ 
						if((ball.x+ball.SIZE/2)>(st.brick[i][j].x+st.brick[i][j].WIDTH) && 
								(ball.x+ball.SIZE/2)<=(st.brick[i][j].x+st.brick[i][j].WIDTH+12)
							&& (ball.y+ball.SIZE/2)>=st.brick[i][j].y-12 && 
									((ball.y+ball.SIZE/2)<st.brick[i][j].y+st.brick[i][j].HEIGHT+12))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.x = st.brick[i][j].x+st.brick[i][j].WIDTH;
							ball.xStep = -ball.xStep;
							brokenBricks++;
							userScore += 13;
							break OuterLoop;
							
						}
						/*블록 왼쪽*/						
						else if((ball.x+ball.SIZE/2)<st.brick[i][j].x && 
								(ball.x+ball.SIZE/2)>=(st.brick[i][j].x-12) && 
								(ball.y+ball.SIZE/2)>st.brick[i][j].y-12 && 
								(ball.y+ball.SIZE/2)<=(st.brick[i][j].y+st.brick[i][j].HEIGHT+12))
						{
							st.temp[i][j] = st.brick[i][j];
							ball.x = st.brick[i][j].x-ball.SIZE;
							ball.xStep = -ball.xStep;
							brokenBricks++;
							userScore += 50;
							break OuterLoop;							
						}						
					}
				}											
				repaint();
				
				try
				{
					sleep(8);
				}
				catch(InterruptedException e){}
			}
		}
	}	
}

