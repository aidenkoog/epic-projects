package Menu_Delete;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import DataBase.DBWork;
import Main.MainFrame;
import WidgetProperty.WidgetProperty;

public class DeleteScreen extends Composite implements SelectionListener{
	private GridLayout gridLayout = null;
	private Color buttonColor = null;
	private Color tableCellColor = null;
	private Font groupFont = null;
	private Group treeGroup = null;
	private Button selectAllTreeItemButton = null;
	private Tree deleteTree = null;
	private TreeColumn deleteTreeColumn = null;
	private Group tableGroup = null;
	private Button selectAllTableItemButton = null;
	private Table deleteTable = null;
	private TableColumn tableColumn = null;
	private Button viewBriefButton = null;
	private Button viewDetailsButton = null;
	private Group buttonGroup = null;
	private Button initializeButton = null;
	private Button cancelButton = null;

	//생성자
	public DeleteScreen(){
		super(MainFrame.shell, SWT.NONE);
		this.initializeMember();
		this.setLayout(gridLayout);
		this.createTitleLabel();
		this.createTree();				
		this.createBriefButton(); 		
		this.createTable();
		this.createViewDetailButton();
		this.createButton();
		this.mountEventHandler();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		gridLayout = new GridLayout(5, false);
		buttonColor = new Color(this.getParent().getDisplay(), 70,210,210);
		tableCellColor = new Color(this.getParent().getDisplay(), 180,230,240);
		groupFont = new Font(this.getParent().getDisplay(), "Miriam", 10, SWT.BOLD);
	} //멤버 변수 초기화 함수 끝
	
	//제목레이블 생성
	public void createTitleLabel(){
		Label titleLabel = new Label(this, SWT.BORDER);
		titleLabel.setText("◆ Delete Services ◆");
		titleLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 15, SWT.BOLD));
		titleLabel.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(5, SWT.FILL));
	} //제목레이블 생성 함수 끝
	
	//트리 생성
	public void createTree(){
		treeGroup = new Group(this, SWT.SHADOW_ETCHED_OUT);	//트리 그룹 생성
		treeGroup.setText("Tree Classification");	
		treeGroup.setFont(groupFont);
		treeGroup.setLayout(new GridLayout(1, false));
		treeGroup.setLayoutData(WidgetProperty.setGridLayoutNotGrabVerticalProperty(1, SWT.FILL));

		selectAllTreeItemButton = new Button(treeGroup, SWT.CHECK);	//트리 전체 선택 버튼
		selectAllTreeItemButton.setText("Select All");
		deleteTree = new Tree(treeGroup, SWT.BORDER | SWT.V_SCROLL | SWT.CHECK);	//트리 생성
		deleteTree.setLinesVisible(true);	
		deleteTree.setHeaderVisible(true);
		deleteTreeColumn = new TreeColumn(deleteTree, SWT.LEFT);
		deleteTreeColumn.setWidth(130); 
		deleteTreeColumn.setResizable(false);
		deleteTreeColumn.setText("Companies / Services");
		deleteTree.setLayoutData(WidgetProperty.setGridLayoutVerticalProperty(1, 450, SWT.FILL, true));
		String[] companyNameTree = DBWork.setCompanyNameComboList();	//회사 리스트를 가져옴
		int companyNumber = companyNameTree.length;	//회사의 개수
		for(int index = 0; index < companyNumber; index++){	//회사 이름(테이블 명)
			TreeItem companyTreeItem = new TreeItem(deleteTree, SWT.NONE);	//회사이름 트리 아이템 생성 
			companyTreeItem.setText(companyNameTree[index]);
			String[] serviceNameTree = DBWork.setParentServiceNameComboList(companyNameTree[index]);
			int serviceNumber = serviceNameTree.length;	//서비스 개수
			for(int j = 0; j < serviceNumber; j++){	//서비스 명을 등록
				TreeItem serviceTreeItem = new TreeItem(companyTreeItem, SWT.NONE);
				serviceTreeItem.setText(serviceNameTree[j]);
			}			
		}
		Label explainTreeLabel = new Label(treeGroup, SWT.NONE);
		explainTreeLabel.setText("▷ Registered Services List");
		explainTreeLabel.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.CENTER));
	} //트리 생성 함수 끝
	
	//테이블 생성
	public void createTable(){
		tableGroup = new Group(this, SWT.SHADOW_ETCHED_OUT);	//테이블 그룹 생성
		tableGroup.setText("Service Attribute Table");	
		tableGroup.setFont(groupFont);
		tableGroup.setLayout(new GridLayout(2, false));	
		tableGroup.setLayoutData(WidgetProperty.setGridLayoutProperty(3, SWT.FILL, true));

		selectAllTableItemButton = new Button(tableGroup, SWT.CHECK);	//테이블 전체 선택 버튼
		selectAllTableItemButton.setText("Select All");
		deleteTable = new Table(tableGroup, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);	//테이블 생성
		deleteTable.setLayoutData(WidgetProperty.setGridLayoutVerticalProperty(2, 440, SWT.FILL, true)); 

		String[] tableColumnList = {"Company", "Name", "DomainTask", "Stype"};	//테이블 컬럼 4개 생성
		for(int index = 0; index < tableColumnList.length; index++){
			tableColumn = new TableColumn(deleteTable, SWT.LEFT); 
			tableColumn.setText(tableColumnList[index]);
			if(index == 3){
				tableColumn.setWidth(100);	//컬럼 사이즈 설정
			}else{
				tableColumn.setWidth(135);	//컬럼 사이즈 설정
			}			
		} 
		deleteTable.setHeaderVisible(true);	//테이블 컬럼 보이기 
		deleteTable.setLinesVisible(true);	//테이블 라인 보이기
		Label explainTableLabel = new Label(tableGroup, SWT.NONE);
		explainTableLabel.setText("▷ Selected Services's Properties Table List ");
		explainTableLabel.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.LEFT));
	} //테이블 생성 함수 끝
	
	//▶버튼 생성
	public void createBriefButton(){
		viewBriefButton = new Button(this, SWT.PUSH); //▶버튼	 
		viewBriefButton.setBackground(buttonColor);
		viewBriefButton.setText("▶");
		viewBriefButton.setSize(50,50);
		viewBriefButton.setLayoutData(WidgetProperty.setGridLayoutVerticalProperty(1, 50));
	} //▶버튼 생성 함수 끝
	
	//▶▶자세히 보기 버튼 생성
	public void createViewDetailButton(){
		viewDetailsButton = new Button(tableGroup, SWT.NONE);
		viewDetailsButton.setBackground(buttonColor);
		viewDetailsButton.setText("View Service Details ▶▶");
		viewDetailsButton.setSize(50,50);
		viewDetailsButton.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.RIGHT));
	} //▶▶자세히 보기 버튼 생성 함수 끝
	
	//버튼 생성
	public void createButton(){
		buttonGroup = new Group(this, SWT.SHADOW_ETCHED_OUT);
		buttonGroup.setLayout(new GridLayout(4, false));
		buttonGroup.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(5, SWT.FILL));

		initializeButton = new Button(buttonGroup, SWT.PUSH); 
		initializeButton.setBackground(buttonColor);
		initializeButton.setText("Initialize");
		initializeButton.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 80, SWT.RIGHT, true));

		cancelButton = new Button(buttonGroup, SWT.PUSH);
		cancelButton.setBackground(buttonColor);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 80, SWT.LEFT, true));	
	} //버튼 생성 함수 끝
	
	//이벤트 처리기 등록
	public void mountEventHandler(){
		deleteTree.addSelectionListener(this);
		selectAllTreeItemButton.addSelectionListener(this);
		selectAllTableItemButton.addSelectionListener(this);
		viewBriefButton.addSelectionListener(this);
		viewDetailsButton.addSelectionListener(this);
		initializeButton.addSelectionListener(this);
		cancelButton.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//이벤트 처리기
	public void widgetSelected(SelectionEvent e){
		//트리 전체 선택 체크 버튼
		if(e.getSource() == selectAllTreeItemButton){	
			Button button = (Button)e.widget;
			TreeItem[] companyTreeItem = deleteTree.getItems();
			if (button.getSelection()){		            
				for(int i=0; i<companyTreeItem.length; i++){
					companyTreeItem[i].setChecked(true);
					TreeItem[] serviceTreeItem = companyTreeItem[i].getItems();
					for(int j=0; j<serviceTreeItem.length; j++){
						serviceTreeItem[j].setChecked(true);
					}
				}
			}else{
				for(int i=0; i<companyTreeItem.length; i++){
					companyTreeItem[i].setChecked(false);
					TreeItem[] serviceTreeItem = companyTreeItem[i].getItems();
					for(int j=0; j<serviceTreeItem.length; j++){
						serviceTreeItem[j].setChecked(false);
					}
				}
			}
		}
		//▶버튼
		else if(e.getSource() == viewBriefButton){	
			TreeItem[] companyTreeItemList = deleteTree.getItems();	//회사 트리 아이템 추출
			for(int i=0; i < companyTreeItemList.length; i++){
				TreeItem[] serviceTreeItemList = companyTreeItemList[i].getItems();	//회사 내의 서비스들 추출
				for(int j=0; j < serviceTreeItemList.length; j++){
					if(serviceTreeItemList[j].getChecked()){
						String companyNameString = companyTreeItemList[i].getText();	//회사 이름(부모 트리 노드)
						String serviceNameString = serviceTreeItemList[j].getText();	//서비스 이름
						String[] tablePropertyList = DBWork.getTableProperty(companyNameString, serviceNameString);
						TableItem tableItem = new TableItem(deleteTable, SWT.NONE);	//테이블 아이템 생성
						tableItem.setBackground(0, tableCellColor);
						for(int columnIndex = 0; columnIndex < 4; columnIndex++){	//테이블 칼럼 크기만큼 텍스트 설정
							tableItem.setText(columnIndex, tablePropertyList[columnIndex]);
						}
					}
				}
			}
		}
		//▶▶자세히 보기 버튼
		else if(e.getSource() == viewDetailsButton){	
			TableItem[] tableItem = deleteTable.getItems();
			int numberOfCheckedItem = 0;
			for(int i=0; i<tableItem.length; i++){
				if(tableItem[i].getChecked()){	//테이블 아이템이 체크 상태이면
					numberOfCheckedItem++;
				}
			}
			DeleteDetailScreen deleteDetailScreen = new DeleteDetailScreen();
			for(int i=0; i<tableItem.length; i++){
				if(tableItem[i].getChecked()){	//테이블 아이템이 체크 상태이면
					deleteDetailScreen.createTabItem(tableItem[i].getText(1), tableItem[i].getText(0));	//서비스, 회사
				}
			}
			if(numberOfCheckedItem != 0){
				MainFrame.stackLayout.topControl = deleteDetailScreen;
				MainFrame.shell.layout();
			}
		}
		//테이블 전체 선택 체크 버튼
		else if(e.getSource() == selectAllTableItemButton){	
			Button button = (Button) e.widget;
			TableItem[] tableItem = deleteTable.getItems();
			if (button.getSelection()){		            
				for(int i=0; i<tableItem.length; i++){
					tableItem[i].setChecked(true);
				}
			}else{
				for(int i=0; i<tableItem.length; i++){
					tableItem[i].setChecked(false);
				}
			}
		}
		//트리
		else if(e.getSource() == deleteTree){	
			if(e.detail == SWT.CHECK){
				TreeItem item = (TreeItem)e.item;
				boolean checked = item.getChecked();
				checkItems(item, checked);
				checkPath(item.getParentItem(), checked, false);
			}
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){	
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
		}
		//초기화 버튼
		else if(e.getSource() == initializeButton){	
			selectAllTreeItemButton.setSelection(false);
			selectAllTableItemButton.setSelection(false);
			TreeItem[] treeItem = deleteTree.getItems();
			for(int i=0; i<treeItem.length; i++){
				if(treeItem[i].getChecked()){
					treeItem[i].setChecked(false); 
				}
				if(treeItem[i].getExpanded()){
					treeItem[i].setExpanded(false);
				}
				if(treeItem[i].getItemCount() > 0){
					for(int j=0; j<treeItem[i].getItems().length; j++){
						if(treeItem[i].getItems()[j].getChecked()){
							treeItem[i].getItems()[j].setChecked(false);
						}
					}
				}
			}
			deleteTable.removeAll();
		}
	}
	public void widgetDefaultSelected(SelectionEvent e){}
	
	//트리 체크 루틴
	///////////////////////////////////////////////////////////////////////////////////////
	private void checkPath(TreeItem item, boolean checked, boolean grayed){
		if(item == null){
			return;
		}
		if(grayed){
			checked = true;
		}else{
			int index = 0;
			TreeItem[] items = item.getItems();
			while(index < items.length){
				TreeItem child = items[index];
				if(child.getGrayed() || checked != child.getChecked()){
					checked = grayed = true;
					break;
				}
				index++;
			}
		}
		item.setChecked(checked);
		item.setGrayed(grayed);
		checkPath(item.getParentItem(), checked, grayed);
	}
	private void checkItems(TreeItem item, boolean checked){
		item.setGrayed(false);
		item.setChecked(checked);
		TreeItem[] items = item.getItems();
		for(int i = 0; i < items.length; i++){
			checkItems(items[i], checked);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////
	//트리 체크 루틴 함수 끝
}