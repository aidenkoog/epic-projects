/*°ø*/
public class Ball
{
	public int x;
	public int y;
	final int SIZE;	
	final int SPEED;
		
	public int xStep;
	public int yStep;
		
	public Ball()
	{
		x = 239;
		y = 375;
		SIZE = 20;
		SPEED = 3;
		xStep = SPEED;
		yStep = SPEED;
	}		
}