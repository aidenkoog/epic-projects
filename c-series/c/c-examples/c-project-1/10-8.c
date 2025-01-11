#include <stdio.h>

int Power(int n)
{
	if(n==0)
		return 1;
	return Power(n-1)*2;
}

int main(void)
{	
	int num;

	printf("정수 입력 : ");
	scanf("%d", &num);

	printf("2의 %d승은 %d \n", num, Power(num));
	return 0;;
}
