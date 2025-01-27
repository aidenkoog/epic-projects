import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class SearchResultTable extends AbstractTableModel {
   	private Connection connection;
   	private Statement statement;
   	private ResultSet resultSet;
   	private ResultSetMetaData metaData;
   	private int numberOfRows;   
   	private boolean connectedToDatabase = false;
      
   	public SearchResultTable(String url, String username, String password, String query) throws SQLException {
   		connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
         		ResultSet.CONCUR_READ_ONLY 
		);
   		connectedToDatabase = true;		
   		setQuery(query);
   	}

   	public Class getColumnClass(int column) throws IllegalStateException{
        	if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database");
       
      		try {
         		String className = metaData.getColumnClassName(column + 1);         
                 	return Class.forName(className);
      		}
      		catch (Exception exception) {
         		exception.printStackTrace();
      		}      
      		return Object.class;
  	}

   	public int getColumnCount() throws IllegalStateException{   
        	if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database");

      		try {
         		return metaData.getColumnCount()-4; 
      		}
      		catch (SQLException sqlException) {
         		sqlException.printStackTrace();
      		}      
      		return 0;
   	}
   
	public String getColumnName(int column) throws IllegalStateException{    
      		if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database");

            try	{				
            	return metaData.getColumnName(column + 1);  
      		}
      		catch(SQLException sqlException){
         		sqlException.printStackTrace();
      		}      
      		return "";
   	}
   
   	public int getRowCount() throws IllegalStateException{      
        	if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database"); 
      		return numberOfRows;
   	}   
   	public Object getValueAt(int row, int column) throws IllegalStateException{
        	if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database");

            try{
         		resultSet.absolute(row + 1);
         		return resultSet.getObject(column + 1);
      		}
      		catch(SQLException sqlException){
         		sqlException.printStackTrace();
      		}      
      		return "";
   	}   
   	
   	public void setQuery(String query) throws SQLException, IllegalStateException {     
      		if (!connectedToDatabase) 
         		throw new IllegalStateException("Not Connected to Database");
     
      		resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();      
      		resultSet.last();                   
      		numberOfRows = resultSet.getRow();      
      
           	fireTableStructureChanged();
   	}     
           
   	public void disconnectFromDatabase() {              
      		if(connectedToDatabase) {                     
         		try {                                            
            			resultSet.close();                        
            			statement.close();                        
            			connection.close();                       
         		}                                
         		catch(SQLException sqlException) {                                            
            			sqlException.printStackTrace();           
         		}                              
         		finally {                                            
            			connectedToDatabase = false;              
         		}                             
      		}
   	}      
}