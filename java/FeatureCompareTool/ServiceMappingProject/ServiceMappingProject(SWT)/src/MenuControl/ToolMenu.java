package MenuControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import Main.MainFrame;
import Menu_Delete.DeleteScreen;
import Menu_Register.RegisterScreen;
import Menu_Update.UpdateScreen;
import Search.SearchServiceDialog;
import SimilarityCheck.SimilarityCheckScreen;

public class ToolMenu implements SelectionListener{	
	private Menu menuBar = null;
	
	//File 메뉴 변수
	private MenuItem cascadeFileMenu = null;			
	private Menu fileMenu = null;
	private MenuItem openFirstServiceItem = null;
	private MenuItem openSecondServiceItem = null;
	private MenuItem searchServiceItem = null;
	private MenuItem exitItem = null;
	
	//Edit 메뉴 변수
	private MenuItem cascadeEditMenu = null;			
	private Menu editMenu = null;
	private MenuItem registerItem = null;
	private MenuItem updateItem = null;
	private MenuItem deleteItem = null;	
	
	//Tool 메뉴 변수
	private MenuItem cascadeToolMenu = null;			
	private Menu toolMenu = null;
	private MenuItem similarityCheckItem = null;
	
	//Help 메뉴 변수
	private MenuItem cascadeHelpMenu = null;			
	private Menu helpMenu = null;
	private MenuItem informationItem = null;
	
    private SimilarityCheckScreen similarityCheckScreen = null;
	
	//생성자
	public ToolMenu(){
		this.initializeMember();
		this.setFileMenu(menuBar);	
		this.setEditMenu(menuBar);	
		this.setToolMenu(menuBar);	
		this.setHelpMenu(menuBar);
		this.setEventHandler();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){				
		menuBar = new Menu(MainFrame.shell, SWT.BAR);	
		similarityCheckScreen = new SimilarityCheckScreen();
	} //멤버 변수 초기화 함수 끝
	
	//File 메뉴 생성
	public void setFileMenu(Menu menuBar){
		cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");

		fileMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
		openFirstServiceItem = new MenuItem(fileMenu, SWT.PUSH); 
		openFirstServiceItem.setText("Open F&irst Service");
		openSecondServiceItem = new MenuItem(fileMenu, SWT.PUSH);
		openSecondServiceItem.setText("&Open S&econd Service");
		searchServiceItem = new MenuItem(fileMenu, SWT.PUSH);
		searchServiceItem.setText("&Service Search");
		exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
	} //File 메뉴 생성 함수 끝
	
	//Edit 메뉴 생성
	public void setEditMenu(Menu menuBar){
		cascadeEditMenu = new MenuItem(menuBar, SWT.CASCADE); 
		cascadeEditMenu.setText("&Edit");

		editMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeEditMenu.setMenu(editMenu);		
		registerItem = new MenuItem(editMenu, SWT.PUSH);
		registerItem.setText("&Register Service");
		updateItem = new MenuItem(editMenu, SWT.PUSH);
		updateItem.setText("&Update Service");
		deleteItem = new MenuItem(editMenu, SWT.PUSH);
		deleteItem.setText("&Delete Service");
	} //Edit 메뉴 생성 함수 끝
	
	//Tool 메뉴 생성
	public void setToolMenu(Menu menuBar){
		cascadeToolMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeToolMenu.setText("&Tool");

		toolMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeToolMenu.setMenu(toolMenu);
		similarityCheckItem = new MenuItem(toolMenu, SWT.PUSH);
		similarityCheckItem.setText("Similarity Check");
	} //Tool 메뉴 생성 함수 끝
	
	//Help 메뉴 생성
	public void setHelpMenu(Menu menuBar){
		cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeHelpMenu.setText("&Help");

		helpMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeHelpMenu.setMenu(helpMenu);
		informationItem = new MenuItem(helpMenu, SWT.PUSH);
		informationItem.setText("Information");
		
		MainFrame.shell.setMenuBar(menuBar);
	} //Help 메뉴 생성 함수 끝
	
	//이벤트 처리기 등록
	public void setEventHandler(){
		openFirstServiceItem.addSelectionListener(this);
		openSecondServiceItem.addSelectionListener(this);
		searchServiceItem.addSelectionListener(this);
		exitItem.addSelectionListener(this);
		registerItem.addSelectionListener(this);
		updateItem.addSelectionListener(this);
		deleteItem.addSelectionListener(this);
		similarityCheckItem.addSelectionListener(this);
		informationItem.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//이벤트 처리기
	public void widgetSelected(SelectionEvent e){
		//첫번째 서비스 열기
		if(e.getSource() == openFirstServiceItem){
			MainFrame.stackLayout.topControl = similarityCheckScreen;
			MainFrame.shell.layout();
			new SelectServiceDialog("First Service");
		}
		//두번째 서비스 열기
		else if(e.getSource() == openSecondServiceItem){
			MainFrame.stackLayout.topControl = similarityCheckScreen;
			MainFrame.shell.layout();
			new SelectServiceDialog("Second Service");
		}
		//서비스 검색
		else if(e.getSource() == searchServiceItem){
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
			new SearchServiceDialog();
		}
		//프로그램 종료
		else if(e.getSource() == exitItem){
			MainFrame.shell.getDisplay().dispose();
		}
		//서비스 등록
		else if(e.getSource() == registerItem){
			MainFrame.stackLayout.topControl = new RegisterScreen();        		
			MainFrame.shell.layout();
		}
		//서비스 수정
		else if(e.getSource() == updateItem){
			MainFrame.stackLayout.topControl = new UpdateScreen();
			MainFrame.shell.layout();
		}
		//서비스 삭제
		else if(e.getSource() == deleteItem){
			MainFrame.stackLayout.topControl = new DeleteScreen();
			MainFrame.shell.layout();
		}
		//서비스들 간 유사성 측정
		else if(e.getSource() == similarityCheckItem){
			MainFrame.stackLayout.topControl = similarityCheckScreen;
			MainFrame.shell.layout();
		}
		//프로그램 정보
		else if(e.getSource() == informationItem){
			MessageBox informationMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			informationMessageBox.setText("Information");
			informationMessageBox.setMessage("Semantic similarity analyser of service");
			informationMessageBox.open();
		}
	} //이벤트 처리기 함수 끝
	public void widgetDefaultSelected(SelectionEvent e){}		
}
