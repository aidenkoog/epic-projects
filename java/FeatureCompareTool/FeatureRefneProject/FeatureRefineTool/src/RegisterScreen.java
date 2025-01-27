import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RegisterScreen extends Composite implements SelectionListener{
	private GridLayout gridLayout = null;
	private Text nameText = null;
	private Text classificationText = null;
	private Text fTypeText = null;
	private Text synonymousNamesText = null;
	private Button[] radioButton = null;
	private Text conditionText = null;
	private Text definitionText = null;
	private Button mandatoryButton = null;
	private Button optionalButton = null;
	private Button alternativeButton = null;
	private Button composedOfButton = null;
	private Button generalizedByButton = null;	
	private Button implementedByButton = null;
	private Text identifierText = null;
	private Text registrationAuthorityText = null;
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
		this.createIdentifyingAttributes();
		this.createSelectiveAttributes();		
		this.createRelationalAttributes();
		this.createManagingAttributes();
		this.createRegisterButton();	
		this.mountEventHandler();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		gridLayout = new GridLayout(1, false);
		labelColor = new Color(this.getParent().getDisplay(), 200,230,230);
		buttonColor = new Color(this.getParent().getDisplay(), 70,210,210);
		groupFont = new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD);
	} //멤버 변수 초기화 함수 끝
	
	//Identifying Attributes 생성
	public void createIdentifyingAttributes(){		
		Group identifyingAttributeGroup = new Group(this, SWT.BORDER);
		identifyingAttributeGroup.setText("Identifying Attribute");	
		identifyingAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(12, false);
		identifyingAttributeGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		identifyingAttributeGroup.setLayoutData(gridData);

		Label nameLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Feature Name	
		nameLabel.setText("Name:");
		nameLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		nameLabel.setLayoutData(gridData);
		nameText = new Text(identifyingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		nameText.setLayoutData(gridData);		

		Label classificationLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Classification			
		classificationLabel.setText("Classification:");
		classificationLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		classificationLabel.setLayoutData(gridData);
		classificationText = new Text(identifyingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;		
		classificationText.setLayoutData(gridData);

		Label fTypeLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//FType		
		fTypeLabel.setText("FType:");
		fTypeLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		fTypeLabel.setLayoutData(gridData);
		fTypeText = new Text(identifyingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		fTypeText.setLayoutData(gridData);

		Label snonymousNamesLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//SynonymousNames	
		snonymousNamesLabel.setText("SynonymousNames:");
		snonymousNamesLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		snonymousNamesLabel.setLayoutData(gridData);
		synonymousNamesText = new Text(identifyingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		synonymousNamesText.setLayoutData(gridData);

		Label priorityLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Priority	
		priorityLabel.setText("Priority:");
		priorityLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		priorityLabel.setLayoutData(gridData);
		radioButton = new Button[11];
		for(int radioButtonIndex=0; radioButtonIndex < radioButton.length; radioButtonIndex++){
			radioButton[radioButtonIndex] = new Button(identifyingAttributeGroup, SWT.RADIO);
			if(radioButtonIndex == 0){
				radioButton[radioButtonIndex].setSelection(true); 
				radioButton[radioButtonIndex].setText("1.0");
			}
			else{
				String buttonText = Integer.toString((radioButton.length) - radioButtonIndex - 1);
				radioButton[radioButtonIndex].setText("0." + buttonText);
			}
			gridData = new GridData();			
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = SWT.LEFT;
			radioButton[radioButtonIndex].setLayoutData(gridData);
		}		

		Label conditionLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Condition		
		conditionLabel.setText("Condition:");
		conditionLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		conditionLabel.setLayoutData(gridData);
		conditionText = new Text(identifyingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		conditionText.setLayoutData(gridData);

		Label definitionLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Definition	
		definitionLabel.setText("Definition:");
		definitionLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		definitionLabel.setLayoutData(gridData);
		definitionText = new Text(identifyingAttributeGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		definitionText.setLayoutData(gridData);
	} //Identifying Attributes 생성 함수 끝
	
	//Selective Attributes 생성
	public void createSelectiveAttributes(){
		Group selectiveAttributeGroup = new Group(this, SWT.BORDER);
		selectiveAttributeGroup.setText("Selective Attribute");	
		selectiveAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		GridData gridData = new GridData();	
		selectiveAttributeGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		selectiveAttributeGroup.setLayoutData(gridData);

		Label featurePropertyLabel = new Label(selectiveAttributeGroup, SWT.BORDER);
		featurePropertyLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		featurePropertyLabel.setLayoutData(gridData);

		mandatoryButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Mandatory
		mandatoryButton.setText("Mandatory");	
		mandatoryButton.setSelection(true);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		mandatoryButton.setLayoutData(gridData);

		optionalButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Optional
		optionalButton.setText("Optional");		
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		optionalButton.setLayoutData(gridData);

		alternativeButton = new Button(selectiveAttributeGroup, SWT.RADIO);	//Alternative
		alternativeButton.setText("Alternative");
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		alternativeButton.setLayoutData(gridData);
	} //Selective Attributes 생성 함수 끝
	
	//Relational Attributes 생성
	public void createRelationalAttributes(){
		Group relationalAttributeGroup = new Group(this, SWT.BORDER);
		relationalAttributeGroup.setText("Relational Attribute");
		relationalAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		GridData gridData = new GridData();	
		relationalAttributeGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		relationalAttributeGroup.setLayoutData(gridData);

		Label featurePropertyLabel = new Label(relationalAttributeGroup, SWT.BORDER);
		featurePropertyLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		featurePropertyLabel.setLayoutData(gridData);

		composedOfButton = new Button(relationalAttributeGroup, SWT.RADIO);	//ComposedOf
		composedOfButton.setText("ComposedOf");	
		composedOfButton.setSelection(true);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		composedOfButton.setLayoutData(gridData);

		generalizedByButton = new Button(relationalAttributeGroup, SWT.RADIO);	//GeneralizedBy
		generalizedByButton.setText("GeneralizedBy");		
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		generalizedByButton.setLayoutData(gridData);

		implementedByButton = new Button(relationalAttributeGroup, SWT.RADIO);	//ImplementedBy
		implementedByButton.setText("ImplementedBy");
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		implementedByButton.setLayoutData(gridData);	
	} //Relational Attributes 생성 함수 끝
	
	//Managing Attributes 생성
	public void createManagingAttributes(){
		Group managingAttributeGroup = new Group(this, SWT.BORDER);
		managingAttributeGroup.setText("Managing Attribute");	
		managingAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		GridData gridData = new GridData();	
		managingAttributeGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		managingAttributeGroup.setLayoutData(gridData);

		Label identifierLabel = new Label(managingAttributeGroup, SWT.BORDER);	//Identifier		
		identifierLabel.setText("Identifier:");
		identifierLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		identifierLabel.setLayoutData(gridData);
		identifierText = new Text(managingAttributeGroup, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 10;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		identifierText.setLayoutData(gridData);

		Label registrationAuthorityLabel = new Label(managingAttributeGroup, SWT.BORDER);	//Registration Authority		
		registrationAuthorityLabel.setText("Registration Authority:");
		registrationAuthorityLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		registrationAuthorityLabel.setLayoutData(gridData);
		registrationAuthorityText = new Text(managingAttributeGroup, SWT.BORDER);		
		gridData = new GridData();
		gridData.horizontalSpan = 10;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		registrationAuthorityText.setLayoutData(gridData);
	} //Managing Attributes 생성 함수 끝
	
	//버튼 생성
	public void createRegisterButton(){
		Group buttonGroup = new Group(this, SWT.SHADOW_ETCHED_IN);		
		GridLayout gridLayout = new GridLayout(5, false);
		GridData gridData = new GridData();	
		buttonGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		buttonGroup.setLayoutData(gridData);

		registerButton = new Button(buttonGroup, SWT.PUSH);	//Register 버튼	
		registerButton.setBackground(buttonColor);
		registerButton.setText("Register");
		gridData = new GridData();	
		gridData.widthHint = 80;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.grabExcessHorizontalSpace = true;
		registerButton.setLayoutData(gridData);

		initializeButton = new Button(buttonGroup, SWT.PUSH);	//Initialize 버튼
		initializeButton.setBackground(buttonColor);
		initializeButton.setText("Initialize");
		gridData = new GridData();	
		gridData.minimumWidth = 80;
		gridData.horizontalAlignment = SWT.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		initializeButton.setLayoutData(gridData);

		cancelButton = new Button(buttonGroup, SWT.PUSH);	//Cancel 버튼
		cancelButton.setBackground(buttonColor);
		cancelButton.setText("Cancel");		
		gridData = new GridData();	
		gridData.widthHint = 80;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(gridData);
	} //버튼 생성 함수 끝
	
	//이벤트 처리기 등록
	public void mountEventHandler(){
		registerButton.addSelectionListener(this);
		initializeButton.addSelectionListener(this);
		cancelButton.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//이벤트 처리기
	public void widgetSelected(SelectionEvent e){
		//등록 버튼
		if(e.getSource() == registerButton){	
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
			
			String relationalPropertyString = null;
			Button[] relationalPropertyRadioButton = {composedOfButton, generalizedByButton, implementedByButton};
			for(int index = 0; index < relationalPropertyRadioButton.length; index++){
				if(relationalPropertyRadioButton[index].getSelection()){
					relationalPropertyString = relationalPropertyRadioButton[index].getText();
					break;
				}
			}
			MainFrame.featureQueries.addService(nameText.getText(), classificationText.getText(), fTypeText.getText(), synonymousNamesText.getText(), 
											  priority, conditionText.getText(), definitionText.getText(), 
											  servicePropertyString,	
											  relationalPropertyString,
											  identifierText.getText(), registrationAuthorityText.getText());
		}

		//초기화 버튼
		else if(e.getSource() == initializeButton){	
			nameText.setText("");
			classificationText.setText("");
			fTypeText.setText("");
			definitionText.setText("");
			synonymousNamesText.setText("");
			for(int radioButtonIndex=0; radioButtonIndex < radioButton.length; radioButtonIndex++){
				if(radioButton[radioButtonIndex].getSelection()){		//선택된 라디오 버튼 확인
					radioButton[radioButtonIndex].setSelection(false);	//선택 해제
					break;
				}
			}
			conditionText.setText("");
			definitionText.setText("");
			Button[] servicePropertyRadioButton = {mandatoryButton, optionalButton, alternativeButton};
			for(int index = 0; index < servicePropertyRadioButton.length; index++){					
				if(servicePropertyRadioButton[index].getSelection()){					
					servicePropertyRadioButton[index].setSelection(false);	//선택 해제
				}
			}
			Button[] relationalRadioButton = {composedOfButton, generalizedByButton, implementedByButton};
			for(int index = 0; index < relationalRadioButton.length; index++){
				if(relationalRadioButton[index].getSelection()){
					relationalRadioButton[index].setSelection(false);	//선택 해제
					break;
				}
			}
			identifierText.setText("");
			registrationAuthorityText.setText("");
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){	
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
		}
	} //이벤트 처리기 함수 끝
	public void widgetDefaultSelected(SelectionEvent e){}
}