#include <stdio.h>

void NineNine(int num1, int num2);	

int main(void)
{
	int num1, num2;

	printf("두개의 정수 입력 : ");
	scanf("%d %d", &num1, &num2);

	if(num1<num2)
		NineNine(num1, num2);
	else
		NineNine(num2, num1);

    return 0;
}

/* num1단부터 num2단까지 출력 */
void NineNine(int num1, int num2)	
{
	int i;
	
	while(num1<=num2)
	{
		for(i=1; i<10; i++)
			printf("%d * %d = %d \n", num1, i, num1*i);
		printf("\n");
		num1++;
	}
}