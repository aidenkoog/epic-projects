import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

public class ToolMenu implements SelectionListener{	
	private Menu menuBar = null;
	
	//File 메뉴 변수
	private MenuItem cascadeFileMenu = null;			
	private Menu fileMenu = null;
	private MenuItem openFirstFeatureItem = null;
	private MenuItem openSecondFeatureItem = null;
	private MenuItem searchFeatureItem = null;
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
	private MenuItem checkFeatureItem = null;
	
	//Help 메뉴 변수
	private MenuItem cascadeHelpMenu = null;			
	private Menu helpMenu = null;
	private MenuItem informationItem = null;
	
    private AnalyzeFeatureScreen analyzeFeatureScreen = null;
    
	//생성자
	public ToolMenu(){
		this.initializeMember();
		this.setFileMenu(menuBar);	
		this.setEditMenu(menuBar);	
		this.setToolMenu(menuBar);	
		this.setHelpMenu(menuBar);
		this.mountEventHandler();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){				
		menuBar = new Menu(MainFrame.shell, SWT.BAR);	
		analyzeFeatureScreen = new AnalyzeFeatureScreen();
	} //멤버 변수 초기화 함수 끝
	
	//File 메뉴 생성
	public void setFileMenu(Menu menuBar){
		cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");

		fileMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
		openFirstFeatureItem = new MenuItem(fileMenu, SWT.PUSH); 
		openFirstFeatureItem.setText("Open F&irst Feature");
		openSecondFeatureItem = new MenuItem(fileMenu, SWT.PUSH);
		openSecondFeatureItem.setText("&Open S&econd Feature");
		searchFeatureItem = new MenuItem(fileMenu, SWT.PUSH);
		searchFeatureItem.setText("Search &Feature");
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
		registerItem.setText("&Register Feature");
		updateItem = new MenuItem(editMenu, SWT.PUSH);
		updateItem.setText("&Update Feature");
		deleteItem = new MenuItem(editMenu, SWT.PUSH);
		deleteItem.setText("&Delete Feature");
	} //Edit 메뉴 생성 함수 끝
	
	//Tool 메뉴 생성
	public void setToolMenu(Menu menuBar){
		cascadeToolMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeToolMenu.setText("&Tool");

		toolMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeToolMenu.setMenu(toolMenu);
		checkFeatureItem = new MenuItem(toolMenu, SWT.PUSH);
		checkFeatureItem.setText("Analyze Feature");
	} //Tool 메뉴 생성 함수 끝
	
	//Help 메뉴 생성
	public void setHelpMenu(Menu menuBar){
		cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeHelpMenu.setText("&Help");

		helpMenu = new Menu(MainFrame.shell, SWT.DROP_DOWN);
		cascadeHelpMenu.setMenu(helpMenu);
		informationItem = new MenuItem(helpMenu, SWT.PUSH);
		informationItem.setText("Information...");
		
		MainFrame.shell.setMenuBar(menuBar);
	} //Help 메뉴 생성 함수 끝
	
	//이벤트 처리기 등록
	public void mountEventHandler(){
		openFirstFeatureItem.addSelectionListener(this);
		openSecondFeatureItem.addSelectionListener(this);
		searchFeatureItem.addSelectionListener(this);
		exitItem.addSelectionListener(this);
		registerItem.addSelectionListener(this);
		updateItem.addSelectionListener(this);
		deleteItem.addSelectionListener(this);
		checkFeatureItem.addSelectionListener(this);
		informationItem.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//이벤트 처리기
	public void widgetSelected(SelectionEvent e){
		//첫번째 휘처 열기
		if(e.getSource() == openFirstFeatureItem){
			MainFrame.stackLayout.topControl = analyzeFeatureScreen;
			MainFrame.shell.layout();
			new SelectFeatureDialog("First Service");
		}
		//두번째 휘처 열기
		else if(e.getSource() == openSecondFeatureItem){
			MainFrame.stackLayout.topControl = analyzeFeatureScreen;
			MainFrame.shell.layout();
			new SelectFeatureDialog("Second Service");
		}
		//휘처 검색
		else if(e.getSource() == searchFeatureItem){
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
			new SearchFeatureDialog();
		}
		//프로그램 종료
		else if(e.getSource() == exitItem){
			MainFrame.shell.getDisplay().dispose();
		}
		//휘처 등록
		else if(e.getSource() == registerItem){
			MainFrame.stackLayout.topControl = new RegisterScreen();        		
			MainFrame.shell.layout();
		}
		//휘처 수정
		else if(e.getSource() == updateItem){}
		//휘처 삭제
		else if(e.getSource() == deleteItem){}
		//휘처들 간 분석
		else if(e.getSource() == checkFeatureItem){
			MainFrame.stackLayout.topControl = analyzeFeatureScreen;
			MainFrame.shell.layout();
		}
		//프로그램 정보
		else if(e.getSource() == informationItem){
			MessageBox informationMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			informationMessageBox.setText("Information");
			informationMessageBox.setMessage("Feature Refinement Analyzer");
			informationMessageBox.open();
		}
	} //이벤트 처리기 함수 끝
	public void widgetDefaultSelected(SelectionEvent e){}		
}