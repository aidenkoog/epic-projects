package SimilarityCheck;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import Main.MainFrame;
import MenuControl.SelectServiceDialog;
import WidgetProperty.WidgetProperty;

public class SimilarityCheckScreen extends Composite implements SelectionListener{
	private GridLayout gridLayout = null;
	private Color labelColor = null;
	private Color buttonColor = null;
	private Font groupFont = null;
	private Font buttonFont = null;
	public static Text openFirstCompanyText = null;
	public static Text openFirstServiceText = null;
	public static Text openSecondCompanyText = null;
	public static Text openSecondServiceText = null;
	private Button openFirstServiceButton = null;	
	private Button openSecondServiceButton = null;
	private Button analyzeButton = null;
	private Button initialButton = null;
	private Button cancelButton = null;
	private Group serviceAnalysisResultGroup = null;
	private Button selfRadioButton = null;
	private Button allServiceRadioButton = null;
	private Button functionBasedCheckButton = null; 
	private Button allCheckButton = null;
	private Group canvasGroup = null;
	private Group detailGroupFunctionBoundary = null;
	private Group detailGroupTotalSelf = null;
	private Group detailGroupTotalBoundary = null;
	private Group resultGroup = null;
	private Label hitRatioLabel = null;
	private Label hitRatioValueLabel = null;
	private Label similarityLevelLabel = null;
	private Label similarityLevelValueLabel = null;
	private Label explainSimilarityLabel = null;
	
	//생성자
	public SimilarityCheckScreen(){
		super(MainFrame.shell, SWT.NONE);
		this.initializeMember();
		this.setLayout(gridLayout);
		this.createInputFeatureForm();
		this.createServiceAnalysisResult();
		this.createButton();
		this.mountEventHandler();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		gridLayout = new GridLayout(1, false);
		labelColor = new Color(this.getParent().getDisplay(), 180,230,240);
		buttonColor = new Color(this.getParent().getDisplay(), 70,210,210);
		groupFont = new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD);
		buttonFont = new Font(this.getParent().getDisplay(), "Miriam", 10, SWT.BOLD);
	} //멤버 변수 초기화 함수 끝
	
	//휘처 입력 폼
	public void createInputFeatureForm(){
		Group inputServiceGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		inputServiceGroup.setText("Input Service");		 
		inputServiceGroup.setFont(groupFont);
		gridLayout = new GridLayout(7, false);
		inputServiceGroup.setLayout(gridLayout);		
		inputServiceGroup.setLayoutData(WidgetProperty.setGridLayoutProperty(SWT.FILL, true));

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		Label openFirstCompanyLabel = new Label(inputServiceGroup, SWT.BORDER);	//첫번째 회사 열기
		openFirstCompanyLabel.setText("First Company:");		
		openFirstCompanyLabel.setBackground(labelColor);
		openFirstCompanyLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(100));
		openFirstCompanyText = new Text(inputServiceGroup, SWT.BORDER);
		openFirstCompanyText.setEnabled(false);
		openFirstCompanyText.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 130, SWT.FILL, true));
		
		Label openFirstServiceLabel = new Label(inputServiceGroup, SWT.BORDER);	//첫번째 서비스 열기
		openFirstServiceLabel.setText("First Service:");		
		openFirstServiceLabel.setBackground(labelColor);
		openFirstServiceLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(90));
		openFirstServiceText = new Text(inputServiceGroup, SWT.BORDER); 
		openFirstServiceText.setEnabled(false);
		openFirstServiceText.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 170, SWT.FILL, true));
		
		openFirstServiceButton = new Button(inputServiceGroup, SWT.PUSH);	//GetService 버튼
		openFirstServiceButton.setText("Get Service");
		openFirstServiceButton.setBackground(buttonColor);
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		Label openSecondCompanyLabel = new Label(inputServiceGroup, SWT.BORDER);	//두번째 회사 열기
		openSecondCompanyLabel.setText("Second Company:");		
		openSecondCompanyLabel.setBackground(labelColor);
		openSecondCompanyLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(100));
		openSecondCompanyText = new Text(inputServiceGroup, SWT.BORDER);
		openSecondCompanyText.setEnabled(false);
		openSecondCompanyText.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 130, SWT.FILL, true));
		
		Label openSecondServiceLabel = new Label(inputServiceGroup, SWT.BORDER);	//두번째 서비스 열기
		openSecondServiceLabel.setText("Second Service:");		
		openSecondServiceLabel.setBackground(labelColor);
		openSecondServiceLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(90));
		openSecondServiceText = new Text(inputServiceGroup, SWT.BORDER);
		openSecondServiceText.setEnabled(false);
		openSecondServiceText.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 170, SWT.FILL, true));
		
		openSecondServiceButton = new Button(inputServiceGroup, SWT.NONE);	//GetService 버튼
		openSecondServiceButton.setText("Get Service");
		openSecondServiceButton.setBackground(buttonColor);
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		Label optionLabel = new Label(inputServiceGroup, SWT.BORDER);	//옵션
		optionLabel.setText("Analysis Option:");	
		optionLabel.setBackground(labelColor);
		optionLabel.setLayoutData(WidgetProperty.setGridLayoutProperty(100));
		
		selfRadioButton = new Button(inputServiceGroup, SWT.RADIO);	//자기 자신의 서비스만		
		selfRadioButton.setText("Semantic Self Similarity");
		selfRadioButton.setSelection(true);
		selfRadioButton.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(2, SWT.LEFT));
				
		allServiceRadioButton = new Button(inputServiceGroup, SWT.RADIO);	//서비스 전체
		allServiceRadioButton.setText("Semantic Boundary Similarity");
		allServiceRadioButton.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(4, SWT.LEFT));
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		Label label = new Label(inputServiceGroup, SWT.BORDER);	//디스플레이 모드
		label.setText("Display Mode:");	
		label.setBackground(labelColor);
		label.setLayoutData(WidgetProperty.setGridLayoutProperty(100));
		
		functionBasedCheckButton = new Button(inputServiceGroup, SWT.CHECK);	//기능 기반	
		functionBasedCheckButton.setText("Function based");
		functionBasedCheckButton.setSelection(true);
		functionBasedCheckButton.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(2, SWT.LEFT));
				
		allCheckButton = new Button(inputServiceGroup, SWT.CHECK);	//전체
		allCheckButton.setText("Total property based");
		allCheckButton.setLayoutData(WidgetProperty.setGridLayoutNotGrabProperty(2, SWT.LEFT));
		
		analyzeButton = new Button(inputServiceGroup, SWT.PUSH);	//분석 버튼
		analyzeButton.setText("Analyze");
		analyzeButton.setBackground(new Color(this.getParent().getDisplay(), 255,120,120));
		analyzeButton.setFont(buttonFont);
		analyzeButton.setLayoutData(WidgetProperty.setGridLayoutProperty(2, 73, SWT.RIGHT, false));
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////
		//검사용
		openFirstServiceText.setText("Order service");
		openFirstCompanyText.setText("a_company");
		openSecondServiceText.setText("Customer order service");
		openSecondCompanyText.setText("b_company");
		/////////////////////////////////////////////////////////
	} //휘처 입력 폼 함수 끝
	
	//휘처 진단 결과 표시
	public void createServiceAnalysisResult(){
		serviceAnalysisResultGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		serviceAnalysisResultGroup.setText("Analyzed Result");
		serviceAnalysisResultGroup.setFont(groupFont);
		serviceAnalysisResultGroup.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		serviceAnalysisResultGroup.setLayoutData(gridData);
		
		//왼쪽 그룹(텍스트 표시용)
		Group textResultGroup = new Group(serviceAnalysisResultGroup, SWT.SHADOW_ETCHED_IN);
		textResultGroup.setLayout(new GridLayout(1, false));
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		textResultGroup.setLayoutData(gridData);
		
		//최종 적중율 결과 그룹
		resultGroup = new Group(textResultGroup, SWT.SHADOW_ETCHED_IN);
		resultGroup.setText("▶Result For Analysis");
		resultGroup.setFont(new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD));
		gridLayout = new GridLayout(2, false); 
		resultGroup.setLayout(gridLayout);
		gridData = new GridData();
		gridData.widthHint = 340;
		gridData.verticalIndent = 25;
		gridData.horizontalIndent = 5;
		resultGroup.setLayoutData(gridData);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		//아직 사용하지 않음
		//기능기반 + Boundary 그룹
		detailGroupFunctionBoundary = new Group(textResultGroup, SWT.SHADOW_ETCHED_IN);
		detailGroupFunctionBoundary.setText("▶The results of each service");
		detailGroupFunctionBoundary.setFont(new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD));
		gridLayout = new GridLayout(1, false); 
		detailGroupFunctionBoundary.setLayout(gridLayout);
		gridData = new GridData();
		gridData.widthHint = 0;
		gridData.verticalIndent = 0;
		gridData.horizontalIndent = 5;
		detailGroupFunctionBoundary.setLayoutData(gridData);

		//전체속성기반 + Self 그룹
		detailGroupTotalSelf = new Group(textResultGroup, SWT.SHADOW_ETCHED_IN);
		detailGroupTotalSelf.setText("▶The results of each service");
		detailGroupTotalSelf.setFont(new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD));
		gridLayout = new GridLayout(1, false); 
		detailGroupTotalSelf.setLayout(gridLayout);
		gridData = new GridData();
		gridData.widthHint = 0;
		gridData.verticalIndent = 0;
		gridData.horizontalIndent = 5;
		detailGroupTotalSelf.setLayoutData(gridData);

		//전체속성기반 + Boundary 그룹
		detailGroupTotalBoundary = new Group(textResultGroup, SWT.SHADOW_ETCHED_IN);
		detailGroupTotalBoundary.setText("▶The results of each service");
		detailGroupTotalBoundary.setFont(new Font(this.getParent().getDisplay(), "Miriam", 11, SWT.BOLD));
		gridLayout = new GridLayout(1, false); 
		detailGroupTotalBoundary.setLayout(gridLayout);
		gridData = new GridData();
		gridData.widthHint = 0;
		gridData.verticalIndent = 0;
		gridData.horizontalIndent = 5;
		detailGroupTotalBoundary.setLayoutData(gridData);
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//적중율 표시
		hitRatioLabel = new Label(resultGroup, SWT.NONE);
		hitRatioLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 12, SWT.NONE));
		gridData = new GridData();
		gridData.horizontalIndent = 10;
		gridData.widthHint = 150;
		hitRatioLabel.setLayoutData(gridData);
		
		hitRatioValueLabel = new Label(resultGroup, SWT.NONE);
		hitRatioValueLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 14, SWT.BOLD));
		gridData = new GridData();
		gridData.widthHint = 70;
		hitRatioValueLabel.setLayoutData(gridData);
		
		//유사성 레벨 표시
		similarityLevelLabel = new Label(resultGroup, SWT.NONE);
		similarityLevelLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 12, SWT.NONE));
		gridData = new GridData();
		gridData.horizontalIndent = 10;
		gridData.widthHint = 150;
		similarityLevelLabel.setLayoutData(gridData);
		
		similarityLevelValueLabel = new Label(resultGroup, SWT.NONE);
		similarityLevelValueLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 14, SWT.BOLD));
		gridData = new GridData();
		gridData.widthHint = 70;
		similarityLevelValueLabel.setLayoutData(gridData);
		
		//유사성 정도 설명
		explainSimilarityLabel = new Label(resultGroup, SWT.NONE);
		explainSimilarityLabel.setFont(new Font(this.getParent().getDisplay(), "Miriam", 15, SWT.BOLD));
		explainSimilarityLabel.setForeground(new Color(this.getParent().getDisplay(), 255,0,0));
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalIndent = 24;
		gridData.verticalIndent = 5;
		gridData.widthHint = 250;
		explainSimilarityLabel.setLayoutData(gridData);
		
		//오른쪽 그룹(그래프 표시용)
		canvasGroup = new Group(serviceAnalysisResultGroup, SWT.SHADOW_ETCHED_IN);
		canvasGroup.setLayout(new GridLayout(1, false));
		gridData = new GridData();
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		canvasGroup.setLayoutData(gridData);
		
		//각각 결과 표시를 위한 그룹들 안보이게 설정
		detailGroupFunctionBoundary.setVisible(false);
		detailGroupTotalSelf.setVisible(false);
		detailGroupTotalBoundary.setVisible(false);
		resultGroup.setVisible(false);
	} //휘처 진단 결과 표시 함수 끝
	
	//버튼 생성
	public void createButton(){
		Group buttonGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		GridLayout gridLayout = new GridLayout(4, false);
		buttonGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		buttonGroup.setLayoutData(gridData);
		
		initialButton = new Button(buttonGroup, SWT.PUSH);	//초기화 버튼
		initialButton.setBackground(buttonColor);
		initialButton.setText("Initialize");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.grabExcessHorizontalSpace = true;
		initialButton.setLayoutData(gridData);

		cancelButton = new Button(buttonGroup, SWT.PUSH);	//취소 버튼
		cancelButton.setBackground(buttonColor);
		cancelButton.setText("Cancel");	
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(gridData);
	} //버튼 생성 함수 끝
	
	//맵핑 결과 표시
	public void setMappingResult(String hitRatio, String similarityLevel, String explainSimilarityLevel, String displayMode, 
								 boolean boundaryMode){
		//기능 기반 모드 + Self 비교
		if(displayMode.equals("Function based") && boundaryMode == false){	
//			detailGroupFunctionBoundary.setVisible(false);
//			detailGroupTotalSelf.setVisible(false);
//			detailGroupTotalBoundary.setVisible(false);
			resultGroup.setVisible(true);
		}
		//기능 기반 모드 + Boundary 비교
		if(displayMode.equals("Function based") && boundaryMode == true){
//			detailGroupTotalSelf.setVisible(false);
//			detailGroupTotalBoundary.setVisible(false);
//			detailGroupFunctionBoundary.setVisible(true);
			resultGroup.setVisible(true);
		}
		//전체 속성 기반 모드 + Self 비교
		if(displayMode.equals("Total property based") && boundaryMode == false){	
//			detailGroupFunctionBoundary.setVisible(false);
//			detailGroupTotalBoundary.setVisible(false);
//			detailGroupTotalSelf.setVisible(true);
			resultGroup.setVisible(true);
		}
		//전체 속성 기반 모드 + Boundary 비교
		if(displayMode.equals("Total property based") && boundaryMode == true){
//			detailGroupFunctionBoundary.setVisible(false);
//			detailGroupTotalSelf.setVisible(false);
//			detailGroupTotalBoundary.setVisible(true);
			resultGroup.setVisible(true);
		}
		
		hitRatioLabel.setText("▷ Hit Ratio(%) : ");
		similarityLevelLabel.setText("▷ Similarity Level : ");
		
		hitRatioValueLabel.setText(hitRatio+"%");
		similarityLevelValueLabel.setText(similarityLevel);
		explainSimilarityLabel.setText(explainSimilarityLevel);
		
		//적중률 int로 변환
		double hitRatioDouble = Double.parseDouble(hitRatio);
		final int hitRatioInt = (int)hitRatioDouble;
		
		//그래프 그리기
		canvasGroup.addPaintListener(new PaintListener() { 
			public void paintControl(PaintEvent e) { 
				//원그래프
				int commonAngle = (int)((360*hitRatioInt)/100);
				int variAngle = -(int)(360-commonAngle);

				//색 안내 스트링
				e.gc.setForeground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				e.gc.setFont(new Font(SimilarityCheckScreen.this.getParent().getDisplay(), "Miriam", 10, SWT.BOLD));
				e.gc.drawText("Similarity", 90, 33, true);
				e.gc.drawText("Variability", 90, 53, true);

				e.gc.setForeground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				e.gc.drawText("0 / 100", 260, 195, true);
				e.gc.drawLine(250, 200, 255, 200);
				
				e.gc.setForeground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				e.gc.drawText("25(%)", 155, 90, true);
				e.gc.drawLine(160, 110, 160, 105);

				e.gc.setForeground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				e.gc.drawText("50", 45, 195, true);
				e.gc.drawLine(65, 200, 70, 200);

				e.gc.setForeground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				e.gc.drawText("75", 155, 300, true);
				e.gc.drawLine(160, 290, 160, 295);

				//색 안내 표시
				e.gc.setBackground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				e.gc.fillRectangle(20, 35, 60, 10);

				e.gc.setBackground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_RED));
				e.gc.fillRectangle(20, 55, 60, 10);

				//유사성, 비유사성 보이기
				e.gc.setBackground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				e.gc.fillArc(70, 110, 180, 180, 0, commonAngle);

				e.gc.setBackground(MainFrame.shell.getDisplay().getSystemColor(SWT.COLOR_RED));
				e.gc.fillArc(70, 110, 180, 180, 360, variAngle);
			} 
		}); 
		canvasGroup.redraw();
	} //맵핑 결과 표시 함수 끝
	
	//이벤트 처리기 등록
	public void mountEventHandler(){
		openFirstServiceButton.addSelectionListener(this);
		openSecondServiceButton.addSelectionListener(this);
		analyzeButton.addSelectionListener(this);
		initialButton.addSelectionListener(this);
		cancelButton.addSelectionListener(this);
		functionBasedCheckButton.addSelectionListener(this);
		allCheckButton.addSelectionListener(this);
	} //이벤트 처리기 등록 함수 끝
	
	//이벤트 처리기
	public void widgetSelected(SelectionEvent e){
		//첫번째 서비스 열기 버튼
		if(e.getSource() == openFirstServiceButton){		
			new SelectServiceDialog("First Service");
		}
		//두번째 서비스 열기 버튼
		else if(e.getSource() == openSecondServiceButton){	
			new SelectServiceDialog("Second Service");
		}
		//분석버튼
		else if(e.getSource() == analyzeButton){	
			if(openFirstServiceText.getText().length() > 0 && openSecondServiceText.getText().length() > 0){
				
				//분석 모드 설정
				String analysisOption = null;
				if(selfRadioButton.getSelection()){		
					analysisOption = selfRadioButton.getText();			//Self
				}else{
					analysisOption = allServiceRadioButton.getText();	//All Services
				}
				
				//디스플레이 모드 설정
				String displayMode = null;
				if(functionBasedCheckButton.getSelection()){
					displayMode = functionBasedCheckButton.getText();
				}
				else if(allCheckButton.getSelection()){
					displayMode = allCheckButton.getText();
				}
				//맵핑 시작
				new MappingServices(openFirstCompanyText.getText(), openSecondCompanyText.getText(),
									openFirstServiceText.getText(), openSecondServiceText.getText(), 
									analysisOption, displayMode, this);
			}else{
				MessageBox errorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
				errorMessageBox.setText("Error");
				errorMessageBox.setMessage("Enter the name of the service properly");
				errorMessageBox.open();	
			}
		}
		//초기화 버튼
		else if(e.getSource() == initialButton){	
			
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){	
			MainFrame.stackLayout.topControl = new Composite(MainFrame.shell, SWT.NONE);
			MainFrame.shell.layout();
		}
		//기능 기반 버튼 클릭 시
		else if(e.getSource() == functionBasedCheckButton){
			functionBasedCheckButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					allCheckButton.setSelection(false);
				}
			});
		}
		//전체 속성 버튼 클릭 시
		else if(e.getSource() == allCheckButton){
			allCheckButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					functionBasedCheckButton.setSelection(false);
				}
			});
		}
	} //이벤트 처리기 함수 끝
	public void widgetDefaultSelected(SelectionEvent e){}
}