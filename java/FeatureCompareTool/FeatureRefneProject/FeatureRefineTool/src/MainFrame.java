import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StackLayout;

public class MainFrame {
	static Shell shell = null;
	static StackLayout stackLayout = null;
	static FeatureQueries featureQueries = null;

	//생성자
	public MainFrame(Display display){			
		this.initializeMember(display);							//멤버변수 초기화	
		new ToolMenu();										//메뉴 생성
		shell.setLayout(stackLayout);						//전체 레이아웃 지정
		shell.setSize(710, 530);							//셸 사이즈
		shell.setText("Feature Refinement Analyzer");		//타이틀 제목

		int x = (display.getBounds().width - shell.getSize().x) / 2;
		int y = (display.getBounds().height - shell.getSize().y) / 2;	
		shell.setLocation(x,y);								//셸 위치 지정
		shell.open(); 				

		while (!shell.isDisposed()){			//셸이 종료되지 않았으면
			if (!display.readAndDispatch()) {
				display.sleep();				//화면이 종료되지 않게 유지
			}
		}
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(Display display){
		shell = new Shell(display);
		featureQueries = new FeatureQueries();	//데이터베이스 연결
		stackLayout = new StackLayout();		//전체 레이아웃
	} //멤버 변수 초기화 함수 끝
	
	//Main
	public static void main(String[] args) {
		Display display = new Display();	//기본적인 디스플레이
		new MainFrame(display);
		display.dispose();					//셸이 종료되면, 남아 있는 디스플레이를 해제
	} //Main 함수 끝
}