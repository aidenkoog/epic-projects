//A사 Component 속성란에 (M/V/C pattern base) 제거함.
package SimilarityCheck;
import java.util.ArrayList;

import DataBase.DBWork;

public class MappingServices {
	private String firstCompanyName = null;
	private String secondCompanyName = null;
	private String firstServiceName = null;
	private String secondServiceName = null;
	private String analysisOption = null;
	private String displayMode = null;
	private SimilarityCheckScreen similarityCheckScreen = null;
	private double totalPercentage = 0;
	private ArrayList<Double> percentageArray = null;
	private boolean boundaryMode = false;
	private String similarityLevel = null;			//유사성 레벨
	private String explainSimilarityLevel = null;	//유사성 정도의 설명
	private double sumOfArrayListValue = 0.0;		//ArrayList안의 요소들의 합을 위한 변수

	//생성자
	public MappingServices(String firstCompanyName, String secondCompanyName, String firstServiceName, String secondServiceName, 
						   String analysisOption, String displayMode, SimilarityCheckScreen similarityCheckScreen){
		//맴버 변수 초기화 함수 호출
		this.initializeMember(firstCompanyName, secondCompanyName, firstServiceName, secondServiceName, 
							  analysisOption, displayMode, similarityCheckScreen);
		//서비스 맵핑 처리 호출
		this.processMapping();
	} //생성자 끝

	//멤버 변수 초기화
	public void initializeMember(String firstCompanyName, String secondCompanyName, String firstServiceName, String secondServiceName, 
								 String analysisOption, String displayMode, SimilarityCheckScreen similarityCheckScreen){
		this.firstCompanyName = firstCompanyName;			//첫번째 서비스의 회사 이름
		this.secondCompanyName = secondCompanyName;			//두번째 서비스의 회사 이름
		this.firstServiceName = firstServiceName;			//첫번째 서비스 이름
		this.secondServiceName = secondServiceName;			//두번째 서비스 이름
		this.analysisOption = analysisOption;				//분석 옵션
		this.displayMode = displayMode;						//디스플레이 모드
		this.similarityCheckScreen = similarityCheckScreen;	
		this.percentageArray = new ArrayList<Double>();	//Pre-Post-Self 퍼센티지를 담기 위한 변수
	} //멤버 변수 초기화 함수 끝

	//서비스 맵핑 처리
	public void processMapping(){
		//첫번째 서비스 속성 리스트
		String[] firstServicePropertyList = DBWork.getPropertyByServiceName(firstCompanyName, firstServiceName);
		//두번째 서비스 속성 리스트
		String[] secondServicePropertyList = DBWork.getPropertyByServiceName(secondCompanyName, secondServiceName);
		//속성의 개수
		int lengthOfProperty = firstServicePropertyList.length;

		//셀프 서비스들 간의 맵핑
		if(analysisOption.equals("Semantic Self Similarity")){	
			//Boundary 모드를 켬
			boundaryMode = false;
			
			//디스플레이 모드가 기능 기반일 경우
			if(displayMode.equals("Function based")){
				//Functional 속성
				mappingFunctionalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
			}
			//디스플레이 모드가 전체 기반일 경우
			else{
				//Identidying 속성
				mappingIdentifyingProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
				//Functional 속성
				mappingFunctionalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
				//Selective 속성
				mappingSelectiveProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
				//Relational 속성
				mappingRelationalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
			}
			//ArrayList안의 값 가져오기
			for(int j=0; j<percentageArray.size(); j++){
				sumOfArrayListValue += percentageArray.get(j);
			}
			String hitRatio = String.format("%.2f", sumOfArrayListValue);	//소수점 2자리까지 표현
			measureSimilarityLevel(sumOfArrayListValue);					//유사성 레벨 측정(High, Middle, Low)
			
			//맵핑 결과를 화면에 표시
			similarityCheckScreen.setMappingResult(hitRatio, similarityLevel, explainSimilarityLevel, displayMode, boundaryMode);
			
			//결과값 변수 초기화
			initializeResultValue();										
		} //셀프 서비스들 간의 맵핑 끝
		
		//전체(Pre-Self-Post)서비스들 간의 맵핑
		else if(analysisOption.equals("Semantic Boundary Similarity")){
			//Boundary 모드를 켬
			boundaryMode = true;
			
			for(int i=0; i<3; i++){
				//Self 서비스
				if(i==0){
					//디스플레이 모드가 기능 기반일 경우
					if(displayMode.equals("Function based")){
						//Functional 속성
						mappingFunctionalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
					}
					//디스플레이 모드가 전체 기반일 경우
					else{
						//Identidying 속성
						mappingIdentifyingProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
						//Functional 속성
						mappingFunctionalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
						//Selective 속성
						mappingSelectiveProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
						//Relational 속성
						mappingRelationalProperties(firstServicePropertyList, secondServicePropertyList, lengthOfProperty);
					}		
				}
				//Pre 서비스
				else if(i==1){
					//부모서비스 저장(첫번째 회사)
					String firstParentService = firstServicePropertyList[firstServicePropertyList.length - 2];
					//부모서비스 저장(두번째 회사)
					String secondParentService = secondServicePropertyList[secondServicePropertyList.length - 2];
					//첫번째 서비스의 부모 서비스 명에 해당하는 속성들을 가져옴
					String[] firstPropertyList = DBWork.getPropertyByServiceName(firstCompanyName, firstParentService);
					//두번째 서비스의 부모 서비스 명에 해당하는 속성들을 가져옴
					String[] secondPropertyList = DBWork.getPropertyByServiceName(secondCompanyName, secondParentService);
					//디스플레이 모드가 기능 기반일 경우
					if(displayMode.equals("Function based")){
						//Functional 속성
						mappingFunctionalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
					}
					//디스플레이 모드가 전체 기반일 경우
					else{
						//Identidying 속성
						mappingIdentifyingProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Functional 속성
						mappingFunctionalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Selective 속성
						mappingSelectiveProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Relational 속성
						mappingRelationalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
					}
				}
				//Post 서비스
				else if(i==2){
					//자식서비스 저장(첫번째 회사)
					String firstChildService = firstServicePropertyList[firstServicePropertyList.length - 1];
					//자식서비스 저장(두번째 회사)
					String secondChildService = secondServicePropertyList[secondServicePropertyList.length - 1];
					//첫번째 서비스의 자식 서비스 명에 해당하는 속성들을 가져옴
					String[] firstPropertyList = DBWork.getPropertyByServiceName(firstCompanyName, firstChildService);
					//두번째 서비스의 자식 서비스 명에 해당하는 속성들을 가져옴
					String[] secondPropertyList = DBWork.getPropertyByServiceName(secondCompanyName, secondChildService);
					//디스플레이 모드가 기능 기반일 경우
					if(displayMode.equals("Function based")){
						//Functional 속성
						mappingFunctionalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
					}
					//디스플레이 모드가 전체 기반일 경우
					else{
						//Identidying 속성
						mappingIdentifyingProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Functional 속성
						mappingFunctionalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Selective 속성
						mappingSelectiveProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
						//Relational 속성
						mappingRelationalProperties(firstPropertyList, secondPropertyList, lengthOfProperty);
					}
				}
			} //전체(Pre-Self-Post)서비스들 간의 맵핑 끝
			//ArrayList안의 값 가져오기
			for(int j=0; j<percentageArray.size(); j++){
				sumOfArrayListValue += percentageArray.get(j);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//중간 점검하기 위한 코드
			System.out.println("Pre-Post-Self 총 합계 : " + sumOfArrayListValue);
			System.out.println("Pre-Post-Self 총 매칭 퍼센트 : " + sumOfArrayListValue/3);
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String hitRatio = String.format("%.2f", sumOfArrayListValue/3);	//소수점 2자리까지 표현
			measureSimilarityLevel(sumOfArrayListValue/3);					//유사성 레벨 측정(High, Middle, Low)

			//맵핑 결과를 화면에 표시
			similarityCheckScreen.setMappingResult(hitRatio, similarityLevel, explainSimilarityLevel, displayMode, boundaryMode);
			
			//결과값 변수 초기화
			initializeResultValue();
		}
	} //서비스 맵핑 처리 함수 끝
	
	//Identifying 속성 맵핑 처리
	private void mappingIdentifyingProperties(String[] firstServicePropertyList, String[] secondServicePropertyList, int lengthOfProperty){
		double mappingResult = 0.0;
		for(int i=0; i<lengthOfProperty-7; i++){
			//두 속성의 값이 일치하면
			if(firstServicePropertyList[i].compareTo(secondServicePropertyList[i]) == 0){
				mappingResult += 100; 
			}
			//두 속성의 값이 일치하지 않으면
			else{
				//두 속성 값들 중에 콤마가 있는지 확인
				if(firstServicePropertyList[i].indexOf(",") != -1 || secondServicePropertyList[i].indexOf(",") != -1){
					//전체 문자열 길이 - 콤마 제거한 문자열 길이 = 콤마 개수
					int firstServiceCommaCount = firstServicePropertyList[i].length() - (firstServicePropertyList[i].replaceAll(",", "")).length();
					int secondServiceCommaCount = secondServicePropertyList[i].length() - (secondServicePropertyList[i].replaceAll(",", "")).length();

					mappingResult += handleAttributes(firstServiceCommaCount, secondServiceCommaCount, 
							firstServicePropertyList[i], secondServicePropertyList[i], i);
				}
				//속성이 Priority이면((1.0 - (두 개의 차)) * 100)
				else if(i==5){
					double firstVal = Double.parseDouble(firstServicePropertyList[i]);
					double secondVal = Double.parseDouble(secondServicePropertyList[i]);
					double subtractResult = firstVal>secondVal ? firstVal-secondVal : secondVal-firstVal;
					subtractResult = 1.0 - subtractResult;
					mappingResult += subtractResult*100;
				}
			}
		}
		totalPercentage += mappingResult/10;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//중간 점검하기 위한 코드
		System.out.println("식별속성 10개 매칭 퍼센트 : " +mappingResult/10);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	} //Identifying 속성 맵핑 처리 함수 끝

	//Functional 속성 맵핑 처리
	private void mappingFunctionalProperties(String[] firstServicePropertyList, String[] secondServicePropertyList, int lengthOfProperty){
		double mappingResult = 0.0;
		for(int i=10; i<lengthOfProperty-5; i++){
			//두 속성의 값이 일치하면
			if(firstServicePropertyList[i].compareTo(secondServicePropertyList[i]) == 0){
				//속성이 MandatoryFunction 이면
				if(i == 10){
					mappingResult += 75;	//75%
				}
				//속성이 SecondaryFunction 이면
				else if(i == 11){
					mappingResult += 25;	//25%
				}
			}
			//두 속성의 값이 일치하지 않으면
			else{
				//두 속성 값들 중에 콤마가 있는지 확인
				if(firstServicePropertyList[i].indexOf(",") != -1 || secondServicePropertyList[i].indexOf(",") != -1){
					//전체 문자열 길이 - 콤마 제거한 문자열 길이 = 콤마 개수
					int firstServiceCommaCount = firstServicePropertyList[i].length() - (firstServicePropertyList[i].replaceAll(",", "")).length();
					int secondServiceCommaCount = secondServicePropertyList[i].length() - (secondServicePropertyList[i].replaceAll(",", "")).length();

					mappingResult += handleAttributes(firstServiceCommaCount, secondServiceCommaCount, 
							firstServicePropertyList[i], secondServicePropertyList[i], i);
				}
			}
		}
		totalPercentage += mappingResult;
		
		//디스플레이 모드가 기능 기반일 경우
		if(displayMode.equals("Function based")){
			percentageArray.add(totalPercentage);
			totalPercentage = 0.0;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//중간 점검하기 위한 코드
		System.out.println("기능속성 2개 매칭 퍼센트 : " +mappingResult);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	} //Functional 속성 맵핑 처리 함수 끝

	//Selective 속성 맵핑 처리
	private void mappingSelectiveProperties(String[] firstServicePropertyList, String[] secondServicePropertyList, int lengthOfProperty){
		double mappingResult = 0.0;
		if(firstServicePropertyList[12].compareTo(secondServicePropertyList[12]) == 0){
			mappingResult = 100;
		}
		totalPercentage += mappingResult;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//중간 점검하기 위한 코드
		System.out.println("선택속성 1개 매칭 퍼센트 : " +mappingResult);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	} //Selective 속성 맵핑 처리 함수 끝

	//Relational 속성 맵핑 처리
	private void mappingRelationalProperties(String[] firstServicePropertyList, String[] secondServicePropertyList, int lengthOfProperty){
		double mappingResult = 0.0;
		for(int i=13; i<lengthOfProperty-2; i++){
			//두 속성의 값이 일치하면
			if(firstServicePropertyList[i].compareTo(secondServicePropertyList[i]) == 0){
				mappingResult += 100;
			}
			//두 속성의 값이 일치하지 않으면
			else{
				//두 속성 값들 중에 콤마가 있는지 확인
				if(firstServicePropertyList[i].indexOf(",") != -1 || secondServicePropertyList[i].indexOf(",") != -1){
					//전체 문자열 길이 - 콤마 제거한 문자열 길이 = 콤마 개수
					int firstServiceCommaCount = firstServicePropertyList[i].length() - (firstServicePropertyList[i].replaceAll(",", "")).length();
					int secondServiceCommaCount = secondServicePropertyList[i].length() - (secondServicePropertyList[i].replaceAll(",", "")).length();

					mappingResult += handleAttributes(firstServiceCommaCount, secondServiceCommaCount, 
							firstServicePropertyList[i], secondServicePropertyList[i], i);
				}
			}
		}
		totalPercentage += mappingResult/2;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//중간 점검하기 위한 코드
		System.out.println("관계속성 2개 매칭 퍼센트 : " +mappingResult/2);
		System.out.println("전체속성 15개 매칭 퍼센트 : " + totalPercentage/4 + "\n");
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		percentageArray.add(totalPercentage/4);
		totalPercentage = 0.0;
	} //Relational 속성 맵핑 처리 함수 끝

	//콤마가 있는 속성란 처리
	public double handleAttributes(int firstServiceCommaCount, int secondServiceCommaCount,
			String firstServiceMandatoryFunction, String secondServiceMandatoryFunction, int index){		
		//콤마를 제외한 기능을 나타내는 문자열을 담기 위한 배열 선언
		String[] firstServiceFunctionString = new String[firstServiceCommaCount + 1];
		String[] secondServiceFunctionString = new String[secondServiceCommaCount + 1];

		//첫번째 서비스의 MandatoryFunction속성의 기능들 추출
		extractMandatoryFunction(firstServiceFunctionString, firstServiceMandatoryFunction);

		//두번째 서비스의 MandatoryFunction속성의 기능들 추출
		extractMandatoryFunction(secondServiceFunctionString, secondServiceMandatoryFunction);

		//기능 개수가 많은 것 알아내기
		int maxLength = 0;		//콤마로 구분된 기능의 수 중 최대값
		if(firstServiceFunctionString.length > secondServiceFunctionString.length){
			maxLength = firstServiceFunctionString.length;
		}
		else if(firstServiceFunctionString.length < secondServiceFunctionString.length){
			maxLength = secondServiceFunctionString.length;
		}
		else{
			maxLength = firstServiceFunctionString.length;
		}

		int matchFunctionNumber = 0;
		for(int secondFunctionIndex = 0; secondFunctionIndex < secondServiceFunctionString.length; secondFunctionIndex++){
			for(int firstFunctionIndex = 0; firstFunctionIndex < firstServiceFunctionString.length; firstFunctionIndex++){
				//2개의 서비스들의 기능이 일치하면
				if((secondServiceFunctionString[secondFunctionIndex].trim()).equals(firstServiceFunctionString[firstFunctionIndex].trim())){
					matchFunctionNumber++;	//일치 횟수를 증가
					break;
				}
			}
		}
		///////////////////////////////////////////////////////////
		////기능의 최대수로 일치 횟수를 나눔
		//
		//MandatoryFunction이면
		if(index==10){
			return ((double)matchFunctionNumber)*75 / maxLength;
		}
		//SecondaryFunction이면
		else if(index==11){
			return ((double)matchFunctionNumber)*25 / maxLength;
		}
		return ((double)matchFunctionNumber)*100 / maxLength;
		///////////////////////////////////////////////////////////
	} //콤마가 있는 속성란 처리 함수 끝

	//콤마와 속성 중에 속성들 추출
	public void extractMandatoryFunction(String[] serviceFunctionString, String serviceMandatoryFunction){

		int findIndex = -1;
		int functionStringIndex = 0;
		while((findIndex = serviceMandatoryFunction.indexOf(",")) != -1){	//문자열안에 콤마가 있다면

			//콤마 전까지의 문자열을 저장
			serviceFunctionString[functionStringIndex++] = serviceMandatoryFunction.substring(0, findIndex);

			//콤마 다음 인덱스부터 끝까지 문자열을 새로 만듬
			serviceMandatoryFunction = serviceMandatoryFunction.substring(findIndex+1);
		}
		//마지막 문자열 저장
		serviceFunctionString[functionStringIndex] = serviceMandatoryFunction;
	} //콤마와 속성 중에 속성들 추출 함수 끝
	
	//유사성 레벨 측정
	private void measureSimilarityLevel(double result){
		//적중율이 75%이상이면
		if(result >= (double)75){	
			similarityLevel = "High";
			explainSimilarityLevel = "Service similarity occurs";
			System.out.println("High");
			System.out.println("Service similarity occurs" + "\n");
		}
		//적중율이 50이상 75미만이면
		else if(result >= (double)50){
			similarityLevel = "Middle";
			explainSimilarityLevel = "Service similarity can occur";
			System.out.println("Middle");
			System.out.println("Service similarity can occur" + "\n");
		}
		//적중율이 50미만이면
		else{
			similarityLevel = "Low";
			explainSimilarityLevel = "Service similarity doesn't occur";
			System.out.println("Low");
			System.out.println("Service similarity doesn't occur" + "\n");
		}
	} //유사성 레벨 측정 함수 끝
	
	//변수 초기화
	private void initializeResultValue(){
		percentageArray.clear(); //ArrayList안의 요소들을 삭제
		sumOfArrayListValue = 0.0;
		similarityLevel = null;
	} //변수 초기화 함수 끝
}