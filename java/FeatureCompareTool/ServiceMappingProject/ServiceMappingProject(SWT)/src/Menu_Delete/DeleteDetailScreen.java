package Menu_Delete;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import Main.MainFrame;

public class DeleteDetailScreen extends Composite{
	static TabFolder tabFolder = null;
	private FillLayout fillLayout = null;	
	
	//생성자
	public DeleteDetailScreen(){
		super(MainFrame.shell, SWT.NONE);
		initializeMember();
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
		DeleteServiceForm deleteServiceForm = new DeleteServiceForm();
		deleteServiceForm.createWidgetValue(service, company);
		tabItem.setControl(deleteServiceForm);
	} //탭 아이템 생성 함수 끝
}