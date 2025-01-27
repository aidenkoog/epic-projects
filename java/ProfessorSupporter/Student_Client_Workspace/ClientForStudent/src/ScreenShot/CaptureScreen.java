package ScreenShot;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

//
public class CaptureScreen extends Thread{//이미지 비교하는 클래스
	protected BufferedImage img1 = null;
	protected BufferedImage img2 = null;
	protected int dx = 8;  //이미지를 나눌 가로 크기
	protected int dy = 6;  //이미지를 나눌 세로 크기
	protected int parameter1 = 50;  //민감도 5
	protected int parameter2 = 10;
	protected boolean match = true;

	public CaptureScreen() {//생성자
	}
	
	public void compare() {//비교 함수
		//img1 = ToBufImage(GrayFilter.createDisabledImage(img1));	//그레이필터씌움
		//img2 = ToBufImage(GrayFilter.createDisabledImage(img2));

		//이미지를 나눈 가로 길이
		int bx = (int)(img1.getWidth() / dx);  //dx(x조각 너비)로 나눈 개수
		//세로길이
		int by = (int)(img1.getHeight() / dy);  //dy(y조각 너비)로 나눈 개수

		System.out.println("start");  //시작(디버깅용)

		for (int y = 0; y < dy; y++) { //모든 블럭 검사
			for (int x = 0; x < dx; x++) {
				int b1 = Bright(img1.getSubimage(x*bx, y*by, bx - 1, by - 1));
				int b2 = Bright(img2.getSubimage(x*bx, y*by, bx - 1, by - 1));
				int diff = Math.abs(b1 - b2);  //이미지 블럭이 서로 다르면
				if (diff > parameter1) {   		//다른게 민감도 이상일때
					this.match = false;  		//false로 바꾼다.
				}
			}
		}
		System.out.println("end");  //끝(디버깅용)
	}

	protected int Bright(BufferedImage img) {   //그레이 필터 씌워서 밝기 비교
		Raster r = img.getData();
		int total = 0;
		for (int y = 0; y < r.getHeight(); y++) { //블럭 하나에 대하여
			for (int x = 0; x < r.getWidth(); x++) {
				total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0); //샘플채취
			}
		}
		return (int)(total / ((r.getWidth()/parameter2)*(r.getHeight()/parameter2)));  //
	}

	/////////////////////////////////////////////////////////////////////////////////
	//스크린 캡쳐
	private BufferedImage captureScreen(){
		Robot robot = null;
		Rectangle area = null;
		BufferedImage bufImage = null;
		try{
			robot = new Robot();
			area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			bufImage = robot.createScreenCapture(area);
		}catch(Exception e){
			e.printStackTrace();
		}
		return bufImage;
	}
	/////////////////////////////////////////////////////////////////////////////////
	
	public void run(){
		System.out.println("스레드 진입");
		try{
			while(true){
				match = true;
				img1 = captureScreen();	//1. 찍고 
				System.out.println("1.찍고");
				Thread.sleep(3000);		//2. 5초후
				System.out.println("2. 휴식 후");
				img2 = captureScreen();	//3. 두번째 사진 찍고
				System.out.println("3. 또 찍고");
				compare();				//4. 비교
				System.out.println("4. 비교 완료 후");
				System.out.println("5. 일치 결과 : " + match); //5. 결과 표시

				if(match == false){
					new ImageClient("ScreenShot");
				}
			}
		}catch(Exception e){
			System.out.println("쓰레드 에러");
		}
	}
	
	//이미지 사이즈 처리
	private BufferedImage createJustScaledImage(BufferedImage image, int targetSize) throws Exception{
		double resizeRatio = 1.0;
		if(image.getWidth() > image.getHeight()){
			resizeRatio = (double)targetSize / image.getWidth();
		}
		else{
			resizeRatio = (double)targetSize / image.getHeight();
		}

		if(resizeRatio > 1){
			throw new Exception("Error");
		}

		int width = (int)(image.getWidth() * resizeRatio);
		int height = (int)(image.getHeight() * resizeRatio);

		int type = (image.getTransparency() == Transparency.OPAQUE)? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB; 
		BufferedImage bufferedImage = new BufferedImage(width, height, type);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		AffineTransform xform = AffineTransform.getScaleInstance(resizeRatio,  resizeRatio);
		g2.drawRenderedImage((RenderedImage)image, xform);

		g2.dispose();

		return bufferedImage;
	}
}