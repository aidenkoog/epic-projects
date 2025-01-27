package FileTransfer;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SendFile extends JDialog implements ActionListener{
	
	private JPanel jp1,jp2,jp3;	
	private Socket socket;
	private ServerSocket s;
	private File file;
	private JButton b1,b2;
	private JProgressBar jb;

	public SendFile (JFrame fa, File f){
		//속하는 프레임창, 제목, Modal여부 세팅
		super(fa, "파일 보내기창", false);
					
		jp1	= new JPanel(new GridLayout(0, 1));
		jp2	= new JPanel(new FlowLayout());
		jp3	= new JPanel(new GridLayout(2, 0));
		file = f;
		
		try{
			//서버 소켓 열고 대기
			s = new ServerSocket(7777);	
			//JProgressBar의 최대, 최소값 세팅
			jb = new JProgressBar(0, 100);
			//진행률을 보여줌
			jb.setStringPainted(true);
			jb.setString("대기중... 0%");
			b1 = new JButton("취소");
			b2 = new JButton("닫기");
			b1.addActionListener(this);
			b2.addActionListener(this);
			
			jp1.add(jb);
			jp2.add(b1);	
			jp2.add(b2);			
			jp3.add(jp1);
			jp3.add(jp2);
			
			Container con = getContentPane();
			con.add(jp3);
			
			this.addWindowListener(new WindowAdapter(){
				
	     		public void windowClosing(WindowEvent we){	// 닫기 버턴을 눌리게 되면 전송 중지를 한다.
	    			
	     			try{   
	     				
	    				socket.close();
		    			jb.setEnabled(false);
		    			s.close();
		    			s = null;
		    			
	    			}catch(Exception e){
	    				
	    				System.out.println(e);
	    		        e.printStackTrace();
	    				
	    			}
	    			
	     		}
			});
			
			Thread  t = new Thread(){
				
				public void run(){
					
					try{
						//소켓 연결~
						socket = s.accept();
						
						BufferedWriter	bufWriter 	= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						BufferedReader	bufReader	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						// 상대측에게 보내는 파일 정보를 전송한다.
						bufWriter.write(file.getName() + "," + (file.length() / 100) + "\n");
						bufWriter.flush();
						
						// 상대측에서 보내는 정보를 받아 수락인지 취소인지를 구별한다.
						String line = bufReader.readLine();
					
						if(line.equals("Accept")){
							
							Send();		// 수락일 경우 전송 을 시작한다.
							
						}else if(line.equals("Cancel")){	// 모든 작업을 종료한다.
						
							jb.setString("전송취소!!!");
							jb.setEnabled(false);						
							b2.setEnabled(true);
							b1.setEnabled(false);
							
							socket.close();
							s.close();
							
							socket	= null;
							s		= null;
							
							return;
						}
						
					}catch(Exception e){
						
						jb.setString("전송취소!!!");
						jb.setEnabled(false);
						b1.setEnabled(false);
						b2.setEnabled(true);
						
						try{
							
							socket.close();
							s.close();
							socket	= null;
							s = null;
							
						}catch(Exception ee){ 
							
							System.out.println(e);
							e.printStackTrace();
							
						}
						
					}
					
				}
			};
			
			t.start();		
			
		}catch(Exception e){	
			
			System.out.println(e);	
			jb.setString("전송취소!!!");
			jb.setEnabled(false);
			b1.setEnabled(false);
			b2.setEnabled(true);			
			socket	= null;
			s		= null;
			
			}
						
		this.setSize(300, 100);			
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension di = tk.getScreenSize();
	    Dimension di1 = this.getSize();
	    //화면의 정 가운데 위치시킴
	    this.setLocation((int)(di.getWidth()/2 - di1.getWidth()/2), (int)(di.getHeight()/2 - di1.getHeight()/2));
	    this.setVisible(true);
	    
	}
	
	public void Send(){
		
		try{
			
			DataInputStream	inData = new DataInputStream(new FileInputStream(file));
			DataOutputStream outData = new DataOutputStream(socket.getOutputStream());
			int	b = 0;
			int	cnt = 0;
		
			while((b = inData.read()) != -1){
				
				outData.writeByte(b);	// 파일에서 읽어와서 바이트 단위로  전송한다.
				outData.flush();
			
				cnt += 1;			//1/100만큼 보내면 cnt를 1증가
			
				int send = (int)(cnt / (file.length() / 100));//몇 %됐는지 세팅
			
				if(jb.isEnabled() == false){
					
					jb.setString("전송취소!!!");
					jb.setEnabled(false);
					b1.setEnabled(false);
					b2.setEnabled(true);
					throw	new Exception();
					
				}
			
				if(send > jb.getValue()){
					
					jb.setValue(send);
					jb.setString("전송중... \t" + send + "%");					
					this.b2.setEnabled(false);
					
				}
				
			}
			//전송완료
			jb.setValue(100);
			jb.setString("송신완료!!! 100%");
					
			this.b2.setEnabled(true);
			//소켓과 스트림을 다 닫아줌
			inData.close();
			outData.close();
			socket.close();
			s.close();
			//자원해제 하기 위해 주소 삭제
			inData = null; 
			outData = null;	
			socket = null;
			s = null;
			
			JOptionPane.showMessageDialog(this,	"파일전송이 완료됐습니다!", "알림창", JOptionPane.INFORMATION_MESSAGE);			
			this.dispose();
			
		}catch(Exception e) {	
			
			System.out.println(e);	
			jb.setString("전송취소!!!");
			jb.setEnabled(false);
			b1.setEnabled(false);
			b2.setEnabled(true);
			
			try{
				
				socket.close();
				s.close();
				socket = null;
				s = null;
				
			}catch(Exception ee){ 
				
				System.out.println(e);
				e.printStackTrace();
				
			}
						
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource() == b1){
			
			JOptionPane.showMessageDialog(this,	"파일보내기가 취소되었습니다!", "경고창", JOptionPane.ERROR_MESSAGE);			
			jb.setEnabled(false);
			b1.setEnabled(false);
			b2.setEnabled(true);
			
			try{
				
				socket.close();
				s.close();
				socket = null;
				s = null;
				
			}catch(Exception e){ 
				
				System.out.println(e);
				e.printStackTrace();
				
			}			
			
		}else if(ae.getSource() == b2){
			
			int cc = JOptionPane.showConfirmDialog(this, "파일보내기창을 닫겠습니까?", "확인창", JOptionPane.YES_NO_OPTION);
			
			if(cc == 0)
				
				this.dispose();
			
		}
	}	
}