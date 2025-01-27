import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SelectFeatureDialog{
	private Shell featureSelectDialog = null;
	private Combo featureNameCombo = null;
	private GridLayout gridLayout = null;
	private Button applyButton = null;
	private Button cancelButton = null;
	private Color labelColor = null;
	private Color buttonColor = null;

	//생성자
	public SelectFeatureDialog(String titleName){
		this.initializeMember();
		this.createInputForm();
		this.setFeatureNameCombo();
		this.mountEventHandler();
		featureSelectDialog.setLayout(gridLayout);
		featureSelectDialog.setText(titleName);

		featureSelectDialog.pack();	//다이얼로그 창 크기 조절
		featureSelectDialog.open();	//다이얼로그 보여줌

		Rectangle shellBounds = MainFrame.shell.getBounds();	//셸의 사각형 정보
		Point dialogSize = featureSelectDialog.getSize();		//다이얼로그 창의 크기 정보

		featureSelectDialog.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,		//셸 중앙으로 표시 
				shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		featureSelectDialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);	//모달 다이얼로그
		gridLayout = new GridLayout(2, false);
		labelColor = new Color(MainFrame.shell.getDisplay(), 180,250,210);
		buttonColor = new Color(MainFrame.shell.getDisplay(), 70,210,210);
	} //멤버 변수 초기화 함수 끝
	
	//입력 양식 생성
	public void createInputForm(){
		Group formGroup = new Group(featureSelectDialog, SWT.NONE);
		formGroup.setText("The Feature's Name you wish to select");
		GridLayout gridLayout = new GridLayout(2, false);
		GridData gridData = new GridData();	
		formGroup.setLayout(gridLayout);	
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		formGroup.setLayoutData(gridData);
		
		Label featureNameLabel = new Label(formGroup, SWT.BORDER);	//휘처
		featureNameLabel.setBackground(labelColor);
		featureNameLabel.setText("Feature Name:");
		gridData = new GridData();
		gridData.widthHint = 90;
		featureNameLabel.setLayoutData(gridData);
		featureNameCombo = new Combo(formGroup, SWT.BORDER);
		featureNameCombo.setText("Drafting");
		gridData = new GridData();
		gridData.widthHint = 90;
		gridData.horizontalAlignment = SWT.FILL;				
		gridData.grabExcessHorizontalSpace = true;
		featureNameCombo.setLayoutData(gridData);
		
		Group buttonGroup = new Group(featureSelectDialog, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridData = new GridData();	
		buttonGroup.setLayout(gridLayout);	
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		buttonGroup.setLayoutData(gridData);

		applyButton = new Button(buttonGroup, SWT.NONE);	//적용 버튼
		applyButton.setText("Apply");
		applyButton.setBackground(buttonColor);
		gridData = new GridData();
		gridData.widthHint = 90;
		gridData.horizontalAlignment = SWT.CENTER;				
		gridData.grabExcessHorizontalSpace = true;
		applyButton.setLayoutData(gridData);

		cancelButton = new Button(buttonGroup, SWT.NONE);	//취소 버튼
		cancelButton.setText("Cancel");
		cancelButton.setBackground(buttonColor);
		gridData = new GridData();
		gridData.widthHint = 90;
		gridData.horizontalAlignment = SWT.CENTER;				
		gridData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(gridData);
	} //입력 양식 생성 함수 끝
	
	//휘처 목록 생성
	public void setFeatureNameCombo(){
		String[] featureList = MainFrame.featureQueries.getFeatureList();
		featureNameCombo.setItems(featureList);	
	} //휘처 목록 생성 함수 끝
	
	//이벤트 처리기
	public void mountEventHandler(){
		//적용
		applyButton.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent selectionEvent){
				if((featureSelectDialog.getText()).equals("First Feature")){
					AnalyzeFeatureScreen.openFirstFeatureText.setText(featureNameCombo.getText());
				}else{
					AnalyzeFeatureScreen.openSecondFeatureText.setText(featureNameCombo.getText());
				}
				featureSelectDialog.dispose();
			}
		});
		//취소
		cancelButton.addSelectionListener(new SelectionAdapter(){		
			public void widgetSelected(SelectionEvent selectionEvent){
				featureSelectDialog.dispose();
			}
		});
	} //이벤트 처리기 함수 끝
}