package Menu_Register;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import DataBase.DBWork;
import Main.MainFrame;
import WidgetProperty.WidgetProperty;

public class RegisterScreen extends Composite implements SelectionListener{
	//레이아웃
	private GridLayout gridLayout = null;
	
	//회사, 부모, 자식 콤보박스 변수
	private Combo companyNameCombo = null;			
	private Combo parentServiceNameCombo = null;	
	private Combo childServiceNameCombo = null;
	
	//Identifying 속성 변수
	private Text nameText = null;					
	private Text definitionText = null;
	private Text domainTaskText = null;
	private Text sTypeText = null;
	private Text snonymousNamesText = null;
	private Button[] radioButton = null;
	private Text conditionText = null;
	private Text interfaceText = null;
	private Text businessProcessText = null;
	private Text componentText = null;
	
	//Functional 속성 변수
	private Text mandatoryFunctionText = null;		
	private Text secondaryFunctionText = null;
	
	//Selective 속성 변수
	private Button mandatoryButton = null;			
	private Button optionalButton = null;
	private Button alternativeButton = null;
	
	//Relational 속성 변수
	private Text andText = null;					
	private Text orText = null;	
	
	//버튼 변수
	private Button registerButton = null;			
	private Button initializeButton = null;
	private Button cancelButton = null;
	private Color labelColor = null;
	private Color buttonColor = null;
	private Font groupFont = null;

	//생성자
	public RegisterScreen(){
		super(MainFrame.shell, SWT.NONE);
		this.initializeMember();
		this.setLayout(gridLayout);	

		this.createRegisterPosition();
		this.createIdentifyingAttributes();	
		this.createFunctionalAttributes();
		this.createSelectiveAttributes();
		this.createRelationalAttributes();
		this.createButton();
		this.mountEventHandler();
		this.setCompanyNameComboList();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){		
		gridLayout = new GridLayout(1, false);
		labelColor = new Color(this.getParent().getDisplay(), 180,230,240);
		buttonColor = new Color(this.getParent().getDisplay(), 70,210,210);
		groupFont = new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD);
	} //멤버 변수 초기화 함수 끝
	
	//등록할 서비스 위치 속성 생성
	public void createRegisterPosition(){
		Group registerPositionGroup = new Group(this, SWT.BORDER);
		registerPositionGroup.setText("Company Name / Service Name");	
		registerPositionGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		registerPositionGroup.setLayout(gridLayout);
		registerPositionGroup.setLayoutData(WidgetProperty.setGridLayoutProperty(SWT.FILL, true));

		Label companyNameLabel = new Label(registerPositionGroup, SWT.BORDER);	//서비스 회사 이름		
		companyNameLabel.setText("Company Name:");
		companyNameLabel.setBackground(labelColor);
		companyNameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		companyNameCombo = new Combo(registerPositionGroup, SWT.DROP_DOWN);	
		companyNameCombo.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 50, SWT.FILL, true));

		Label parentServiceNameLabel = new Label(registerPositionGroup, SWT.BORDER);	//부모 서비스 이름		
		parentServiceNameLabel.setText("Parent Service Name:");
		parentServiceNameLabel.setBackground(labelColor);
		parentServiceNameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(2, SWT.FILL, true));
		parentServiceNameCombo = new Combo(registerPositionGroup, SWT.DROP_DOWN);
		parentServiceNameCombo.setEnabled(false);	
		parentServiceNameCombo.setLayoutData(WidgetProperty.setGridLayoutProperty(2, SWT.FILL, true));

		Label childServiceNameLabel = new Label(registerPositionGroup, SWT.BORDER);	//자식 서비스 이름	
		childServiceNameLabel.setText("Child Service Name:");
		childServiceNameLabel.setBackground(labelColor);
		childServiceNameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(2, SWT.FILL, true));
		childServiceNameCombo = new Combo(registerPositionGroup, SWT.DROP_DOWN);
		childServiceNameCombo.setEnabled(false);	
		childServiceNameCombo.setLayoutData(WidgetProperty.setGridLayoutProperty(2, SWT.FILL, true));
	} //등록할 서비스 위치 속성 생성 함수 끝
	
	//Identifying 속성 생성
	public void createIdentifyingAttributes(){		
		Group identifyingAttributeGroup = new Group(this, SWT.BORDER);
		identifyingAttributeGroup.setText("Identifying Attribute");	
		identifyingAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(12, false);
		identifyingAttributeGroup.setLayout(gridLayout);
		identifyingAttributeGroup.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.FILL));

		Label nameLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Name	
		nameLabel.setText("Name:");
		nameLabel.setBackground(labelColor);
		nameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		nameText = new Text(identifyingAttributeGroup, SWT.BORDER);
		nameText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));		

		Label definitionLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Definition				
		definitionLabel.setText("Definition:");
		definitionLabel.setBackground(labelColor);
		definitionLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		definitionText = new Text(identifyingAttributeGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		definitionText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label domainTaskLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//DomainTask		
		domainTaskLabel.setText("DomainTask:");
		domainTaskLabel.setBackground(labelColor);
		domainTaskLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		domainTaskText = new Text(identifyingAttributeGroup, SWT.BORDER);
		domainTaskText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label sTypeLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//SType		
		sTypeLabel.setText("SType:");
		sTypeLabel.setBackground(labelColor);
		sTypeLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		sTypeText = new Text(identifyingAttributeGroup, SWT.BORDER);
		sTypeText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label snonymousNamesLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//SynonymousNames		
		snonymousNamesLabel.setText("SynonymousNames:");
		snonymousNamesLabel.setBackground(labelColor);
		snonymousNamesLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		snonymousNamesText = new Text(identifyingAttributeGroup, SWT.BORDER);
		snonymousNamesText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label priorityLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Priority	
		priorityLabel.setText("Priority:");
		priorityLabel.setBackground(labelColor);
		priorityLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		radioButton = new Button[11];
		for(int radioButtonIndex=0; radioButtonIndex < radioButton.length; radioButtonIndex++){
			radioButton[radioButtonIndex] = new Button(identifyingAttributeGroup, SWT.RADIO);
			if(radioButtonIndex == 0){
				radioButton[radioButtonIndex].setSelection(true); 
				radioButton[radioButtonIndex].setText("1.0");
			}
			else{
				//정수를 문자열로 변환
				String buttonText = Integer.toString((radioButton.length) - radioButtonIndex - 1);
				radioButton[radioButtonIndex].setText("0." + buttonText);
			}
			radioButton[radioButtonIndex].setLayoutData(WidgetProperty.setGridLayoutProperty(SWT.LEFT, true));
		}			

		Label conditionLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Condition
		conditionLabel.setText("Condition:");
		conditionLabel.setBackground(labelColor);
		conditionLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		conditionText = new Text(identifyingAttributeGroup, SWT.BORDER);
		conditionText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label interfaceLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Interface		
		interfaceLabel.setText("Interface:");
		interfaceLabel.setBackground(labelColor);
		interfaceLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		interfaceText = new Text(identifyingAttributeGroup, SWT.BORDER);
		interfaceText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label businessProcessLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Business Process		
		businessProcessLabel.setText("Business Process:");
		businessProcessLabel.setBackground(labelColor);
		businessProcessLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		businessProcessText = new Text(identifyingAttributeGroup, SWT.BORDER);
		businessProcessText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));

		Label componentLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Component			
		componentLabel.setText("Component:");		
		componentLabel.setBackground(labelColor);
		componentLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		componentText = new Text(identifyingAttributeGroup, SWT.BORDER);
		componentText.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(11, SWT.FILL));
	} //Identifying 속성 생성 함수 끝
	
	//Functional 속성 생성
	public void createFunctionalAttributes(){
		Group functionalAttributeGroup = new Group(this, SWT.BORDER);
		functionalAttributeGroup.setText("Functional Attribute");	
		functionalAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		functionalAttributeGroup.setLayout(gridLayout);
		functionalAttributeGroup.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.FILL));

		Label mandatoryFunctionLabel = new Label(functionalAttributeGroup, SWT.BORDER);	//MandatoryFunction		
		mandatoryFunctionLabel.setText("MandatoryFunction:");
		mandatoryFunctionLabel.setBackground(labelColor);
		mandatoryFunctionLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		mandatoryFunctionText = new Text(functionalAttributeGroup, SWT.BORDER);
		mandatoryFunctionText.setLayoutData(WidgetProperty.setGridLayoutProperty(10, SWT.FILL, true));

		Label secondaryFunctionLabel = new Label(functionalAttributeGroup, SWT.BORDER);	//SecondaryFunction		
		secondaryFunctionLabel.setText("SecondaryFunction:");
		secondaryFunctionLabel.setBackground(labelColor);
		secondaryFunctionLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		secondaryFunctionText = new Text(functionalAttributeGroup, SWT.BORDER);		
		secondaryFunctionText.setLayoutData(WidgetProperty.setGridLayoutProperty(10, SWT.FILL, true));
	} //Functional 속성 생성 함수 끝
	
	//Selective 속성 생성
	public void createSelectiveAttributes(){
		Group selectiveAttributeGroup = new Group(this, SWT.BORDER);
		selectiveAttributeGroup.setText("Selective Attribute");	
		selectiveAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		selectiveAttributeGroup.setLayout(gridLayout);
		selectiveAttributeGroup.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.FILL));

		Label servicePropertyLabel = new Label(selectiveAttributeGroup, SWT.BORDER);	//Property
		//servicePropertyLabel.setText("Service's Property:");
		servicePropertyLabel.setBackground(labelColor);
		servicePropertyLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));

		mandatoryButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Mandatory Property
		mandatoryButton.setText("Mandatory Service");	
		mandatoryButton.setSelection(true);
		mandatoryButton.setLayoutData(WidgetProperty.setGridLayoutProperty(3, SWT.LEFT, true));

		optionalButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Optional Property
		optionalButton.setText("Optional Service");		
		optionalButton.setLayoutData(WidgetProperty.setGridLayoutProperty(4, SWT.LEFT, true));

		alternativeButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Alternative Property
		alternativeButton.setText("Alternative Service");
		alternativeButton.setLayoutData(WidgetProperty.setGridLayoutProperty(3, SWT.LEFT, true));
	} //Selective 속성 생성 함수 끝
	
	//Relational 속성 생성
	public void createRelationalAttributes(){
		Group relationalAttributeGroup = new Group(this, SWT.BORDER);
		relationalAttributeGroup.setText("Relational Attribute");
		relationalAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		relationalAttributeGroup.setLayout(gridLayout);	
		relationalAttributeGroup.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.FILL));

		Label andLabel = new Label(relationalAttributeGroup, SWT.BORDER);	//Pre-Self		
		andLabel.setText("Pre-Self:");
		andLabel.setBackground(labelColor);
		andLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		andText = new Text(relationalAttributeGroup, SWT.BORDER);
		andText.setLayoutData(WidgetProperty.setGridLayoutProperty(10, SWT.FILL, true));

		Label orLabel = new Label(relationalAttributeGroup, SWT.BORDER);	//Self-Post			
		orLabel.setText("Self-Post:");
		orLabel.setBackground(labelColor);
		orLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(110));
		orText = new Text(relationalAttributeGroup, SWT.BORDER);
		orText.setLayoutData(WidgetProperty.setGridLayoutProperty(10, SWT.FILL, true));	
	} //Relational 속성 생성 함수 끝
	
	//버튼 생성
	public void createButton(){
		Group buttonGroup = new Group(this, SWT.SHADOW_ETCHED_IN);	
		GridLayout gridLayout = new GridLayout(5, false);
		buttonGroup.setLayout(gridLayout);		
		buttonGroup.setLayoutData(WidgetProperty.setGridLayoutAlignProperty(SWT.FILL));

		registerButton = new Button(buttonGroup, SWT.PUSH);	//Register 버튼	
		registerButton.setBackground(buttonColor);
		registerButton.setText("Register");
		registerButton.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 80, SWT.RIGHT, true));

		initializeButton = new Button(buttonGroup, SWT.PUSH);	//Initialize 버튼
		initializeButton.setBackground(buttonColor);
		initializeButton.setText("Initialize");
		initializeButton.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 80, SWT.CENTER, false));

		cancelButton = new Button(buttonGroup, SWT.PUSH);	//Cancel 버튼	
		cancelButton.setBackground(buttonColor);
		cancelButton.setText("Cancel");		
		cancelButton.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 80, SWT.LEFT, true));
	} //버튼 생성 함수 끝
	
	//이벤트 처리기 등록
	public void mountEventHandler(){
		companyNameCombo.addSelectionListener(this);
		childServiceNameCombo.addSelectionListener(this);
		registerButton.addSelectionListener(this);
		initializeButton.addSelectionListener(this);
		cancelButton.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//회사 콤보 목록 생성
	public void setCompanyNameComboList(){
		String[] companyList = DBWork.setCompanyNameComboList();
		companyNameCombo.setItems(companyList);
	} //회사 콤보 목록 생성 함수 끝
	
	//부모 서비스 콤보 목록 생성
	public void setParentServiceNameComboList(){
		parentServiceNameCombo.setEnabled(true);
		int selectedItemIndex = companyNameCombo.getSelectionIndex();
		String selectedItem = companyNameCombo.getItem(selectedItemIndex);
		String[] parentList = DBWork.setParentServiceNameComboList(selectedItem);
		parentServiceNameCombo.setItems(parentList);
	} //부모 서비스 콤보 목록 생성 함수 끝
	
	//자식 서비스 콤보 목록 생성
	public void setChildServiceNameComboList(){
		childServiceNameCombo.setEnabled(true);
		int selectedItemIndex = companyNameCombo.getSelectionIndex();
		String selectedItem = companyNameCombo.getItem(selectedItemIndex);
		String[] childList = DBWork.setChildServiceNameComboList(selectedItem);
		childServiceNameCombo.setItems(childList);
	} //자식 서비스 콤보 목록 생성 함수 끝
	
	//위젯 초기화
	public void initializeWidgets(){
		companyNameCombo.deselectAll();
		parentServiceNameCombo.deselectAll();
		childServiceNameCombo.deselectAll();
		parentServiceNameCombo.setEnabled(false);
		childServiceNameCombo.setEnabled(false);
		nameText.setText("");
		definitionText.setText("");
		domainTaskText.setText("");
		sTypeText.setText("");
		snonymousNamesText.setText("");
		for(int radioButtonIndex=0; radioButtonIndex < radioButton.length; radioButtonIndex++){
			if(radioButton[radioButtonIndex].getSelection()){		//선택된 라디오 버튼 확인
				radioButton[radioButtonIndex].setSelection(false);	//선택 해제
				radioButton[0].setSelection(true);
				break;
			}
		}
		conditionText.setText("");
		interfaceText.setText("");
		businessProcessText.setText("");
		componentText.setText("");
		mandatoryFunctionText.setText("");
		secondaryFunctionText.setText("");
		Button[] servicePropertyRadioButton = {mandatoryButton, optionalButton, alternativeButton};
		for(int index = 0; index < servicePropertyRadioButton.length; index++){					
			if(servicePropertyRadioButton[index].getSelection()){					
				servicePropertyRadioButton[index].setSelection(false);	//선택 해제
				servicePropertyRadioButton[0].setSelection(true);
			}
		}
		andText.setText("");
		orText.setText("");
	} //위젯 초기화 함수 끝
	
	//이벤트 처리기
	public void widgetDefaultSelected(SelectionEvent e){}
	public void widgetSelected(SelectionEvent e){
		//회사 목록에서 선택하면
		if(e.getSource() == companyNameCombo){	
			setParentServiceNameComboList();        		
			setChildServiceNameComboList();
		}
		//등록 버튼
		else if(e.getSource() == registerButton){	
			String radioButtonString = null;				
			double priority = 0;
			for(int radioButtonIndex=0; radioButtonIndex < radioButton.length; radioButtonIndex++){ //선택된 라디오 버튼의 텍스트를 추출
				if(radioButton[radioButtonIndex].getSelection()){	
					radioButtonString = radioButton[radioButtonIndex].getText();	//선택된 라디오 버튼의 문자열을 추출
					break;
				}
			} 
			try{	//라디오 버튼의 문자열을 정수로 치환
				priority = Double.parseDouble(radioButtonString);	//추출된 라디오 버튼 문자열을 실수로 치환 "10.0" -> 10.0
			}catch(NumberFormatException numberFormatException){
				priority = -1;	//라디오 버튼이 아무것도 선택되지 않았을 경우 -1
			}
			String servicePropertyString = null;
			Button[] servicePropertyRadioButton = {mandatoryButton, optionalButton, alternativeButton};
			for(int index = 0; index < servicePropertyRadioButton.length; index++){
				if(servicePropertyRadioButton[index].getSelection()){
					servicePropertyString = servicePropertyRadioButton[index].getText();
					break;
				}
			}
			/////////////////////////////////////////////////////////////////////
			String parentService = null;
			if((parentServiceNameCombo.getText()).equals("")){	//부모 콤보가 비어 있다면
				parentService = "No_Parent";
			}else{
				parentService = parentServiceNameCombo.getText();
			}
			/////////////////////////////////////////////////////////////////////
			
			/////////////////////////////////////////////////////////////////////
			String childService = null;
			if((childServiceNameCombo.getText()).equals("")){	//자식 콤보가 비어 있다면
				childService = "No_Child";
			}else{
				childService = childServiceNameCombo.getText();
			}
			/////////////////////////////////////////////////////////////////////
			
			//서비스 등록
			DBWork.addService(companyNameCombo.getText(), parentService, childService,		
					nameText.getText(), definitionText.getText(), domainTaskText.getText(), sTypeText.getText(), 
					snonymousNamesText.getText(), priority, conditionText.getText(), interfaceText.getText(), 
					businessProcessText.getText(), componentText.getText(),	mandatoryFunctionText.getText(), 
					secondaryFunctionText.getText(), servicePropertyString,	andText.getText(), orText.getText());
			setCompanyNameComboList();	//서비스 등록 후 회사 콤보박스 갱신
			initializeWidgets();		//등록 후 초기화
		}
		//초기화 버튼
		else if(e.getSource() == initializeButton){	
			initializeWidgets();
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){		
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
		}
	} //이벤트 처리기 함수 끝
}