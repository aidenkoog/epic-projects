package MenuControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import DataBase.DBWork;
import Main.MainFrame;
import SimilarityCheck.SimilarityCheckScreen;
import WidgetProperty.WidgetProperty;

public class SelectServiceDialog{
	private Shell serviceSelectDialog = null;
	private GridLayout gridLayout = null;
	private Combo companyNameCombo = null;
	private Combo serviceNameCombo = null;
	private Button applyButton = null;
	private Button cancelButton = null;
	private Color labelColor = null;
	private Color buttonColor = null;

	//생성자
	public SelectServiceDialog(String titleName){
		this.initializeMember();
		this.serviceSelectDialog.setLayout(gridLayout);
		this.serviceSelectDialog.setText(titleName);
		this.createInputForm();
		this.setCombo();
		this.mountEventHandler();

		this.serviceSelectDialog.pack();	//다이얼로그 창 크기 조절
		this.serviceSelectDialog.open();	//다이얼로그 보여줌

		Rectangle shellBounds = MainFrame.shell.getBounds();	//셸의 사각형 정보
		Point dialogSize = this.serviceSelectDialog.getSize();	//다이얼로그 창의 크기 정보

		this.serviceSelectDialog.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2,	//셸 중앙으로 표시 
									         shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		serviceSelectDialog = new Shell(MainFrame.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);	//모달 다이얼로그
		gridLayout = new GridLayout(2, false);
		labelColor = new Color(MainFrame.shell.getDisplay(), 180,230,250);
		buttonColor = new Color(MainFrame.shell.getDisplay(), 70,210,210);
	} //멤버 변수 초기화 함수 끝
	
	//입력 양식 생성
	public void createInputForm(){
		Group formGroup = new Group(serviceSelectDialog, SWT.NONE);
		formGroup.setText("The Service's Name you wish to select");
		GridLayout gridLayout = new GridLayout(2, false);
		formGroup.setLayout(gridLayout);	
		formGroup.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(2, SWT.FILL));

		//회사
		Label companyNameLabel = new Label(formGroup, SWT.BORDER);	
		companyNameLabel.setBackground(labelColor);
		companyNameLabel.setText("Company Name:");
		companyNameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(92));
		companyNameCombo = new Combo(formGroup, SWT.BORDER);	
		companyNameCombo.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 90, SWT.FILL, true));

		//서비스
		Label serviceNameLabel = new Label(formGroup, SWT.BORDER);	
		serviceNameLabel.setBackground(labelColor);
		serviceNameLabel.setText("Service Name:");
		serviceNameLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(92));
		serviceNameCombo = new Combo(formGroup, SWT.BORDER);  
		serviceNameCombo.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 90, SWT.FILL, true));
		serviceNameCombo.setEnabled(false);

		Group buttonGroup = new Group(serviceSelectDialog, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		buttonGroup.setLayout(gridLayout);	
		buttonGroup.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(2, SWT.FILL));

		//적용 버튼
		applyButton = new Button(buttonGroup, SWT.NONE);	
		applyButton.setText("Apply");
		applyButton.setBackground(buttonColor);
		applyButton.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 90, SWT.CENTER, true));

		//취소 버튼
		cancelButton = new Button(buttonGroup, SWT.NONE);	
		cancelButton.setText("Cancel");
		cancelButton.setBackground(buttonColor);
		cancelButton.setLayoutData(WidgetProperty.setGridLayoutProperty(1, 90, SWT.CENTER, true));
	} //입력 양식 생성 함수 끝
	
	//목록 생성
	public void setCombo(){
		String[] companyList = DBWork.setCompanyNameComboList();	//회사 목록
		companyNameCombo.setItems(companyList);	
	} //목록 생성 함수 끝
	
	//이벤트 처리기
	public void mountEventHandler(){
		//적용 버튼
		applyButton.addSelectionListener(new SelectionAdapter(){	
			public void widgetSelected(SelectionEvent selectionEvent){	
				if(serviceNameCombo.getText().length() > 0 && companyNameCombo.getText().length() > 0){
					if((serviceSelectDialog.getText()).equals("First Service")){
						SimilarityCheckScreen.openFirstServiceText.setText(serviceNameCombo.getText());
						SimilarityCheckScreen.openFirstCompanyText.setText(companyNameCombo.getText());
					}else{
						SimilarityCheckScreen.openSecondServiceText.setText(serviceNameCombo.getText());
						SimilarityCheckScreen.openSecondCompanyText.setText(companyNameCombo.getText());
					}
					serviceSelectDialog.dispose();
				}else{
					MessageBox errorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
					errorMessageBox.setText("Failed");
					errorMessageBox.setMessage("Input Failed");
					errorMessageBox.open();
				}
			}
		});
		//취소 버튼
		cancelButton.addSelectionListener(new SelectionAdapter(){			
			public void widgetSelected(SelectionEvent selectionEvent){
				serviceSelectDialog.dispose();
			}
		});
		//회사 콤보
		companyNameCombo.addSelectionListener(new SelectionAdapter(){			
			public void widgetSelected(SelectionEvent selectionEvent){
				serviceNameCombo.setEnabled(true);
				String[] serviceList = DBWork.setParentServiceNameComboList(companyNameCombo.getText());
				serviceNameCombo.setItems(serviceList);
			}
		});
	} //이벤트 처리기 함수 끝
}