import java.awt.Canvas;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class DrawIntro2Canvas extends Canvas
{
	private Image img;
   	private Graphics buffer;
	private StrDrawThread strThd;
	private int y; 
	private int xPos;
	private int yPos;
	private int[] tempX;
	private int[] tempY;
	private int count;
	private boolean[] setStringChk;
	private final String[] strArr = {"A", "L", "K", "A", "N", "O", "I", "D"};
	private final String str = "F I G H T E R";
	private int fontSize;
	private int fontStyle;

	
	public DrawIntro2Canvas()
	{
		img = null;
		buffer = null;
		y = 0;
		xPos = 0;
		yPos = 0;
		count = 1;
		tempX = new int[strArr.length];
		tempY = new int[strArr.length];
		setStringChk = new boolean[strArr.length];		
		fontSize = 20;
		fontStyle = Font.PLAIN;
		strThd = new StrDrawThread();
				
	}
	public void strThdStart()
	{
		strThd.start();
	}
	public void paint(Graphics g)
	{
		if(img == null)
    			img = createImage(600, 500);
    		if( buffer == null )
    			buffer = img.getGraphics();
    	
    	/*백 버퍼 지움*/
    	buffer.clearRect(0, 0, 600,500);
    	
    	buffer.setColor(Color.white);    
    	buffer.setFont(new Font("SanSerif", Font.PLAIN, fontSize));
    	for(int i=0; i<setStringChk.length; i++)
    	{    		
    		if(setStringChk[i] == true)
    			buffer.drawString(strArr[i], tempX[i], tempY[i]);
    	}
    	
    	if(count <= 8)
    		buffer.drawString(strArr[count-1], xPos, yPos);
    	else
    	{
    		buffer.setFont(new Font("SanSerif", fontStyle, fontSize));
    		buffer.drawString(str, 220, 230);
    	}
    	
    	    	
    	/*가상화면에 그려진 그림 복사*/
    	g.drawImage(img, 0, 0, this);    	
  	} 
	public void update(Graphics g)
	{
		paint(g);
	}
	public void sleepFunc(int mill)
	{
		try
		{
			strThd.sleep(mill);
		}
		catch(InterruptedException e){}
	}
	class StrDrawThread extends Thread
	{				
		public void run()
		{			
			while(count<9)
			{				
				y += 5;		
				
				if(yPos >= 170)
				{					
					tempX[count-1] = xPos;
					tempY[count-1] = yPos;					
					setStringChk[count-1] = true;
					count++;					
					y=0;
					yPos = 0;
				}
				else
				{
					xPos = 60+(count-1)*65;
					yPos = 100+y;
					repaint();
				}
				sleepFunc(30);
				
			}			
			sleepFunc(20);
			
			if(count == 9)
				repaint();	
			
			while(true)
			{
				fontSize += 2;
				
				if(fontSize == 50)
				{
					sleepFunc(1000);
					fontStyle = Font.BOLD;
					repaint();
					break;
				}
				
				repaint();
				sleepFunc(20);
			}
		}
	}
}
