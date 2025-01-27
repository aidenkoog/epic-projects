import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SearchFeatureScreen extends Composite{
	private GridLayout gridLayout = null;
	private Text nameText = null;
	private Text classificationText = null;
	private Text fTypeText = null;
	private Text synonymousNamesText = null;
	private Text priorityText = null;
	private Text conditionText = null;
	private Text definitionText = null;
	private Text servicePropertyText = null;
	private Text relationalPropertyText = null;
	private Text identifierText = null;
	private Text registrationAuthorityText = null;
	private Button reSearchButton = null;			
	private Button cancelButton = null;
	private Color labelColor = null;
	private Color buttonColor = null;
	private Font groupFont = null;

	//생성자
	public SearchFeatureScreen(){
		super(MainFrame.shell, SWT.NONE);
		this.initializeMember();
		this.setLayout(gridLayout);
		this.createIdentifyingAttributes();
		this.createSelectiveAttributes();		
		this.createRelationalAttributes();
		this.createManagingAttributes();
		this.createRegisterButton();	
		this.mountEventHandler();
	} //생성저 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		gridLayout = new GridLayout(1, false);
		labelColor = new Color(this.getParent().getDisplay(), 230,210,220);
		buttonColor = new Color(this.getParent().getDisplay(), 70,210,210);
		groupFont = new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD);
	} //멤버 변수 초기화 함수 끝
	
	//Identifying Attributes 생성
	public void createIdentifyingAttributes(){		
		Group identifyingAttributeGroup = new Group(this, SWT.NONE);
		identifyingAttributeGroup.setText("Identifying Attribute");	
		identifyingAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(12, false);
		identifyingAttributeGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		identifyingAttributeGroup.setLayoutData(gridData);

		Label nameLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Feature Name	
		nameLabel.setText("Name:");
		nameLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		nameLabel.setLayoutData(gridData);
		nameText = new Text(identifyingAttributeGroup, SWT.BORDER);
		nameText.setEnabled(false);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(gridData);		

		Label classificationLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Classification			
		classificationLabel.setText("Classification:");
		classificationLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		classificationLabel.setLayoutData(gridData);
		classificationText = new Text(identifyingAttributeGroup, SWT.BORDER);
		classificationText.setEnabled(false);
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
		fTypeText.setEnabled(false);
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
		synonymousNamesText.setEnabled(false);
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
		priorityText = new Text(identifyingAttributeGroup, SWT.BORDER);
		priorityText.setEnabled(false);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		priorityText.setLayoutData(gridData);

		Label conditionLabel = new Label(identifyingAttributeGroup, SWT.BORDER);	//Condition		
		conditionLabel.setText("Condition:");
		conditionLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		conditionLabel.setLayoutData(gridData);
		conditionText = new Text(identifyingAttributeGroup, SWT.BORDER);
		conditionText.setEnabled(false);
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
		definitionText = new Text(identifyingAttributeGroup, SWT.BORDER);
		definitionText.setEnabled(false);
		gridData = new GridData();
		gridData.horizontalSpan = 11;
		gridData.horizontalAlignment = SWT.FILL;
		definitionText.setLayoutData(gridData);
	} //Identifying Attributes 생성 함수 끝
	
	//Selective Attributes 생성
	public void createSelectiveAttributes(){
		Group selectiveAttributeGroup = new Group(this, SWT.NONE);
		selectiveAttributeGroup.setText("Selective Attribute");	
		selectiveAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		GridData gridData = new GridData();	
		selectiveAttributeGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		selectiveAttributeGroup.setLayoutData(gridData);

		Label servicePropertyLabel = new Label(selectiveAttributeGroup, SWT.BORDER);
		servicePropertyLabel.setText("Feature's Property:");
		servicePropertyLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		servicePropertyLabel.setLayoutData(gridData);
		servicePropertyText = new Text(selectiveAttributeGroup, SWT.BORDER);
		servicePropertyText.setEnabled(false);
		gridData = new GridData();
		gridData.horizontalSpan = 10;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		servicePropertyText.setLayoutData(gridData);
	} //Selective Attributes 생성 함수 끝
	
	//Relational Attributes 생성
	public void createRelationalAttributes(){
		Group relationalAttributeGroup = new Group(this, SWT.NONE);
		relationalAttributeGroup.setText("Relational Attribute");
		relationalAttributeGroup.setFont(groupFont);
		GridLayout gridLayout = new GridLayout(11, false);
		GridData gridData = new GridData();	
		relationalAttributeGroup.setLayout(gridLayout);		
		gridData.horizontalAlignment = SWT.FILL;
		relationalAttributeGroup.setLayoutData(gridData);

		Label composedOfLabel = new Label(relationalAttributeGroup, SWT.BORDER);	//ComposedOf		
		composedOfLabel.setText("Feature's Property:");
		composedOfLabel.setBackground(labelColor);
		gridData = new GridData();
		gridData.widthHint = 125;
		composedOfLabel.setLayoutData(gridData);
		relationalPropertyText = new Text(relationalAttributeGroup, SWT.BORDER);
		relationalPropertyText.setEnabled(false);
		gridData = new GridData();
		gridData.horizontalSpan = 10;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		relationalPropertyText.setLayoutData(gridData);
	} //Relational Attributes 생성 함수 끝
	
	//Managing Attributes 생성
	public void createManagingAttributes(){
		Group managingAttributeGroup = new Group(this, SWT.NONE);
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
		identifierText.setEnabled(false);
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
		registrationAuthorityText.setEnabled(false);
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

		reSearchButton = new Button(buttonGroup, SWT.PUSH);	//Register 버튼	
		reSearchButton.setBackground(buttonColor);
		reSearchButton.setText("Re-Search");
		gridData = new GridData();	
		gridData.widthHint = 80;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.grabExcessHorizontalSpace = true;
		reSearchButton.setLayoutData(gridData);

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
	
	//값 표시
	public void createWidgetValue(String featureName){
		String[] featurePropertyList = MainFrame.featureQueries.getPropertyByFeatureName(featureName);
		Text[] textWidget = {nameText, classificationText, fTypeText, synonymousNamesText, priorityText, conditionText, definitionText,
							 servicePropertyText, relationalPropertyText, identifierText, registrationAuthorityText};
		for(int i=0; i<textWidget.length; i++){
			textWidget[i].setText(featurePropertyList[i]);
		}
	} //값 표시 함수 끝
	
	//이벤트 처리기
	public void mountEventHandler(){
		//다시 찾기
		reSearchButton.addSelectionListener(new SelectionAdapter(){			
			public void widgetSelected(SelectionEvent selectionEvent){
				new SearchFeatureDialog();
			}
		});
		//취소
		cancelButton.addSelectionListener(new SelectionAdapter(){			
			public void widgetSelected(SelectionEvent selectionEvent){
				MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
				MainFrame.shell.layout();
			}
		});
	} //이벤트 처리기 함수 끝
}