import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.border.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

import java.util.Enumeration;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.sql.SQLException;

public class MenuFrame extends JFrame 		
{   	
	//DB연동 관련 변수	
	private ServiceQuery serviceQuery;
	private List<Service> results;
	private int numberOfData = 0;
	private Service currentData;
	private int currentDataIdx;
	private Enumeration<AbstractButton> enums;
	
	
	//Register관련 변수
	private JLabel sName;				//레이블
	private JLabel uDomain;
	private JLabel task;
	private JLabel snonySName;
	private JLabel priority;
	private JLabel interfaceName;
	private JLabel component;
	private JLabel def;
	private JLabel serviceAtr;
	private JLabel identifier;
	private JLabel queryLabel;
	private JLabel regAuthority;
	
	private JTextField sText;			//텍스트 필드
	private JTextField uDomainText;
	private JTextField taskText;
	private JTextField snonyText;
	private JTextField interfaceText;
	private JTextField componentText;	
	private JTextField idText;
	private JTextField regText;
	
	private JRadioButton veryImp;			//라디오버튼
	private JRadioButton imp;
	private JRadioButton notImp;
	private JRadioButton mandatory;
	private JRadioButton optional;
	private JRadioButton alternative;

	private ButtonGroup priorRadioGroup;
	private ButtonGroup serviceAtrRadioGroup;

	private JScrollPane scroll;
	private JTextArea defArea;
	private JTextField queryTextField;

	private JButton queryButton;

	private SmallButton regBtn;			//버튼
	private SmallButton initialBtn;
	private SmallButton cancelBtn;
	private SmallButton allShowBtn;
	
	private Border iBorder;		
	private Border sBorder;
	private Border mBorder;

	private JPanel iBorderPanel;
	private JPanel sBorderPanel;
	private JPanel mBorderPanel;
	private JPanel queryPanel;
	private JPanel btnPanel;

	//URL, 이름, 비번
	private static final String DATABASE_URL = "jdbc:mysql://localhost/oldservices";
   	private static final String USERNAME = "root";
   	private static final String PASSWORD = "2001";   
   	
   	private static final String DEFAULT_QUERY = "SELECT * FROM oldservices";
   
   	private SearchResultTable tableModel;
   	
   	private boolean mappingBtnCheck = false;
   	
   	/////////////////////////////////////////
   	//그래프 그리기 위한 변수
   	private int commonH;	//유사성
   	private int variH;		//비유사성
   	////////////////////////////////////////
   	
   	
   	JTextField comText;
		
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
   	//MenuFrame 생성자 함수
	public MenuFrame()
   	{
		super("Mapping Tool Project");     //상위 생성자 호출, 타이틀 바 제목 

		//serviceQuery = new ServiceQuery(); //데이터베이스 연결하고 PreparedStatements 셋업			
					
		////////////////////////////////////////////////////
		//1. File 메뉴 생성
		////////////////////////////////////////////////////
		JMenu fileMenu = new JMenu("File");		
		JMenuItem firstOtlItem = new JMenuItem("Open First Service");
		JMenuItem secondOtlItem = new JMenuItem("Open Second Service");
		JMenuItem subSearchItem = new JMenuItem("Service Search");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		//단축키 설정 
		fileMenu.setMnemonic('f');	//메뉴
		firstOtlItem.setMnemonic('f');
		secondOtlItem.setMnemonic('s');
		subSearchItem.setMnemonic('v');
      		exitItem.setMnemonic('x');
		
		//File에 하위메뉴 추가
		fileMenu.add(firstOtlItem);
		fileMenu.add(secondOtlItem);
		fileMenu.add(subSearchItem); 
		fileMenu.add(exitItem); 	

		////////////////////////////////////////////////////
		//2. Edit 메뉴 생성
		////////////////////////////////////////////////////
		JMenu editMenu = new JMenu("Edit");		
		JMenuItem registerItem = new JMenuItem("Register");	
		JMenuItem updateItem = new JMenuItem("Update");	
		JMenuItem deleteItem = new JMenuItem("Delete");		
			
		//단축키 설정
		editMenu.setMnemonic('e');	//메뉴
		registerItem.setMnemonic('r');
		updateItem.setMnemonic('u');
		deleteItem.setMnemonic('d');

		//Edit에 하위메뉴 추가
		editMenu.add(registerItem);
		editMenu.add(updateItem);
		editMenu.add(deleteItem);			
		      	
		////////////////////////////////////////////////////
		//3. Tool 메뉴 생성
		////////////////////////////////////////////////////
		JMenu toolMenu = new JMenu("Tool");
		JMenuItem toolItem = new JMenuItem("Similarity Check");
		
		//단축키 설정
		toolMenu.setMnemonic('t');	//메뉴
		toolItem.setMnemonic('c');

		//Tool에 하위메뉴 추가
		toolMenu.add(toolItem);

		////////////////////////////////////////////////////
		//4. Help 메뉴 생성
		////////////////////////////////////////////////////
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpItem = new JMenuItem("Information...");

		//단축키 설정
		helpMenu.setMnemonic('h');	//메뉴
      		helpItem.setMnemonic('i');
      		
		//Help에 하위메뉴 추가
		helpMenu.add(helpItem);				

		//메뉴바에 항목 추가
		JMenuBar bar = new JMenuBar(); 
      	setJMenuBar(bar);
    	bar.add(fileMenu); 
		bar.add(editMenu);
		bar.add(toolMenu);
		bar.add(helpMenu);
					
		//File하위메뉴 Exit
		exitItem.addActionListener(

         		new ActionListener()
         		{  
                       	public void actionPerformed(ActionEvent event)
            			{
               				System.exit(0); //프로그램 종료
            			}
         		}
      		);
		firstOtlItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						mappingBtnCheck=false;	//유사성 그래프 지우기
						firstFileOpen();
					}
				}
		);
		secondOtlItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						mappingBtnCheck=false;
						secondFileOpen();
					}
				}
		);
		subSearchItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						mappingBtnCheck=false;
						searchMenu();
					}
				}
		);

		//Edit하위메뉴 Register
		registerItem.addActionListener(

			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					//등록화면 보이기
					mappingBtnCheck=false;
					showRegisterForm(event);
				}
			}
		);	
		deleteItem.addActionListener(
	
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					mappingBtnCheck=false;
					frameClean();
					displayDeleteResults();
				}
			}
		);
		////////////////////////////////////////////////////////////////////////////////////////
		updateItem.addActionListener(
		
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mappingBtnCheck=false;
					frameClean();
					displayUpdateResults();
				}
			}
		);
		////////////////////////////////////////////////////////////////////////////////////////
		//Tool하위메뉴 Similarity Check
		toolItem.addActionListener(

			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					mappingBtnCheck=false;
					frameClean();
					displaySimilarityCheck();
				}
			}
		);

		//Help하위메뉴 Information
      		helpItem.addActionListener(
      	
      				new ActionListener()
      				{
      					public void actionPerformed(ActionEvent event)
      					{
      						//대화상자
      						JOptionPane.showMessageDialog(MenuFrame.this, "Mapping Tool Project",
        	          						"Information", JOptionPane.PLAIN_MESSAGE);
      					} 
      				}
      		);	

   	} //MenuFrame 생성자 끝
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
	//컴포넌트 지우고 컨텐트패인 초기화           //차후에 cardLayout으로 전환
	public void frameClean()
	{
		Container newcontent = getContentPane();
		newcontent.removeAll();		//컴포넌트 모두 제거
		setContentPane(newcontent);
	}
	////////////////////////////////////////////////
	
	//첫번째 파일 열기
	public void firstFileOpen()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(
				JFileChooser.FILES_AND_DIRECTORIES);
		
		int result = fileChooser.showOpenDialog(this);
		//if(result == JFileChooser.CANCEL_OPTION)
		//	System.exit(1);
		
		File fileName = fileChooser.getSelectedFile();
		
		if((fileName == null) || (fileName.getName().equals(""))){
			JOptionPane.showMessageDialog(this, "유효하지 않은 파일 이름입니다.",
						"유효하지 않은 파일 이름", JOptionPane.ERROR_MESSAGE);
			//System.exit(1);
		}
	}
	//두번째 파일 열기
	public void secondFileOpen()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(
				JFileChooser.FILES_AND_DIRECTORIES);
		
		int result = fileChooser.showOpenDialog(this);
		//if(result == JFileChooser.CANCEL_OPTION)
		//	System.exit(1);
		
		File fileName = fileChooser.getSelectedFile();
		
		if((fileName == null) || (fileName.getName().equals(""))){
			JOptionPane.showMessageDialog(this, "유효하지 않은 파일 이름입니다.",
						"유효하지 않은 파일 이름", JOptionPane.ERROR_MESSAGE);
			//System.exit(1);
		}
	}
	//검색 메뉴
	public void searchMenu()
	{
		frameClean();
		//setLayout(new FlowLayout(FlowLayout.LEFT));		
		
		//전체 레이아웃
		setLayout(new BorderLayout());
		//서비스 검색하는 필드 영역
				queryLabel = new JLabel("Service Name : ");
				queryLabel.setOpaque(true);
				queryLabel.setBackground(Color.lightGray);

				queryTextField = new JTextField(20);
				queryButton = new JButton("Search");
				queryPanel = new JPanel();
				queryPanel.setLayout(new FlowLayout());		
				queryPanel.setBorder(BorderFactory.createTitledBorder("Find a data by service name"));
				queryPanel.add(Box.createHorizontalStrut(5));
				queryPanel.add(queryLabel);
				queryPanel.add(Box.createHorizontalStrut(10));
				queryPanel.add(queryTextField);	
				queryPanel.add(Box.createHorizontalStrut(10));
				queryPanel.add(queryButton);
				queryPanel.add(new JLabel("  		 "));
				queryPanel.add(new JLabel("   		 "));
				queryPanel.add(new JLabel("  		 "));
				queryPanel.add(new JLabel("  		 "));	
				queryPanel.add(new JLabel("  		 "));
				queryPanel.add(new JLabel("  		"));
				queryPanel.add(new JLabel("  		"));
				queryPanel.add(Box.createHorizontalStrut(5));	
				
				///////////////////////////////////////////////////////////////////////////////////////Search//////////////////////////////////////////////
				//Search 버튼 액션리스너	
				//Search 버튼 눌리면 해당하는 서비스 이름에 해당하는 DB내용이 출력된다.						
				queryButton.addActionListener(
					
					new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							String str = queryTextField.getText();	
							if(str.length() <= 0){
								JOptionPane.showMessageDialog(null, "입력된 값이 없습니다!", "Search", 
										JOptionPane.PLAIN_MESSAGE);
								//showRegisterForm(evt);
							}
							else
							{
								frameClean();								
								displayBySearchBtn(str);
							}
						}
					}
				);
				
				JPanel btnPanel = new JPanel();
				//btnPanel.setLayout(new FlowLayout());
				allShowBtn = new SmallButton("All Datas Show");	
				SmallButton cancelBtn = new SmallButton("Cancel");
				btnPanel.add(allShowBtn);
				btnPanel.add(cancelBtn);
				
				//모든 데이터 보기
				allShowBtn.addActionListener(
						new ActionListener(){
							public void actionPerformed(ActionEvent event){
								frameClean();
								displaySearchResults(); 
							}
						}
				);					
				//취소버튼
				cancelBtn.addActionListener(
						new ActionListener(){
							public void actionPerformed(ActionEvent e){
								frameClean();
							}
						}
				);
									
				add(queryPanel, BorderLayout.CENTER);
				add(btnPanel, BorderLayout.SOUTH);
				setVisible(true);
	}
	//페인트 함수//////////////////////////////////////////////////////
	public void paint(Graphics g)
	{
		super.paint(g);
		
		if(mappingBtnCheck == true)			//맵핑버튼 눌러지면
		{			
			//표
			g.drawLine(5, 130, 220, 130);
			g.drawLine(5, 130, 5, 190);
			g.drawLine(5, 190, 220, 190);
			g.drawLine(220, 130, 220, 190);
			
			g.drawLine(5, 145, 220, 145);
			g.drawLine(5, 160, 220, 160);
			g.drawLine(5, 175, 220, 175);
			
			g.drawLine(100, 145, 100, 190);
			
			//그래프 퍼센트
			g.drawString("(%)", 318, 95);
			g.drawString("100", 313, 108);
			g.drawLine(336, 100, 344, 100);
			
			g.drawString("50", 320, 155);
			g.drawLine(336, 150, 344, 150);
			
			g.drawString("75", 320, 130);
			g.drawLine(336, 125, 344, 125);
			
			//그래프 틀
			g.drawLine(340, 100, 340, 200);
			g.drawLine(340, 200, 460, 200);
			
			g.setColor(Color.blue);
			g.fillRect(360, 200-commonH, 30, commonH);
			
			g.setColor(Color.red);
			g.fillRect(420, 200-variH, 30, variH);
			
			//색 안내 표시
			g.setColor(Color.blue);
			g.fillRect(465, 100, 20, 10);
			
			g.setColor(Color.red);
			g.fillRect(465, 120, 20, 10);
			
			g.setColor(Color.black);
			g.drawString("commonality", 490, 110);
			g.drawString("variability", 490, 130);
			
			//원그래프
			int commonAngle = (int)((360*commonH)/100);
			int variAngle = -(int)(360-commonAngle);
			
			g.setColor(Color.black);
			g.drawString("25(%)", 390, 240);
			g.drawLine(395, 245, 395, 255);
			
			g.setColor(Color.black);
			g.drawString("50", 300, 330);
			g.drawLine(315, 325, 325, 325);
			
			g.setColor(Color.black);
			g.drawString("75", 390, 420);
			g.drawLine(395, 405, 395, 395);
			
			g.setColor(Color.black);
			g.drawString("0 / 100", 480, 330);
			g.drawLine(475, 325, 465, 325);
			
			//유사성, 비유사성 보이기
			g.setColor(Color.blue);
			g.fillArc(320, 250, 150, 150, 0, commonAngle);
			
			g.setColor(Color.red);
			g.fillArc(320, 250, 150, 150, 360, variAngle);
			
			//색 안내 표시
			g.setColor(Color.blue);
			g.fillRect(465, 250, 20, 10);
			
			g.setColor(Color.red);
			g.fillRect(465, 270, 20, 10);
			
			g.setColor(Color.black);
			g.drawString("commonality", 490, 260);
			g.drawString("variability", 490, 280);
		}
		else	//유사성 맵핑 버튼 false일 때
		{
			
		}
	}
	//update함수
	public void update(Graphics g)
	{
		paint(g);
	}
	//서비스 유사성 체크 함수
	private void displaySimilarityCheck()
	{
		setLayout(new BorderLayout());

		final JPanel classPanel = new JPanel();
		classPanel.setLayout(new FlowLayout());
		classPanel.setOpaque(true);
		classPanel.setBackground(Color.lightGray);
		
		JLabel firstClassLb = new JLabel("1st Service Name : ");
		JLabel secondClassLb = new JLabel("2nd Service Name : ");			
		final JTextField firstField = new JTextField(10);
		final JTextField secondField = new JTextField(10);	
		SmallButton searchBtn1 = new SmallButton("Open");
		SmallButton searchBtn2 = new SmallButton("Open");
		SmallButton mappingBtn = new SmallButton("Similarity Mapping");
		SmallButton cancelBtn = new SmallButton("Cancel");
		
		final JPanel mappingPanel = new JPanel();
		mappingPanel.setOpaque(true);
		mappingPanel.setBackground(Color.lightGray);
		mappingPanel.add(mappingBtn);
		mappingPanel.add(cancelBtn);

		classPanel.add(firstClassLb);
		classPanel.add(firstField);
		classPanel.add(searchBtn1);
		classPanel.add(secondClassLb);
		classPanel.add(secondField);
		classPanel.add(searchBtn2);

		add(classPanel, BorderLayout.NORTH);
		add(mappingPanel, BorderLayout.SOUTH);

		//취소버튼
		cancelBtn.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						mappingBtnCheck=false;
						frameClean();
					}
				}
		);

		//맵핑버튼
		mappingBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frameClean();
					mappingBtnCheck = true;
					int count = 0;			
		
					results = serviceQuery.getServiceBySName(firstField.getText());
					numberOfData = results.size();					
					currentDataIdx = 0;
					currentData = results.get(currentDataIdx);
			
					String[] firstClassArr = new String[]{currentData.getSName(), currentData.getUDName(), currentData.getTask(),
								  		currentData.getSSName(), currentData.getInterfaceName(), currentData.getComponent(),
								  		currentData.getServiceID(), currentData.getRegAuthority(), currentData.getDefinition()};

					results = serviceQuery.getServiceBySName(secondField.getText());
					numberOfData = results.size();					
					currentDataIdx = 0;
					currentData = results.get(currentDataIdx);
			
					String[] secondClassArr = new String[]{currentData.getSName(), currentData.getUDName(), currentData.getTask(),
								  		currentData.getSSName(), currentData.getInterfaceName(), currentData.getComponent(),
								  		currentData.getServiceID(), currentData.getRegAuthority(), currentData.getDefinition()};				
					
					for(int i=1; i<=9; i++){
						if(firstClassArr[i-1].equals(secondClassArr[i-1]))
							count++;
					}
					double commonality = (((double)count)/9)*100.0;			//유사성
					commonality = Math.round(commonality*100.0)/100.0;
					commonH = (int)commonality;

					double variability = 100.0 - commonality;				//변동성
					variability = Math.round(variability*100.0)/100.0;
					variH = (int)variability;

					
					JLabel first = new JLabel("1st  Service Name : ");
					JLabel second = new JLabel("2nd Service Name : ");
					first.setBounds(5,5,130,20);
					second.setBounds(5,20,130,20);
					
					JLabel firstClassName = new JLabel(" " + firstField.getText());
					JLabel secondClassName = new JLabel(" " + secondField.getText());
					firstClassName.setBounds(125, 5, 110, 20);
					secondClassName.setBounds(125, 20, 110, 20);
					
					JPanel panel = new JPanel();					
					panel.setLayout(null);

					JLabel result = new JLabel("Semantic Mapping Result", SwingConstants.CENTER);
					result.setBounds(5,45,200,20);
					JLabel commonalityLb = new JLabel("Commonality", SwingConstants.LEFT);	// + 
					JLabel variabilityLb = new JLabel("Variability", SwingConstants.LEFT);	// + variability + "%"
					commonalityLb.setBounds(5,60,90,20);
					variabilityLb.setBounds(5,75,90,20);
					
					JLabel commonVal = new JLabel(commonality +"%", SwingConstants.CENTER);
					JLabel variVal = new JLabel(variability +"%", SwingConstants.CENTER);
					commonVal.setBounds(100,60,100,20);
					variVal.setBounds(100,75,100,20);
					
					JLabel label = new JLabel();
					
					//75%이상이면 유사함
					if(commonality >= 75)
						label.setText("Common Feature");
					else
						label.setText("Dissimilar Feature");
					label.setBounds(100,90,300,20);
					
					JLabel resLb = new JLabel("Result", SwingConstants.LEFT);
					resLb.setBounds(5,90,100,20);
					
					//취소 버튼
					JButton canBtn = new JButton("Cancel");
					canBtn.addActionListener(			
						new ActionListener(){
							public void actionPerformed(ActionEvent e){
								mappingBtnCheck=false;
								frameClean();
							}
						}
					);
								
					JPanel btnPanel = new JPanel();
					btnPanel.setLayout(new FlowLayout());
					btnPanel.setOpaque(true);
					btnPanel.setBackground(Color.lightGray);
					btnPanel.add(canBtn);

					panel.add(first);
					panel.add(firstClassName);
					panel.add(second);
					panel.add(secondClassName);
					panel.add(result);
					panel.add(commonalityLb);
					panel.add(commonVal);
					panel.add(variabilityLb);
					panel.add(variVal);
					panel.add(resLb);
					panel.add(label);				

					add(classPanel, BorderLayout.NORTH);					
					add(panel, BorderLayout.CENTER);
					add(btnPanel, BorderLayout.SOUTH);					

					repaint();
					setVisible(true);
				}
			}
		);
		
		//첫번째 데이터 검색버튼(Open)
		searchBtn1.addActionListener(		
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try	{
						frameClean();
						add(classPanel, BorderLayout.NORTH);
		
         				tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);        	
         				JTable resultTable = new JTable(tableModel);

         				resultTable.addMouseListener(	
							new MouseAdapter(){
								public void mouseClicked(MouseEvent e){							
									JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
									int selRow = table.getSelectedRow();

									String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값			
									frameClean();

									add(classPanel, BorderLayout.NORTH);
									add(mappingPanel, BorderLayout.CENTER);
									firstField.setText(selStr);
								}
							}
						);
			
						Box boxNorth = Box.createHorizontalBox();
						         	
         				JLabel filterLabel = new JLabel( "Filter:" );			//필터
         				final JTextField filterText = new JTextField();
         				JButton filterButton = new JButton( "Apply Filter" );
         				
         				//취소 버튼
    					JButton cancelBtn = new JButton("Cancel");
    					
         				Box boxSouth = Box.createHorizontalBox();
         
         				boxSouth.add(filterLabel);
         				boxSouth.add(filterText);
         				boxSouth.add(filterButton);
         				boxSouth.add(cancelBtn);
         				
                     	JPanel panel = new JPanel();
         				panel.setLayout(new BorderLayout());
         				panel.add(boxNorth, BorderLayout.NORTH);
         				panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
         				panel.add(boxSouth, BorderLayout.SOUTH); 
         				add(panel, BorderLayout.CENTER);        	
         
         				final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
         				resultTable.setRowSorter(sorter);
         				setSize(620,500); 
         				setVisible(true);   
         				
         				cancelBtn.addActionListener(			
        						new ActionListener(){
        							public void actionPerformed(ActionEvent e){
        								frameClean();
        								displaySimilarityCheck();
        							}
        						}
        					);
         				
                        filterButton.addActionListener(            
            					new ActionListener() {
               						public void actionPerformed( ActionEvent e ) {
                  						String text = filterText.getText();
                  						
                  						if ( text.length() == 0 )
                     							sorter.setRowFilter( null );
                  						else{
                    	 						try	{
                        							sorter.setRowFilter(RowFilter.regexFilter(text));
                     							}
                     							catch(PatternSyntaxException pse) {
                        							JOptionPane.showMessageDialog(null, "Bad regex pattern", 
                        											"Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                     							}
                  						}
               						}
								}
         					);
     					}
      					catch (SQLException sqlException) {
         					JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
           
         					tableModel.disconnectFromDatabase();         
         					System.exit(1);
      					}      
      
      					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
      					addWindowListener(      
         					new WindowAdapter() {
                					public void windowClosed( WindowEvent event ){
               							tableModel.disconnectFromDatabase();
               							System.exit(0);
            						}
         					}
      					); 
				}
			}
		);
		
		//두번째 데이터 검색버튼(Open)
		searchBtn2.addActionListener(		
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try	{
						frameClean();
						add(classPanel, BorderLayout.NORTH);

         				tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);        	
         				JTable resultTable = new JTable(tableModel);

						resultTable.addMouseListener(	
							new MouseAdapter(){
								public void mouseClicked(MouseEvent e){		
									JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
									int selRow = table.getSelectedRow();

									String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값					
									frameClean();

									add(classPanel, BorderLayout.NORTH);
									add(mappingPanel, BorderLayout.CENTER);
									secondField.setText(selStr);									
								}
							}
						);
			
						Box boxNorth = Box.createHorizontalBox();
						         	
         				JLabel filterLabel = new JLabel( "Filter:" );			//필터
         				final JTextField filterText = new JTextField();
         				JButton filterButton = new JButton( "Apply Filter" );
         				//취소 버튼
    					JButton cancelBtn = new JButton("Cancel");
    					
         				Box boxSouth = Box.createHorizontalBox();
         
         				boxSouth.add(filterLabel);
         				boxSouth.add(filterText);
         				boxSouth.add(filterButton);
         				boxSouth.add(cancelBtn);
                  
						JPanel panel = new JPanel();
						panel.setLayout(new BorderLayout());
         				panel.add(boxNorth, BorderLayout.NORTH);
         				panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
         				panel.add(boxSouth, BorderLayout.SOUTH); 
						add(panel, BorderLayout.CENTER);        	
         
         				final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
         				resultTable.setRowSorter(sorter);
         				setSize(620,500); 
         				setVisible(true);   
                  
         				cancelBtn.addActionListener(			
        						new ActionListener(){
        							public void actionPerformed(ActionEvent e){
        								frameClean();
        								displaySimilarityCheck();
        							}
        						}
        					);
         				
         				filterButton.addActionListener(            
							new ActionListener() {
               						public void actionPerformed( ActionEvent e ) {
                  						String text = filterText.getText();

                  						if ( text.length() == 0 )
                     							sorter.setRowFilter( null );
                  						else{
                  							try{
                        						sorter.setRowFilter(RowFilter.regexFilter(text));
                     						}
                     						catch(PatternSyntaxException pse){
                        						JOptionPane.showMessageDialog(null, "Bad regex pattern", 
                        								"Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                     						}
                  						}
               						}
            				}
         				);
					}
      				catch (SQLException sqlException){
         				JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
           
         				tableModel.disconnectFromDatabase();         
         				System.exit(1);
      				}      
      
      				setDefaultCloseOperation(DISPOSE_ON_CLOSE);      
      				addWindowListener(      
         				new WindowAdapter() {
                				public void windowClosed(WindowEvent event){
               						tableModel.disconnectFromDatabase();
               						System.exit(0);
            					}
         				}
      				); 
				}
			}
		);
		setVisible(true);				
	}

	//서비스 등록(register) 함수/////////////////////////////////////
	private void showRegisterForm(ActionEvent event)
	{
		frameClean();
		//전체적인 레이아웃
		setLayout(new FlowLayout(FlowLayout.LEFT)); 	

		//서비스 이름
		sName = new JLabel("Service Name  :  ");
		sName.setToolTipText("A name of a service");
		sName.setOpaque(true);
		sName.setBackground(Color.lightGray);
		sText = new JTextField(8);
		
		
		
		//상위 도메인 이름
		uDomain = new JLabel("Upper Domain Name  :  ");
		uDomainText = new JTextField(8);
		uDomain.setToolTipText("A name of upper domain and task which a service is included");
		uDomain.setOpaque(true);
		uDomain.setBackground(Color.lightGray);
		

		//Task
		task = new JLabel("Task : ", SwingConstants.LEFT);
		taskText = new JTextField(8);
		task.setToolTipText("Task");
		task.setOpaque(true);
		task.setBackground(Color.lightGray);

		//유사 서비스 이름
		snonySName = new JLabel("Synonymous Service Names : ");
		snonyText = new JTextField(8);
		snonySName.setToolTipText("Names of service, i.e., domain, application area, and granularity in Fig. 2");
		snonySName.setOpaque(true);
		snonySName.setBackground(Color.lightGray);

		//우선순위
		priority = new JLabel("Priority : ");
		priority.setToolTipText("The level of preference\n(1.0 : very important, 0.5 : important, 0 : not important)");
		priority.setOpaque(true);
		priority.setBackground(Color.lightGray);

		JRadioButton ten = new JRadioButton("10");
		JRadioButton nine = new JRadioButton("9");
		JRadioButton eight = new JRadioButton("8");
		JRadioButton seven = new JRadioButton("7");
		JRadioButton six = new JRadioButton("6");
		JRadioButton five = new JRadioButton("5");
		JRadioButton four = new JRadioButton("4");
		JRadioButton three = new JRadioButton("3");
		JRadioButton two = new JRadioButton("2");
		JRadioButton one = new JRadioButton("1");
		
		priorRadioGroup = new ButtonGroup();
		priorRadioGroup.add(ten);
		priorRadioGroup.add(nine);
		priorRadioGroup.add(eight);
		priorRadioGroup.add(seven);
		priorRadioGroup.add(six);
		priorRadioGroup.add(five);
		priorRadioGroup.add(four);
		priorRadioGroup.add(three);
		priorRadioGroup.add(two);
		priorRadioGroup.add(one);

		//인터페이스 이름
		interfaceName = new JLabel("Interface Name  :  ");
		interfaceName.setToolTipText("A name of interface for a service use");
		interfaceText = new JTextField(8);
		interfaceName.setOpaque(true);
		interfaceName.setBackground(Color.lightGray);
		
		//컴포넌트
		component = new JLabel("Component  :  ");
		component.setToolTipText("The inner components being required in service execution");
		component.setOpaque(true);
		component.setBackground(Color.lightGray);
		componentText = new JTextField(8);	
		
		//정의
		def = new JLabel("Definition   :   ");
		def.setToolTipText("Explaining semantics, function, and status of function for service using natural language");
		def.setOpaque(true);
		def.setBackground(Color.lightGray);
		defArea = new JTextArea(0, 10);
		defArea.setLineWrap(true); 	//자동 줄바꿈
		defArea.setWrapStyleWord(true); //단어 단위 줄바꿈
		scroll = new JScrollPane(defArea, 
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
				  );		

		//서비스 속성
		serviceAtr = new JLabel("Service Attribute  :  ");
		serviceAtr.setOpaque(true);
		serviceAtr.setBackground(Color.lightGray);
		serviceAtr.setToolTipText("Selective attributes");
		mandatory = new JRadioButton("Mandatory", true);
		mandatory.setToolTipText("A service which must exist between sibling services in the Domain");
		optional = new JRadioButton("Optional", false);
		optional.setToolTipText("A service which can be selected between sibling services in the Domain");
		alternative = new JRadioButton("Alternative", false);
		alternative.setToolTipText("A service which must exist alone between sibling services in the Domain");
		serviceAtrRadioGroup = new ButtonGroup();
		serviceAtrRadioGroup.add(mandatory);
		serviceAtrRadioGroup.add(optional);
		serviceAtrRadioGroup.add(alternative);

		//서비스 아이디
		identifier = new JLabel("Service ID  :  ");
		identifier.setToolTipText("The unique ID of a service to distinguish a service between services in the Domain");
		identifier.setOpaque(true);
		identifier.setBackground(Color.lightGray);
		idText = new JTextField(8);	
		
		//등록기관
		regAuthority = new JLabel("Registration Authority  :  ");
		regAuthority.setToolTipText("The name of a company, organization, or group which registers a service");
		regAuthority.setOpaque(true);
		regAuthority.setBackground(Color.lightGray);
		regText = new JTextField(10);
		
		//등록, 초기화, 취소, 모든데이터 보기버튼
		regBtn = new SmallButton("Register");
		initialBtn = new SmallButton("Initialize");
		cancelBtn = new SmallButton("Cancel");
		//allShowBtn = new SmallButton("All Datas Show");
		
		
		/////////////////////////////////////////
		//해당 서비스가 속해있는 회사
		JLabel comLb = new JLabel("       	  Company :");
		comLb.setToolTipText("Company");
		comLb.setOpaque(true);
		comLb.setBackground(Color.lightGray);
		comText = new JTextField(8);

		//등록버튼
		regBtn.addActionListener(		
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					String comStr = comText.getText();
					
					//Company란 비어 있지 않으면
					if(comStr.length() > 0){
						//등록 완료 메시지 출력
						JOptionPane.showMessageDialog(null, "Register Complete!", "Complete", 
								JOptionPane.PLAIN_MESSAGE);

						serviceQuery.addService(sText.getText(), uDomainText.getText(), taskText.getText(), 
								snonyText.getText(), interfaceText.getText(), componentText.getText(), 
								idText.getText(), regText.getText(), defArea.getText()/*, comStr*/);
						//등록 완료 메시지 출력 후 초기화
						sText.setText("");
						uDomainText.setText("");
						taskText.setText("");
						snonyText.setText("");
						interfaceText.setText("");
						componentText.setText("");
						idText.setText("");
						regText.setText("");
						defArea.setText("");
						queryTextField.setText("");
						//comText.setText("");
					}
					
					//Company란 비어 있다면
					else{
						JOptionPane.showMessageDialog(null, "Company Input Value Empty!", "Warning", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		);
		//초기화버튼
		initialBtn.addActionListener(	
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sText.setText("");
					uDomainText.setText("");
					taskText.setText("");
					snonyText.setText("");
					interfaceText.setText("");
					componentText.setText("");
					idText.setText("");
					regText.setText("");
					defArea.setText("");
					queryTextField.setText("");										
				}
			}
		);
		//등록 취소버튼
		cancelBtn.addActionListener(		
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					frameClean();
				}
			}
		);
		//모든데이타 보기
		/*
		allShowBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					frameClean();
					displaySearchResults(); 
				}
			}
		);		
		*/
		
		//Identifying 속성 영역
		iBorder = BorderFactory.createEtchedBorder();
		iBorder = BorderFactory.createTitledBorder(iBorder, "Identifying attributes");
		iBorderPanel = new JPanel();
		
		iBorderPanel.setLayout(new GridLayout(7,3));
				
		iBorderPanel.add(sName);
		iBorderPanel.add(sText);
		iBorderPanel.add(new JLabel(" "));				
		
		iBorderPanel.add(uDomain);
		iBorderPanel.add(uDomainText);
		iBorderPanel.add(new JLabel(" "));
		iBorderPanel.setBorder(iBorder);
		iBorderPanel.add(task);
		iBorderPanel.add(taskText);
		iBorderPanel.add(new JLabel(" "));
		iBorderPanel.add(snonySName);
		iBorderPanel.add(snonyText);
		iBorderPanel.add(new JLabel(" "));

		iBorderPanel.add(interfaceName);
		iBorderPanel.add(interfaceText);
		iBorderPanel.add(new JLabel(" "));

		iBorderPanel.add(component);
		iBorderPanel.add(componentText);
		iBorderPanel.add(new JLabel(" "));

		iBorderPanel.add(def);
		iBorderPanel.add(scroll);
		iBorderPanel.add(new JLabel(" "));
				
		//Selective 속성 영역
		sBorder = BorderFactory.createEtchedBorder();
		sBorder = BorderFactory.createTitledBorder(sBorder, "Selective attributes");
		sBorderPanel = new JPanel();
		
		sBorderPanel.setLayout(new GridLayout(1,5));
		sBorderPanel.setBorder(sBorder);		
		sBorderPanel.add(serviceAtr);
		sBorderPanel.add(mandatory);
		sBorderPanel.add(optional);
		sBorderPanel.add(alternative);
		sBorderPanel.add(new JLabel("  "));
		
		//Managing 속성 영역	
		mBorder = BorderFactory.createEtchedBorder();
		mBorder = BorderFactory.createTitledBorder(mBorder, "Managing attributes");
		mBorderPanel = new JPanel();
		
		mBorderPanel.setLayout(new GridLayout(1,2));
		mBorderPanel.setBorder(mBorder);
		mBorderPanel.add(identifier);
		mBorderPanel.add(idText);
		mBorderPanel.add(regAuthority);
		mBorderPanel.add(regText);

		//Priority(1~10)
		JPanel pn = new JPanel();		
		pn.add(priority);
		pn.add(ten);
		pn.add(nine);
		pn.add(eight);
		pn.add(seven);
		pn.add(six);
		pn.add(five);
		pn.add(four);
		pn.add(three);
		pn.add(two);
		pn.add(one);
		
		//등록, 초기화, 등록취소, 모든 데이터 삭제 버튼 영역
		btnPanel = new JPanel();		
		btnPanel.setLayout(new FlowLayout());
		
		
		
		btnPanel.add(regBtn);
		btnPanel.add(initialBtn);
		btnPanel.add(cancelBtn);
		btnPanel.add(comLb);
		btnPanel.add(comText);
		
		/////////////////////////
		/*
		JLabel lb;
		for(int i=0; i<23; i++)
		{
			lb = new JLabel(" ");	//공백 레이블 생성
			btnPanel.add(lb);
		}
		*/
		////////////////////////
		//btnPanel.add(allShowBtn);
		
		//서비스 검색하는 필드 영역
		queryLabel = new JLabel("Service Name : ");
		queryLabel.setOpaque(true);
		queryLabel.setBackground(Color.lightGray);

		queryTextField = new JTextField(20);
		queryButton = new JButton("Search");
		queryPanel = new JPanel();
		queryPanel.setLayout(new FlowLayout());		
		queryPanel.setBorder(BorderFactory.createTitledBorder("Find a data by service name"));
		queryPanel.add(Box.createHorizontalStrut(5));
		queryPanel.add(queryLabel);
		queryPanel.add(Box.createHorizontalStrut(10));
		queryPanel.add(queryTextField);	
		queryPanel.add(Box.createHorizontalStrut(10));
		queryPanel.add(queryButton);
		queryPanel.add(new JLabel("  		 "));
		queryPanel.add(new JLabel("   		 "));
		queryPanel.add(new JLabel("  		 "));
		queryPanel.add(new JLabel("  		 "));	
		queryPanel.add(new JLabel("  		 "));
		queryPanel.add(new JLabel("  		"));
		queryPanel.add(new JLabel("  		"));
		queryPanel.add(Box.createHorizontalStrut(5));	
		
		//Search 버튼 액션리스너	
		//Search 버튼 눌리면 해당하는 서비스 이름에 해당하는 DB내용이 출력된다.						
		queryButton.addActionListener(			
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					String str = queryTextField.getText();	
					if(str.length() <= 0){
						JOptionPane.showMessageDialog(null, "입력된 값이 없습니다!", "Search", 
								JOptionPane.PLAIN_MESSAGE);						
					}
					else{
						frameClean();
						displayBySearchBtn(str);
					}
				}
			}
		);			
		//패널 추가		
		add(iBorderPanel);
		add(pn);
		add(sBorderPanel);
		add(mBorderPanel);						
		add(queryPanel);
		add(btnPanel);					
		setVisible(true);					
	}
	//등록화면에 있는 Search버튼 눌리면 출력되는 창
	private void displayBySearchBtn(final String str)
	{
		frameClean();
		setLayout(new BorderLayout());		        
 		try	{
     		tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);            	
     	
     		JTable resultTable = new JTable(tableModel);

     		resultTable.addMouseListener(
     				new MouseAdapter(){
     					public void mouseClicked(MouseEvent e){							
     						JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
     						int selRow = table.getSelectedRow();

     						String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값					
     						displaySelRow(selStr);									
     					}
     				}
     		);
		
		Box boxNorth = Box.createHorizontalBox();
		JLabel titleLb = new JLabel("Search Result");			
		boxNorth.add(titleLb);
     
     	JLabel filterLabel = new JLabel( "Filter:" );			//필터
     	final JTextField filterText = new JTextField();
     	JButton filterButton = new JButton( "Apply Filter" );
     	JButton cancelBtn = new JButton("Cancel");
     	Box boxSouth = Box.createHorizontalBox();
     
     	boxSouth.add(filterLabel);
     	boxSouth.add(filterText);
     	boxSouth.add(filterButton);
     	boxSouth.add(cancelBtn);
              
     	add(boxNorth, BorderLayout.NORTH);
     	add(new JScrollPane(resultTable), BorderLayout.CENTER);
     	add(boxSouth, BorderLayout.SOUTH);         	
     
     	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
     	resultTable.setRowSorter(sorter);
     	setSize(620,500); 
     	setVisible(true);   
     	
     	//취소버튼
     	cancelBtn.addActionListener(
     			new ActionListener(){
     				public void actionPerformed(ActionEvent e){
     					frameClean();
     				}
     			}
 		);
              
     	filterButton.addActionListener(       
     			new ActionListener() {
           			public void actionPerformed( ActionEvent e ) {
              			String text = filterText.getText();

              			if ( text.length() == 0 )
                 				sorter.setRowFilter( null );
              			else{
                	 			try	{
                    				sorter.setRowFilter(RowFilter.regexFilter(text));
                 				}
                 				catch(PatternSyntaxException pse) {
                    				JOptionPane.showMessageDialog(null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                 				}
              			}
           			}
        		}
     		);
 		}
  		catch (SQLException sqlException) {
     		JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
       
     		tableModel.disconnectFromDatabase();         
     		System.exit(1);
  		}      
  
  		setDefaultCloseOperation(DISPOSE_ON_CLOSE);  
  		addWindowListener(  
     		new WindowAdapter() {
            		public void windowClosed( WindowEvent event ){
           				tableModel.disconnectFromDatabase();
           				System.exit(0);
        			}
     		}
  		);
	}	
	
	//삭제를 위한 상세내용 출력
	private void displaySelRowtoDelete(final String selStr)	{
		frameClean();		
		results = serviceQuery.getServiceBySName(selStr);
		numberOfData = results.size();

		if(numberOfData != 0){
			currentDataIdx = 0;
			currentData = results.get(currentDataIdx);

			setLayout(new BorderLayout());
			
			JPanel panel = new JPanel();
			panel.setLayout(null);

			JPanel btnPanel = new JPanel();
			
			//////////////////////////////////////////////////////////////////////
			SmallButton sbBack = new SmallButton("Back");				//Back버튼
			sbBack.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){	
						frameClean();
						displayDeleteResults();
					}
				}
			);
			/////////////////////////////////////////////////////////////////////
			SmallButton sbCancel = new SmallButton("Cancel");			//Cancel버튼
			sbCancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						frameClean();
					}
				}
			);
			/////////////////////////////////////////////////////////////////////
			SmallButton sbDelete = new SmallButton("Delete");			//Delete버튼
			sbDelete.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						serviceQuery.deleteServiceOne(selStr);
						JOptionPane.showMessageDialog(null, "Delete Complete!", "Complete", 
									JOptionPane.PLAIN_MESSAGE);
						frameClean();
						displayDeleteResults();
					}
				}
			);
			SmallButton allDelete = new SmallButton("All Delete");
			allDelete.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
							serviceQuery.deleteServiceAll();
							JOptionPane.showMessageDialog(null, "Delete All Complete!", "Complete", 
										JOptionPane.PLAIN_MESSAGE);							
							frameClean();
							displayDeleteResults();
						}
					}
			);
			btnPanel.setLayout(new FlowLayout());
			btnPanel.setOpaque(true);
			btnPanel.setBackground(Color.lightGray);
			btnPanel.add(sbBack);
			btnPanel.add(sbDelete);
			btnPanel.add(allDelete);
			btnPanel.add(sbCancel);
						
			JLabel sname = new JLabel("Service name : ");
			sname.setOpaque(true);
			sname.setBackground(Color.lightGray);
			JTextField sNameText = new JTextField(50);
			sNameText.setText(currentData.getSName());
			sNameText.disable();	//이유..
			sNameText.setBounds(190,10,400,20);
			sname.setBounds(5,10,180,20);			
			panel.add(sname);
			panel.add(sNameText);			

			JLabel udn = new JLabel("Upper Domain Name : ");
			udn.setOpaque(true);
			udn.setBackground(Color.lightGray);
			JTextField udnText = new JTextField(50);
			udnText.setText(currentData.getUDName());
			udnText.disable();
			udnText.setBounds(190,30,400,20);
			udn.setBounds(5,30,180,20);
			panel.add(udn);
			panel.add(udnText);			

			JLabel task = new JLabel("Service Task : ");
			task.setOpaque(true);
			task.setBackground(Color.lightGray);
			JTextField taskText = new JTextField(50);
			taskText.setText(currentData.getTask());
			taskText.disable();
			taskText.setBounds(190,50,400,20);
			task.setBounds(5,50,180,20);
			panel.add(task);
			panel.add(taskText);
			
			JLabel ssname = new JLabel("Synonymous Service Names : ");
			ssname.setOpaque(true);
			ssname.setBackground(Color.lightGray);
			JTextField ssNameText = new JTextField(50);
			ssNameText.setText(currentData.getSSName());
			ssNameText.disable();			
			ssNameText.setBounds(190,70,400,20);
			ssname.setBounds(5,70,180,20);
			panel.add(ssname);
			panel.add(ssNameText);
			
			JLabel interfaceLb = new JLabel("Interface Names : ");
			interfaceLb.setOpaque(true);
			interfaceLb.setBackground(Color.lightGray);
			JTextField interfaceText = new JTextField(50);
			interfaceText.setText(currentData.getInterfaceName());
			interfaceText.disable();
			interfaceText.setBounds(190,90,400,20);
			interfaceLb.setBounds(5,90,180,20);
			panel.add(interfaceLb); 
			panel.add(interfaceText);
			
			JLabel com = new JLabel("Component Name : ");
			com.setOpaque(true);
			com.setBackground(Color.lightGray);
			JTextField Component = new JTextField(50);
			Component.setText(currentData.getComponent());
			Component.disable();
			Component.setBounds(190,110,400,20);
			com.setBounds(5,110,180,20);
			panel.add(com);
			panel.add(Component);			
			
			JLabel sid = new JLabel("Service ID : ");
			sid.setOpaque(true);
			sid.setBackground(Color.lightGray);
			JTextField sID = new JTextField(50);
			sID.setText(currentData.getServiceID());
			sID.disable();
			sID.setBounds(190,130,400,20);
			sid.setBounds(5,130,180,20);
			panel.add(sid);
			panel.add(sID);			
			
			JLabel r = new JLabel("Registration Authority : ");
			r.setOpaque(true);
			r.setBackground(Color.lightGray);
			JTextField reg = new JTextField(50);
			reg.setText(currentData.getRegAuthority());
			reg.disable();
			reg.setBounds(190,150,400,20);
			r.setBounds(5,150,180,20);
			panel.add(r);
			panel.add(reg);			
			
			JLabel d = new JLabel("Definition : ");
			d.setOpaque(true);
			d.setBackground(Color.lightGray);
			JTextArea def = new JTextArea(150,400);			
			def.setText(currentData.getDefinition());
			def.setLineWrap(true);
			def.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(def,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
			def.disable();
			def.setBounds(190,170,400,150);
			scroll.setBounds(190,170,400,150);
			d.setBounds(5,230,180,20);
			panel.add(d);
			panel.add(scroll);
			
			add(panel, BorderLayout.CENTER);
			add(btnPanel, BorderLayout.SOUTH);			
						
			setVisible(true);
		}
	}
	//수정을 위한 상세내용 출력
	private void displaySelRowtoUpdate(final String selStr)
	{
		frameClean();
		
		final JTextField sNameText = new JTextField(50);
		final JTextField udnText = new JTextField(50);
		final JTextField taskText = new JTextField(50);
		final JTextField ssNameText = new JTextField(50);
		final JTextField interfaceText = new JTextField(50);
		final JTextField Component = new JTextField(50);
		final JTextField sID = new JTextField(50);
		final JTextField reg = new JTextField(50);
		final JTextArea def = new JTextArea(150,400);
		
		results = serviceQuery.getServiceBySName(selStr);
		numberOfData = results.size();

		if(numberOfData != 0){
			currentDataIdx = 0;
			currentData = results.get(currentDataIdx);

			setLayout(new BorderLayout());
			
			JPanel panel = new JPanel();
			panel.setLayout(null);	//new GridLayout(0,3)

			JPanel btnPanel = new JPanel();
			
			///////////////////////////////////////////////////////////////////////////
			SmallButton sbBack = new SmallButton("Back");				//Back버튼
			sbBack.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){	
						frameClean();
						displayUpdateResults();
					}
				}
			);
			
			///////////////////////////////////////////////////////////////////////////
			SmallButton sbCancel = new SmallButton("Cancel");			//Cancel버튼
			sbCancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						frameClean();
					}
				}
			);

			///////////////////////////////////////////////////////////////////////////
			SmallButton sbUpdate = new SmallButton("Update");			//Update 버튼
			sbUpdate.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						serviceQuery.updateServiceOne(selStr, sNameText.getText(), udnText.getText(), taskText.getText(), ssNameText.getText(),
								interfaceText.getText(), Component.getText(), def.getText(), sID.getText(), reg.getText());						
						frameClean();
						displayUpdateResults();						
					}
				}
			);			
						
			btnPanel.setLayout(new FlowLayout());
			btnPanel.setOpaque(true);
			btnPanel.setBackground(Color.lightGray);
			btnPanel.add(sbBack);
			btnPanel.add(sbUpdate);
			btnPanel.add(sbCancel);
			
			JLabel sname = new JLabel("Service name : ");
			sname.setOpaque(true);
			sname.setBackground(Color.lightGray);			
			sNameText.setText(currentData.getSName());
			sNameText.setBounds(190,10,400,20);
			sname.setBounds(5,10,180,20);			
			panel.add(sname);
			panel.add(sNameText);
			

			JLabel udn = new JLabel("Upper Domain Name : ");
			udn.setOpaque(true);
			udn.setBackground(Color.lightGray);			
			udnText.setText(currentData.getUDName());
			udnText.setBounds(190,30,400,20);
			udn.setBounds(5,30,180,20);
			panel.add(udn);
			panel.add(udnText);
			

			JLabel task = new JLabel("Service Task : ");
			task.setOpaque(true);
			task.setBackground(Color.lightGray);			
			taskText.setText(currentData.getTask());
			taskText.setBounds(190,50,400,20);
			task.setBounds(5,50,180,20);
			panel.add(task);
			panel.add(taskText);
			

			JLabel ssname = new JLabel("Synonymous Service Names : ");
			ssname.setOpaque(true);
			ssname.setBackground(Color.lightGray);			
			ssNameText.setText(currentData.getSSName());			
			ssNameText.setBounds(190,70,400,20);
			ssname.setBounds(5,70,180,20);
			panel.add(ssname); 
			panel.add(ssNameText);
			
			JLabel interfaceLb = new JLabel("Interface Names : ");
			interfaceLb.setOpaque(true);
			interfaceLb.setBackground(Color.lightGray);			
			interfaceText.setText(currentData.getInterfaceName());
			interfaceText.setBounds(190,90,400,20);
			interfaceLb.setBounds(5,90,180,20);
			panel.add(interfaceLb); 
			panel.add(interfaceText);
			
			JLabel com = new JLabel("Component Name : ");
			com.setOpaque(true);
			com.setBackground(Color.lightGray);			
			Component.setText(currentData.getComponent());
			Component.setBounds(190,110,400,20);
			com.setBounds(5,110,180,20);
			panel.add(com);
			panel.add(Component);			
			
			JLabel sid = new JLabel("Service ID : ");
			sid.setOpaque(true);
			sid.setBackground(Color.lightGray);			
			sID.setText(currentData.getServiceID());
			sID.setBounds(190,130,400,20);
			sid.setBounds(5,130,180,20);
			panel.add(sid);
			panel.add(sID);			
			
			JLabel r = new JLabel("Registration Authority : ");
			r.setOpaque(true);
			r.setBackground(Color.lightGray);			
			reg.setText(currentData.getRegAuthority());
			reg.setBounds(190,150,400,20);
			r.setBounds(5,150,180,20);
			panel.add(r);
			panel.add(reg);			
			
			JLabel d = new JLabel("Definition : ");
			d.setOpaque(true);
			d.setBackground(Color.lightGray);						
			def.setText(currentData.getDefinition());
			def.setLineWrap(true);
			def.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(def,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			def.setBounds(190,170,400,150);
			scroll.setBounds(190,170,400,150);
			d.setBounds(5,230,180,20);
			panel.add(d);
			panel.add(scroll);
			
			add(panel, BorderLayout.CENTER);
			add(btnPanel, BorderLayout.SOUTH);			
						
			setVisible(true);
		}
	}

	//검색을 위한(선택 행) 상세내용 출력
	private void displaySelRow(String selStr)
	{		
		frameClean();
		results = serviceQuery.getServiceBySName(selStr);
		numberOfData = results.size();

		if(numberOfData != 0){
			currentDataIdx = 0;
			currentData = results.get(currentDataIdx);

			setLayout(new BorderLayout());
			
			JPanel panel = new JPanel();
			panel.setLayout(null);	//new GridLayout(0,3)			

			JPanel btnPanel = new JPanel();
			SmallButton sbBack = new SmallButton("Back");				//Back버튼
			sbBack.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){	
						frameClean();
						displaySearchResults();
					}
				}
			);
			
			SmallButton sbCancel = new SmallButton("Cancel");			//Cancel버튼
			sbCancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						frameClean();
					}
				}
			);
						
			btnPanel.setLayout(new FlowLayout());
			btnPanel.setOpaque(true);
			btnPanel.setBackground(Color.lightGray);
			btnPanel.add(sbBack);
			btnPanel.add(sbCancel);			
			
			JLabel sname = new JLabel("Service name : ");
			sname.setOpaque(true);
			sname.setBackground(Color.lightGray);
			JTextField sNameText = new JTextField(50);
			sNameText.setText(currentData.getSName());
			sNameText.disable();
			sNameText.setBounds(190,10,400,20);
			sname.setBounds(5,10,180,20);			
			panel.add(sname);
			panel.add(sNameText);			

			JLabel udn = new JLabel("Upper Domain Name : ");
			udn.setOpaque(true);
			udn.setBackground(Color.lightGray);
			JTextField udnText = new JTextField(50);
			udnText.setText(currentData.getUDName());
			udnText.disable();
			udnText.setBounds(190,30,400,20);
			udn.setBounds(5,30,180,20);
			panel.add(udn);
			panel.add(udnText);			

			JLabel task = new JLabel("Service Task : ");
			task.setOpaque(true);
			task.setBackground(Color.lightGray);
			JTextField taskText = new JTextField(50);
			taskText.setText(currentData.getTask());
			taskText.disable();
			taskText.setBounds(190,50,400,20);
			task.setBounds(5,50,180,20);
			panel.add(task);
			panel.add(taskText);			

			JLabel ssname = new JLabel("Synonymous Service Names : ");
			ssname.setOpaque(true);
			ssname.setBackground(Color.lightGray);
			JTextField ssNameText = new JTextField(50);
			ssNameText.setText(currentData.getSSName());
			ssNameText.disable();			
			ssNameText.setBounds(190,70,400,20);
			ssname.setBounds(5,70,180,20);
			panel.add(ssname);
			panel.add(ssNameText);
			
			JLabel interfaceLb = new JLabel("Interface Names : ");
			interfaceLb.setOpaque(true);
			interfaceLb.setBackground(Color.lightGray);
			JTextField interfaceText = new JTextField(50);
			interfaceText.setText(currentData.getInterfaceName());
			interfaceText.disable();
			interfaceText.setBounds(190,90,400,20);
			interfaceLb.setBounds(5,90,180,20);
			panel.add(interfaceLb); 
			panel.add(interfaceText);
			
			JLabel com = new JLabel("Component Name : ");
			com.setOpaque(true);
			com.setBackground(Color.lightGray);
			JTextField Component = new JTextField(50);
			Component.setText(currentData.getComponent());
			Component.disable();
			Component.setBounds(190,110,400,20);
			com.setBounds(5,110,180,20);
			panel.add(com);
			panel.add(Component);			
			
			JLabel sid = new JLabel("Service ID : ");
			sid.setOpaque(true);
			sid.setBackground(Color.lightGray);
			JTextField sID = new JTextField(50);
			sID.setText(currentData.getServiceID());
			sID.disable();
			sID.setBounds(190,130,400,20);
			sid.setBounds(5,130,180,20);
			panel.add(sid);
			panel.add(sID);			
			
			JLabel r = new JLabel("Registration Authority : ");
			r.setOpaque(true);
			r.setBackground(Color.lightGray);
			JTextField reg = new JTextField(50);
			reg.setText(currentData.getRegAuthority());
			reg.disable();
			reg.setBounds(190,150,400,20);
			r.setBounds(5,150,180,20);
			panel.add(r);
			panel.add(reg);			
			
			JLabel d = new JLabel("Definition : ");
			d.setOpaque(true);
			d.setBackground(Color.lightGray);
			JTextArea def = new JTextArea(150,400);			
			def.setText(currentData.getDefinition());
			def.setLineWrap(true);
			def.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(def,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
			def.disable();
			def.setBounds(190,170,400,150);
			scroll.setBounds(190,170,400,150);
			d.setBounds(5,230,180,20);
			panel.add(d);
			panel.add(scroll);
			
			add(panel, BorderLayout.CENTER);
			add(btnPanel, BorderLayout.SOUTH);			
						
			setVisible(true);
		}
	}		
	   	
	//저장된 데이터 테이블로 표시(Register메뉴 화면의 All Data Show버튼)	
   	public void displaySearchResults(){         
		setLayout(new BorderLayout());		        
     	try{
         	tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);         	
         	JTable resultTable = new JTable(tableModel);

			resultTable.addMouseListener(	
				new MouseAdapter(){
					public void mouseClicked(MouseEvent e){							
						JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
						int selRow = table.getSelectedRow();

						String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값						
						displaySelRow(selStr);									
					}
				}
			);
			
			Box boxNorth = Box.createHorizontalBox();
			JLabel titleLb = new JLabel("Search Result");			
			boxNorth.add(titleLb);
         
         	JLabel filterLabel = new JLabel( "Filter:" );			//필터
         	final JTextField filterText = new JTextField();
         	JButton filterButton = new JButton( "Apply Filter" );
         	Box boxSouth = Box.createHorizontalBox();
         	SmallButton cancelBtn = new SmallButton("Cancel");
         
         	boxSouth.add(filterLabel);
         	boxSouth.add(filterText);
         	boxSouth.add(filterButton);
         	boxSouth.add(cancelBtn);
                  
         	add(boxNorth, BorderLayout.NORTH);
         	add(new JScrollPane(resultTable), BorderLayout.CENTER);
         	add(boxSouth, BorderLayout.SOUTH);         	
         
         	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
         	resultTable.setRowSorter(sorter);
         	setSize(620,500); 
         	setVisible(true);   
         	
         	//취소 버튼
         	cancelBtn.addActionListener(
         			new ActionListener(){
         				public void actionPerformed(ActionEvent e){
         					frameClean();
         				}
         			}
         	);
                  
         	//필터 버튼
         	filterButton.addActionListener(           
				new ActionListener(){
               				public void actionPerformed( ActionEvent e ){
                  				String text = filterText.getText();

                  				if ( text.length() == 0 )
                     					sorter.setRowFilter( null );
                  				else{
                    	 				try	{
                        					sorter.setRowFilter(RowFilter.regexFilter(text));
                     					}
                     					catch(PatternSyntaxException pse){
                        					JOptionPane.showMessageDialog(null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                     					}
                  				}
               				}
            	}
         	);
		}
      	catch (SQLException sqlException) 
      	{
         	JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
           
         	tableModel.disconnectFromDatabase();         
         	System.exit(1);
      	}      
      
      	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
      	addWindowListener(      
         		new WindowAdapter() {
                		public void windowClosed( WindowEvent event ){
               				tableModel.disconnectFromDatabase();
               				System.exit(0);
            			}
         		}
      	);
   	}
   	//수정을 위한 저장된 데이터 테이블로 표시(Update메뉴)
	public void displayUpdateResults() 
   	{         
		setLayout(new BorderLayout());		        
     	try {
         	tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);            	
         
         	JTable resultTable = new JTable(tableModel);

			resultTable.addMouseListener(	
				new MouseAdapter(){
					public void mouseClicked(MouseEvent e){							
						JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
						int selRow = table.getSelectedRow();

						String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값						
						displaySelRowtoUpdate(selStr);									
					}
				}
			);			
			Box boxNorth = Box.createHorizontalBox();
			JLabel titleLb = new JLabel("Search Result");	
			JButton cancelBtn = new JButton("Cancel");
			boxNorth.add(titleLb);		
         
         	JLabel filterLabel = new JLabel( "Filter:" );			//필터
         	final JTextField filterText = new JTextField();
         	JButton filterButton = new JButton( "Apply Filter" );
         	Box boxSouth = Box.createHorizontalBox();
         
         	boxSouth.add(filterLabel);
       		boxSouth.add(filterText);
        	boxSouth.add(filterButton);
        	boxSouth.add(cancelBtn);
                  
         	add(boxNorth, BorderLayout.NORTH);
       		add(new JScrollPane(resultTable), BorderLayout.CENTER);
        	add(boxSouth, BorderLayout.SOUTH);         	
         
         	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
         	resultTable.setRowSorter(sorter);
         	setSize(620,500); 
         	setVisible(true);   
            
         	cancelBtn.addActionListener(
         			new ActionListener(){
         				public void actionPerformed(ActionEvent e){
         					frameClean();
         				}
         			}
        	);
         	
         	filterButton.addActionListener(              
				new ActionListener() {
               				public void actionPerformed( ActionEvent e ) {
                  				String text = filterText.getText();

                  				if ( text.length() == 0 )
                     					sorter.setRowFilter( null );
                  				else{
                    	 				try{
                        					sorter.setRowFilter(RowFilter.regexFilter(text));
                     					}
                     					catch(PatternSyntaxException pse) {
                        					JOptionPane.showMessageDialog(null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                     					}
                  				}
               				}
            	}
         	);
		}
      	catch (SQLException sqlException) 
      	{
         	JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
           
         	tableModel.disconnectFromDatabase();         
         	System.exit(1);
      	}      
      
      	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
      	addWindowListener(      
         		new WindowAdapter() {
                		public void windowClosed( WindowEvent event ){
               				tableModel.disconnectFromDatabase();
               				System.exit(0);
                		}
         		}
      	);
   	}

	//삭제를 위한 저장된 데이터를 테이블로 표시(Delete메뉴)
	public void displayDeleteResults() 
   	{         
		setLayout(new BorderLayout());		        
     	try {
         	tableModel = new SearchResultTable(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);            	
         	JTable resultTable = new JTable(tableModel);

			resultTable.addMouseListener(
					new MouseAdapter(){
					public void mouseClicked(MouseEvent e){							
						JTable table = (JTable)e.getSource();	//이벤트 객체에서 이벤트가 발생한 객체의 주소 복사
						int selRow = table.getSelectedRow();

						String selStr = (String)table.getValueAt(selRow, 0); //선택한 행의 0번째 값
						
						displaySelRowtoDelete(selStr);									
					}
				}
			);
			
			Box boxNorth = Box.createHorizontalBox();
			JLabel titleLb = new JLabel("Search Result");
			boxNorth.add(titleLb);
         
         	JLabel filterLabel = new JLabel( "Filter:" );
         	final JTextField filterText = new JTextField();
         	JButton filterButton = new JButton( "Apply Filter" );
         	JButton cancelBtn = new JButton("Cancel");
         	Box boxSouth = Box.createHorizontalBox();
         
         	boxSouth.add(filterLabel);
         	boxSouth.add(filterText);
         	boxSouth.add(filterButton);
         	boxSouth.add(cancelBtn);
                 
        	add(boxNorth, BorderLayout.NORTH);
         	add(new JScrollPane(resultTable), BorderLayout.CENTER);
         	add(boxSouth, BorderLayout.SOUTH);         	
         
         	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
         	resultTable.setRowSorter(sorter);
         	setSize(620,500); 
         	setVisible(true);   
         	
         	cancelBtn.addActionListener(
         			new ActionListener(){
         				public void actionPerformed(ActionEvent e){
         					frameClean();
         				}
         			}
        	);
                  
         	filterButton.addActionListener(            
				new ActionListener(){
               				public void actionPerformed( ActionEvent e ) {
                  				String text = filterText.getText();

                  				if ( text.length() == 0 )
                     					sorter.setRowFilter( null );
                  				else{
                    	 				try{
                        					sorter.setRowFilter(RowFilter.regexFilter(text));
                     					}
                     					catch(PatternSyntaxException pse){
                        					JOptionPane.showMessageDialog(null, "Bad regex pattern", "Bad regex pattern", JOptionPane.ERROR_MESSAGE);                  
                     					}
                  				}
               				}
            	}
         	);
		}
      	catch (SQLException sqlException){
         	JOptionPane.showMessageDialog(null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
           
         	tableModel.disconnectFromDatabase();         
         	System.exit(1);
      	}      
      
      	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(      
         		new WindowAdapter() {
                		public void windowClosed( WindowEvent event ){
               				tableModel.disconnectFromDatabase();
               				System.exit(0);
            			}
         		}
      	);
   	}
}
//작은 버튼 제작 클래스
class SmallButton extends JButton
{
	public SmallButton(String str)
	{	
		super(str);
	}
	public Dimension getPreferredSize()
	{
		Dimension smallBtn = new Dimension(
				
				super.getPreferredSize().width,
				super.getPreferredSize().height-5
		);
		return smallBtn;
	}
}