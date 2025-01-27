import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class DrawIntro1Canvas extends Canvas
{
	private Image img;
   	private Graphics buffer;
   	
   	public DrawIntro1Canvas()
   	{
   		
   	}
   	public void paint(Graphics g)
   	{
   		if(img == null)
			img = createImage(600, 500);
		if( buffer == null )
			buffer = img.getGraphics();
		
		
		buffer.setColor(Color.white);
		buffer.fillRect(200,300,200,40);
		buffer.fillOval(300,200,40,40);
		
		/*가상화면에 그려진 그림 복사*/
    	g.drawImage(img, 0, 0, this); 		
   	}
   	public void polyThdStart()
	{
		
	}
}
