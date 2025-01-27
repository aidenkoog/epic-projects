public class AnalyzeFeature {
	private AnalyzeFeatureScreen analyzeFeatureScreen = null;
	private String firstFeature = null;
	private String secondFeature = null;
	private String resultOfCollisionAnalysis = null;
	private String resultOfDuplicationAnalysis = null;
	
	//생성자
	public AnalyzeFeature(AnalyzeFeatureScreen analyzeFeatureScreen, String firstFeature, String secondFeature){
		this.initializeMember(analyzeFeatureScreen, firstFeature, secondFeature);
		this.analyzeFeature();
	} //생성자 끝
	
	//멤버 변수 초기화
	public void initializeMember(AnalyzeFeatureScreen analyzeFeatureScreen, String firstFeature, String secondFeature){
		this.analyzeFeatureScreen = analyzeFeatureScreen;
		this.firstFeature = firstFeature;
		this.secondFeature = secondFeature;
	} //멤버 변수 초기화 함수 끝
	
	//휘처간 중복과 충돌의 진단
	public void analyzeFeature(){
		//전체 속성 11개
		String[] totalPropertyOfFirstFeature = MainFrame.featureQueries.getPropertyByFeatureName(firstFeature);
		String[] totalPropertyOfSecondFeature = MainFrame.featureQueries.getPropertyByFeatureName(secondFeature);
		
		//시멘틱스 속성 Classification, FType, Priority, Definition
		String[] semanticsOfFirstFeature = MainFrame.featureQueries.getSemanticsByFeatureName(firstFeature);
		String[] semanticsOfSecondFeature = MainFrame.featureQueries.getSemanticsByFeatureName(secondFeature);
		
		//Selective Attributes, Condition
		String[] relationshipOfFirstFeature = MainFrame.featureQueries.getRelationshipByFeatureName(firstFeature);
		String firstSelectiveAttributes = relationshipOfFirstFeature[0];	//Selective Attribute	
		String firstCondition = relationshipOfFirstFeature[1];				//Condition
		String[] relationshipOfSecondFeature = MainFrame.featureQueries.getRelationshipByFeatureName(secondFeature);
		String secondSelectiveAttributes = relationshipOfSecondFeature[0];
		String secondCondition = relationshipOfSecondFeature[1];
		
		//중복 진단
		resultOfDuplicationAnalysis = analyzeDuplication(semanticsOfFirstFeature, semanticsOfSecondFeature, 
				 		   									 firstSelectiveAttributes, firstCondition, secondSelectiveAttributes, secondCondition);
		//충돌 진단
		if(resultOfCollisionAnalysis == null){
			resultOfCollisionAnalysis = analyzeCollision(semanticsOfFirstFeature, semanticsOfSecondFeature, 
							firstSelectiveAttributes, firstCondition, secondSelectiveAttributes, secondCondition);
		}
		//휘처 간 Selective 속성 비교
		String resultOfSelectiveMapping = mappingProperty(firstSelectiveAttributes, secondSelectiveAttributes);
		
		//휘처 간 Condtition 비교
		String resultOfConditionMapping = mappingProperty(firstCondition, secondCondition);
		
		//휘처 간 퍼센티지 산출
		String resultOfMapping = mappingFeature(totalPropertyOfFirstFeature, totalPropertyOfSecondFeature);
		
		//중복, 충돌 결과 표시
		analyzeFeatureScreen.analyzedResult(resultOfDuplicationAnalysis, resultOfCollisionAnalysis, 
											resultOfSelectiveMapping, resultOfConditionMapping, resultOfMapping);
	} //휘처간 중복과 충돌의 진단 함수 끝
	
	//휘처 Selective와 Condition 속성 비교
	public String mappingProperty(String firstProperty, String secondProperty){
		if(firstProperty.equals(secondProperty)){
			return "100";
		}
		return "0";
	} //휘처 Selective와 Condition 속성 비교 함수 끝
	
	//휘처 매칭 퍼센티지 산출
	public String mappingFeature(String[] totalPropertyOfFirstFeature, String[] totalPropertyOfSecondFeature){
		int numberOfMatch = 0;
		int numberOfProperties = totalPropertyOfFirstFeature.length;	//11개
		for(int i=0; i<numberOfProperties; i++){
			if(totalPropertyOfFirstFeature[i].equals(totalPropertyOfSecondFeature[i])){
				numberOfMatch++;
			}
		}
		return String.format("%.2f", ((double)(numberOfMatch*100)) / numberOfProperties);
	} //휘처 매칭 퍼센티지 산출 함수 끝
	
	//휘처 시멘틱스 검사
	public boolean checkSimantics(String[] semanticsOfFirstFeature, String[] semanticsOfSecondFeature){
		for(int i=0; i<semanticsOfFirstFeature.length; i++){
			if(!(semanticsOfFirstFeature[i].equals(semanticsOfSecondFeature[i]))){
				System.out.println(!false);
				return false;
			}
		}
		return true;
	} //휘처 시멘틱스 검사 함수 끝
	
	//중복 진단
	public String analyzeDuplication(String[] semanticsOfFirstFeature, String[] semanticsOfSecondFeature, 
			                         String firstSelectiveAttributes, String firstCondition, 
			                         String secondSelectiveAttributes, String secondCondition){
		String result = null;
		
		//2개의 휘처의 시멘틱스 속성이 같다면
		if(checkSimantics(semanticsOfFirstFeature, semanticsOfSecondFeature)){	
			System.out.println("Collision doesn't occur");
			resultOfCollisionAnalysis = "Collision doesn't occur";
			
			//2개의 휘처가 Mandatory 속성이라면
			if(firstSelectiveAttributes.equals(secondSelectiveAttributes) && firstSelectiveAttributes.equals("Mandatory")){
				System.out.println("Duplication occurs");
				result = "Duplication occurs";
				
			//1개의 휘처가 Mandatory 속성이고
			}else if(firstSelectiveAttributes.equals("Mandatory")){
				//다른 휘처가 Optional 또는 Alternative 속성이고
				if(secondSelectiveAttributes.equals("Optional") || secondSelectiveAttributes.equals("Alternative")){
					//두번째 휘처가 선택된 상태이면
					if((secondCondition.substring(0, 8)).equals("requires")){	
						System.out.println("Duplication occurs");
						result = "Duplication occurs";
					}else{
						System.out.println("Duplication doesn't occurs");
						result = "Duplication doesn't occurs";
					}
				}
			//1개의 휘처가 Mandatory 속성이 아니고
			}else{
				//다른 휘처가 Optional 또는 Alternative 속성이고
				if(secondSelectiveAttributes.equals("Optional") || secondSelectiveAttributes.equals("Alternative")){
					//휘처 둘 다 선택된 상태이면
					if((firstCondition.substring(0, 8)).equals("requires") && (secondCondition.substring(0, 8)).equals("requires")){	
						System.out.println("Duplication occurs");
						result = "Duplication occurs";
					}else{
						System.out.println("Duplication doesn't occurs");
						result = "Duplication doesn't occurs";
					}
				}
			}
		//2개의 휘처의 시멘틱스 속성이 다르다면
		}else{	
			System.out.println("Duplication doesn't occur");
			result = "Duplication doesn't occurs";
		}
		return result;
	} //중복 진단 함수 끝
	
	//충돌 진단
	public String analyzeCollision(String[] semanticsOfFirstFeature, String[] semanticsOfSecondFeature, 
            					   String firstSelectiveAttributes, String firstCondition, String secondSelectiveAttributes, String secondCondition){
		String result = null;
		//2개의 휘처가 Mandatory 속성이고
		if(firstSelectiveAttributes.equals("Mandatory") && secondSelectiveAttributes.equals("Mandatory")){
			//두개의 휘처 중에 서로 모순되는 시멘틱스가 있다면
			if(!(checkSimantics(semanticsOfFirstFeature, semanticsOfSecondFeature))){	
				System.out.println("Collision occurs");
				result = "Collision occurs";
			}else{
				System.out.println("Collision doesn't occur");
				result = "Collision doesn't occur";
			}
		}
		//1개의 휘처가 Mandatory 속성이고
		if(firstSelectiveAttributes.equals("Mandatory")){
			//다른 휘처가 Optional 또는 Alternative 속성이고
			if(secondSelectiveAttributes.equals("Optional") || secondSelectiveAttributes.equals("Alternative")){
				//두개의 휘처 중에 서로 모순되는 시멘틱스가 있고 2개의 휘처의 Condition 속성이 같다면
				if(!(checkSimantics(semanticsOfFirstFeature, semanticsOfSecondFeature)) 
															&& ((secondCondition.substring(0, 8)).equals("requires"))){	
					System.out.println("Collision occurs");
					result = "Collision occurs";
				}else{
					System.out.println("Collision doesn't occur");
					result = "Collision doesn't occur";
				}
			}
		//1개의 휘처가 Optional 속성 또는 Alternative 속성이고
		}else{	
			//다른 휘처가 Optional 또는 Alternative 속성이고
			if(secondSelectiveAttributes.equals("Optional") || secondSelectiveAttributes.equals("Alternative")){
				//두개의 휘처 중에 서로 모순되는 시멘틱스가 있고 2개의 휘처의 Condition 속성이 같다면
				if(!(checkSimantics(semanticsOfFirstFeature, semanticsOfSecondFeature)) && 
				    ((firstCondition.substring(0, 8)).equals("requires") && (secondCondition.substring(0, 8)).equals("requires"))){
					System.out.println("Collision occurs");
					result = "Collision occurs";
				}else{
					System.out.println("Duplication doesn't occur");
					result = "Collision doesn't occur";
				}
			}
		}
		return result;
	} //충돌 진단 함수 끝
}