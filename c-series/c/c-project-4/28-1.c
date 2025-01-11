#include <stdio.h>
void ClearLineFromReadBuffer(void);

#define LEN 30

typedef struct _book 
{
	char author[LEN];
	char title[LEN];
	int page;
} book;

int main(void)
{
	book list[3];
	int i;

	printf("도서 정보 입력\n");
	for(i=0; i<3; i++)	//데이터 입력
	{
		printf("저자 : ");
		fgets(list[i].author, LEN, stdin);

		printf("제목 : ");
		fgets(list[i].title, LEN, stdin);

		printf("페이지 수 : ");
		scanf("%d", &(list[i].page));

		ClearLineFromReadBuffer();
	}
	printf("\n");

	printf("도서 정보 출력\n");
	for(i=0; i<3; i++)	//데이터 츨력
	{
		printf("book %d \n", i+1);
		printf("저자 : %s", list[i].author);
		printf("제목 : %s", list[i].title);
		printf("페이지 수 : %d \n", list[i].page);
	}
	
	return 0;
}

void ClearLineFromReadBuffer(void)
{
	while(getchar() != '\n');
}
