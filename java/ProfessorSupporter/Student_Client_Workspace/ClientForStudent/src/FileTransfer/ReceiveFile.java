package FileTransfer;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/* 파일 받기 뷰 
 * 
 */

public class ReceiveFile extends JDialog implements ActionListener{

	private JProgressBar 	jb;
	private JPanel	jp1, jp2, jp3;	
	private BufferedWriter	bufWriter;	
	private Socket socket;
	private long fileLength;
	private String fileName;
	private JButton b, b1, b2;
	
	public ReceiveFile (JFrame frame, String addr){// 프레임과 상대측 아이피주소를 얻어 옴
		//부모프레임창, 제목, Modal여부 세팅
		super(frame, "파일 받기창", false);
				
		jp1	= new JPanel(new GridLayout(0, 1));
		jp2	= new JPanel(new FlowLayout());
		jp3	= new JPanel(new GridLayout(2, 0));
		
		jb = new JProgressBar(0, 100);
		
		jb.setStringPainted(true);
		jb.setString("대기중... 0%");
		b1 = new JButton("받기");
		b1.addActionListener(this);
		b = new JButton("취소");
		b.addActionListener(this);
		b2 = new JButton("닫기");
		b2.addActionListener(this);
		
		jp1.add(jb);
		jp2.add(b1);
		jp2.add(b);
		jp2.add(b2);
		jp3.add(jp1);	
		jp3.add(jp2);
		
		Container con = getContentPane();
		con.add(jp3);
		
		try{
			//소켓 생성 하고 서버소켓에 연결
			socket = new Socket(addr, 7777);
			
			BufferedReader	bufReader	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String msg = bufReader.readLine();
			
			String tmp[] = msg.split(",");
			
			fileName = tmp[0];	
			fileLength = Long.parseLong(tmp[1]);
			
			if(fileName.length() >= 20)
				
				jb.setString(fileName.substring(0, 20) + "...");
			
			else
				
				jb.setString(fileName);
			
		}
		
		catch(Exception e){ 
			
			System.out.println(e);
			e.printStackTrace();
			
		}
		
		this.addWindowListener(new WindowAdapter(){
			
     		public void windowClosing(WindowEvent we){		// 윈도 닫기시 파일 전송 실패
     		
    			try{	
    				
    				socket.close();
    				socket = null;
    				
    			}catch(Exception e){ 
    				
    				System.out.println(e);
    				e.printStackTrace();
    				
    			}
    			
    			jb.setEnabled(false);
    			
     		}
		});
					
		this.setSize(300, 100);				
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension di = tk.getScreenSize();
	    Dimension di1 = this.getSize();
	    //화면의 정중앙에 위치
	    this.setLocation((int)(di.getWidth()/2 - di1.getWidth()/2), (int)(di.getHeight()/2 - di1.getHeight()/2));
	    this.setVisible(true);
	    
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource() == b1){
			
			((JButton)ae.getSource()).setEnabled(false);
			
			try{
				
				bufWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));			
				bufWriter.write("Accept" + "\n");
				bufWriter.flush();
			
				Thread t = new Thread(){
					
					public void run(){
						
						getFile();
						
					}
				};
				
				t.start();
			
			}catch(Exception e){ 
				
				System.out.println(e);
				e.printStackTrace();
				
			}
			
		}
		
		else if(ae.getSource() == b){

			JOptionPane.showMessageDialog(this,	"파일보내기가 취소되었습니다!", "경고창", JOptionPane.ERROR_MESSAGE);
			
			try{
				
				jb.setString("전송취소!!!");
				jb.setEnabled(false);
				b2.setEnabled(true);
				b.setEnabled(false);
				b1.setEnabled(false);
				socket.close();
				socket = null;
				
			}catch(Exception e){ 
				
				System.out.println(e);
				e.printStackTrace();
				
			}	
			
		}else if(ae.getSource() == b2){
			
			int cc = JOptionPane.showConfirmDialog(this, "파일받기창을 닫겠습니까?", "확인창", JOptionPane.YES_NO_OPTION);
			
			if(cc == 0)
				
				this.dispose();
			
		}
		
	}
	
	void getFile(){	// 파일 받기 메소드
	
		try {
			
			File f = new File("c:/", fileName);	// 절대 경로로 무조건 c:/로 저장
			
			FileOutputStream out = new FileOutputStream(f);
			InputStream is = socket.getInputStream();
			
			int	i = 0;	// 스트림으로 부터 읽어 오는걸 임시로 담기 위한 변수
			long cnt = 0;	// 전송량 체크를 위한 변수
						
			while((i = is.read()) != -1){ // 파일 끝일때까지 일기 작업을 계속 합니다.
			
				out.write((char)i);
				
				cnt ++;
				
				int result = (int)(cnt / fileLength) ;
				
				if(result > jb.getValue()){
					jb.setValue(result);
					jb.setString("수신중...\t " + result + "%");
					b1.setEnabled(false);
					b2.setEnabled(false);
				}
			}
			
			b1.setEnabled(false);
			b.setEnabled(false);
			
			if((cnt / fileLength) < 100){	// 100 보다 작을 시엔 정상종료가 아니므로 실패를 그렇지않으면 성공.
				jb.setString("전송취소!!!");
				jb.setEnabled(false);
				b.setEnabled(false);
				b1.setEnabled(false);
				b2.setEnabled(true);
			}
			else{	
				jb.setValue(100);
				jb.setString("전송 완료!!! 100%");
				out.close();				
				JOptionPane.showMessageDialog(this,	"파일전송이 완료됐습니다!", "알림창", JOptionPane.INFORMATION_MESSAGE);				
				this.dispose();
			}
		}catch(Exception e){ 
			System.out.println(e);
			e.printStackTrace();	
		}	
	}
}