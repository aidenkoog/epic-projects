public class Stage
{
	Brick[][] brick;
	Brick[][] temp;
	int row;
	int col;
		
	public Stage(int row, int col)
	{		
		this.row = row;
		this.col = col;
				
		brick = new Brick[row][col];		
		temp = new Brick[row][col];
		
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++)
    		   	temp[i][j] = new Brick(0,0);
		
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++)
    		   	temp[i][j].item = 1;
		
	}
	public void stageChange(int stage)
	{
		switch(stage)
		{
		case 1:
			stageOne();
			break;
		case 2:
			stageTwo();
			break;
		case 3:
			stageThree();
			break;
		case 4:
			stageFour();
			break;
		}
	}
	public void stageOne()
	{
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++) 			
    			brick[i][j] = new Brick(10+j*52, 10+i*22);
	}
	public void stageTwo()
	{
		for(int i=0; i<row; i++)
    		for(int j=0; j<9; j++) 			
    			brick[i][j] = new Brick(10+j*51, 10+i*21);
	}
	public void stageThree()
	{
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++) 			
    			brick[i][j] = new Brick(10+j*51, 10+i*21);
	}
	public void stageFour()
	{
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++) 			
    			brick[i][j] = new Brick(10+j*51, 10+i*21);
	}
	public void stageDemo()
	{
		for(int i=0; i<row; i++)
    		for(int j=0; j<col; j++) 			
    			brick[i][j] = new Brick(10+j*52, 10+i*23);
	}
}
