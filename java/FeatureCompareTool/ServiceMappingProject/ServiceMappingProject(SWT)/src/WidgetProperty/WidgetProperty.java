//위젯 배치를 위한 속성 클래스
package WidgetProperty;

import org.eclipse.swt.layout.GridData;

public class WidgetProperty {
	private static GridData gridData = null;

	//넓이
	public static GridData setGridLayoutProperty(int widthHint){
		gridData = new GridData();
		gridData.widthHint = widthHint;
		return gridData;
	}
	
	//수평 정렬 방법
	public static GridData setGridLayoutAlignProperty(int horizontalAlignment){
		gridData = new GridData();
		gridData.horizontalAlignment = horizontalAlignment;
		return gridData;
	}
	
	//수평 셀 합치기, 수평 정렬 방법
	public static GridData setGridLayoutNotGrabProperty(int horizontalSpan, int horizontalAlignment){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;						
		gridData.horizontalAlignment = horizontalAlignment;
		return gridData;
	}
	
	//수평 정렬 방법, 수직 정렬 방법
	public static GridData setGridLayoutNotGrabVerticalProperty(int horizontalSpan, int verticalAlignment){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;						
		gridData.verticalAlignment = verticalAlignment;
		return gridData;
	}
	
	//수평 정렬 방법, 수평 공간 활용
	public static GridData setGridLayoutProperty(int horizontalAlignment, boolean grabExcessHorizontalSpace){
		gridData = new GridData();
		gridData.horizontalAlignment = horizontalAlignment;
		gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		return gridData;
	}
	
	//수평 셀 합치기, 수평 정렬 방법, 수평 공간 활용
	public static GridData setGridLayoutProperty(int horizontalSpan, int horizontalAlignment, boolean grabExcessHorizontalSpace){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;						
		gridData.horizontalAlignment = horizontalAlignment;
		gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		return gridData;
	}
	
	//수평 셀 합치기, 넓이, 수평 정렬 방법, 수평 공간 활용
	public static GridData setGridLayoutProperty(int horizontalSpan, int widthHint, int horizontalAlignment, boolean grabExcessHorizontalSpace){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;
		gridData.widthHint = widthHint;
		gridData.horizontalAlignment = horizontalAlignment;
		gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		return gridData;
	}
	
	//수평 셀 합치기, 높이
	public static GridData setGridLayoutVerticalProperty(int horizontalSpan, int heightHint){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;
		gridData.heightHint = heightHint;
		return gridData;
	}
	
	//수평 셀 합치기, 높이, 수평 정렬 방법, 수평 공간 활용
	public static GridData setGridLayoutVerticalProperty(int horizontalSpan, int heightHint, int horizontalAlignment, boolean grabExcessHorizontalSpace){
		gridData = new GridData();
		gridData.horizontalSpan = horizontalSpan;
		gridData.heightHint = heightHint;
		gridData.horizontalAlignment = horizontalAlignment;
		gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		return gridData;
	}
}