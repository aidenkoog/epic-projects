#include <stdio.h>
#include <string.h>

#define NAME_LEN	20
#define TEL_LEN		20
#define MAX_PERSON_NUM		100

typedef struct _Person
{
	char name[NAME_LEN];
	char phone[TEL_LEN];
} Person;

void ShowMenu(void);	//메뉴 출력
void InsertTelInfo(Person *, int *);	//데이터 입력 입력
void RemoveTelInfo(Person *, int *);	//이름으로 데이터 삭제 
void SearchTelInfo(Person *, int);	//데이터 검색
void PrintAll(Person *, int);	//전체 데이터 출력 
void LoadData(Person *, int *);	//파일로부터 데이터 읽음.
void StoreData(Person *, int);	//파일에 데이터 저장.

int main(void)
{
	int choice;
	Person perArr[MAX_PERSON_NUM];	// Data 저장을 위한 배열
	int perNum=0;	// 저장된 인원 수

	LoadData(perArr, &perNum);

	while(1)
	{
		ShowMenu();
		printf("Choose the item : "); 
		scanf("%d", &choice);

		switch(choice)
		{
			case 1 : 
				InsertTelInfo(perArr, &perNum); 
				break;
			case 2 : 
				RemoveTelInfo(perArr, &perNum); 
				break;
			case 3 : 
				SearchTelInfo(perArr, perNum); 
				break;
			case 4 : 
				PrintAll(perArr, perNum); 
				break;
			case 5 : 
				StoreData(perArr, perNum); 
				return 0;
			default : 
				printf("Illegal selection.. \n"); 
				break;
		}
	}
	return 0;
}

void ShowMenu(void)
{
	printf("\n---------Menu--------\n");
	printf("             1. Insert \n");
	printf("             2. Delete \n");
	printf("             3. Search \n");
	printf("             4. Print All\n");
	printf("             5. Exit\n");
}

void InsertTelInfo(Person * parr, int * pnum)
{
	char name[NAME_LEN], phone[TEL_LEN];

	printf("[ INSERT ] \n");
	printf("Input Name : "); scanf("%s", name);
	printf("Input Tel Number : "); scanf("%s", phone);

	strcpy(parr[*pnum].name, name);
	strcpy(parr[*pnum].phone, phone);
	(*pnum)++;

	printf("--------> Data Inserted......\n");
}

void RemoveTelInfo(Person * parr, int * pnum)
{
	int i, j;
	char name[NAME_LEN];

	printf("[ Remove ] \n");
	printf("Input Name for Removing : "); scanf("%s", name);

	for(i=0; i < *pnum; i++)
	{
		if(strcmp(parr[i].name, name)==0)
		{
			for(j=i+1; j < *pnum; j++)
			{
				strcpy(parr[j-1].name, parr[j].name);
				strcpy(parr[j-1].phone, parr[j].phone);
			}
			(*pnum)--;
			return;
		}
	}

	printf("-------> Data Not Found......\n");
}

void SearchTelInfo(Person * parr, int num)
{
	int i;
	char name[NAME_LEN];

	printf("[ Search ] \n");
	printf("Input Name for Searching : "); scanf("%s", name);

	for(i=0; i<num; i++)
	{
		if(strcmp(parr[i].name, name)==0)
		{
			printf("---> Name: %s   /   Tel : %s  \n",
					parr[i].name, parr[i].phone);
			return;
		}
	}

	printf("--------> Data Not Found......\n");
}

void PrintAll(Person * parr, int num)
{
	int i;
	printf("[ Print All Data ] \n");

	for(i=0; i<num; i++)
		printf("Name : %s    /   Tel : %s \n", parr[i].name, parr[i].phone);
}

void LoadData(Person * parr, int * pnum)		//data Load
{
	FILE * file = fopen("data.dat", "rt");
	if(file==NULL)
	{	
		return;	
	}

	while(1)
	{
		fscanf(file, "%s %s", parr[*pnum].name, parr[*pnum].phone);
		if(feof(file)!=0)
			break;
		(*pnum)++;
	}
}

void StoreData(Person * parr, int num)		//data store
{
	int i;

	FILE * file = fopen("data.dat", "wt");
	if(file==NULL)
	{
		printf("file open error!\n");
		return;
	}

	for(i=0; i<num; i++)
		fprintf(file, "%s %s ", parr[i].name, parr[i].phone);
}
