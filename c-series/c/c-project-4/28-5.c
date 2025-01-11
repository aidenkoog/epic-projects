#include <stdio.h>
#include <string.h>

const int SAME=1;
const int DIFF=2;

int main(int argc, char *argv[])
{
	FILE *sorc1;
	FILE *sorc2;

	char str1[100], str2[100];
	int state1, state2;
	int flag=SAME;    // same or diff
	
	if(argc!=3) 
	{
		puts("usage : comp sorurce1 source2 \n");
		return 1;
	}

	/* 파일의 개방 */
	sorc1=fopen(argv[1], "rt");
	sorc2=fopen(argv[2], "rt");	
	if(sorc1==NULL || sorc2==NULL)
	{
		printf("file open error!\n");
		return 1;
	}

	/* FILE COMPARE */
	while(1)
	{
		if(feof(sorc1)!=0 && feof(sorc2)!=0)
			break;
		fgets(str1, sizeof(str1), sorc1);
		fgets(str2, sizeof(str2), sorc2);
		if(strcmp(str1, str2)!=0)
		{
			flag=DIFF;
			break;
		}
	}

	if(flag==SAME)
		fputs("두 개의 파일은 완전히 일치 합니다.", stdout);
	else
		fputs("두 개의 파일은 불일치 합니다.", stdout);
	
	/* 파일의 종결 */
	state1=fclose(sorc1);
	state2=fclose(sorc2);
	if(state1!=0||state2!=0)
	{
		printf("file close error!\n");
		return 1;
	}
	return 0;
}
