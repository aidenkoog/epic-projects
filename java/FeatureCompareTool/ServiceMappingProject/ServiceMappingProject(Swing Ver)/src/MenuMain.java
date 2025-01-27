import javax.swing.JFrame;

public class MenuMain{
	public static void main(String args[]){ 
     	MenuFrame menuFrame = new MenuFrame(); 				
      	menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	
     	menuFrame.setResizable(false);  				
		menuFrame.setLocation(200,200); 				
		menuFrame.setSize(620,500);    					
		menuFrame.setVisible(true);     				
   	}
}

