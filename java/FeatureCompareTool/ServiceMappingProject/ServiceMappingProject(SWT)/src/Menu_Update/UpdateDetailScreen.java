package Menu_Update;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import Main.MainFrame;

public class UpdateDetailScreen extends Composite{
	static TabFolder tabFolder = null;
	private FillLayout fillLayout = null;	
	
	//생성자
	public UpdateDetailScreen(){
		super(MainFrame.shell, SWT.NONE);
		this.initializeMember();
		this.setLayout(fillLayout);
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(){
		tabFolder = new TabFolder(this, SWT.TOP);
		fillLayout = new FillLayout();		
	} //멤버 변수 초기화 함수 끝
	
	//탭 아이템 생성
	public void createTabItem(String service, String company){
		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(service + "(" + company + ")");	//서비스(회사)
		UpdateServiceForm updateServiceForm = new UpdateServiceForm();
		updateServiceForm.createWidgetValue(service, company);
		tabItem.setControl(updateServiceForm);
	} //탭 아이템 생성 함수 끝
}