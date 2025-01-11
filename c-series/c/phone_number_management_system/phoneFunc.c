

#include "common.h"
#include "phoneData.h"
#include "screenOut.h"
#include "phoneFunc.h"  /* StoreDataToFileInStruct �Լ��� ȣ�� ���� */

#define LIST_NUM   100

int numOfData=0;
phoneData * phoneList[LIST_NUM];

/* ��    ��: void InputPhoneData(void)
 * ��    ��: ��ȭ��ȣ ���� ������ �Է� �޾Ƽ� ����. 
 * ��    ȯ: void.
 *
 */
void InputPhoneData(void)
{
    phoneData * pData;
    int i;
    
    if(numOfData>=LIST_NUM)
    {
        puts("�޸� ������ �����մϴ�.");
        return;
    }
    
    pData = (phoneData *)malloc(sizeof(phoneData));

    fputs("�̸� �Է�: ", stdout);
    gets(pData->name);
    
    fputs("��ȭ��ȣ �Է�: ", stdout);
    gets(pData->phoneNum);

    for(i=0; i<numOfData; i++)
    {
        if( !strcmp(phoneList[i]->name, pData->name) &&
            !strcmp(phoneList[i]->phoneNum, pData->phoneNum) )
        {
            puts("�̹� �Էµ� ������ �Դϴ�.");
            free(pData);
            getchar();
            return;
        }
    }

    phoneList[numOfData]=pData;
    numOfData++;
    
    /* ���� ���� */
    StoreDataToFileInStruct();

    puts("�Է��� �Ϸ�Ǿ����ϴ�.");
    getchar();
}

/* ��    ��: void ShowAllData(void)
 * ��    ��: ����� ������ ��ü ���. 
 * ��    ȯ: void.
 *
 */
void ShowAllData(void)
{
    int i;
    
    for(i=0; i<numOfData; i++)
    {
        ShowPhoneInfoByPtr(phoneList[i]);
    }
    
    puts("����� �Ϸ�Ǿ����ϴ�.");
    getchar();
}

/* ��    ��: void SearchPhoneData(void)
 * ��    ��: �̸��� ���� ������ �˻�. 
 * ��    ȯ: void.
 *
 */
void SearchPhoneData(void)
{
    int i;
    int searchSte=0;  // ã���� 1, �� ã���� 0
    char searchName[NAME_LEN];
    
    fputs("ã�� �̸���? ", stdout);
    gets(searchName);
    
    for(i=0; i<numOfData; i++)
    {
        if(!strcmp(phoneList[i]->name, searchName))
        {
            ShowPhoneInfoByPtr(phoneList[i]);
            searchSte=1;
        }
    }
    
    if(searchSte==0)  // �ϳ��� ã�� ���ߴٸ�
        puts("ã�� �̸��� �����Ͱ� �������� �ʽ��ϴ�.");
    else
        puts("�˻��� �Ϸ�Ǿ����ϴ�.");
	
    getchar();
}

/* ��    ��: void DeletePhoneData(void)
 * ��    ��: �̸��� �����Ͽ� ������ ����. 
 * ��    ȯ: void.
 *
 */
void DeletePhoneData(void)
{
    int i;
    char delName[NAME_LEN];

    int idxOfMatchingData[LIST_NUM];
    int matchedCount=0;
    int selection=0;
    int delIdx=0;
    
    fputs("ã�� �̸���? ", stdout);
    gets(delName);

    for(i=0; i<numOfData; i++)
    {
        if(!strcmp(phoneList[i]->name, delName))
            idxOfMatchingData[matchedCount++]=i;
    }
    
    if(matchedCount==0)
    {
        puts("ã�� �̸��� �����Ͱ� �������� �ʽ��ϴ�.");
        getchar();
        return;
    }
    else if(matchedCount==1)
    {
        delIdx=idxOfMatchingData[0];
    }
    else
    {
        for(i=0; i<matchedCount; i++)
        {
            printf("NUM. %d \n", i+1);
            ShowPhoneInfoByPtr(phoneList[idxOfMatchingData[i]]);
        }
        
        fputs("����: ", stdout);
        scanf("%d", &selection);
        fflush(stdin);

        delIdx=idxOfMatchingData[selection-1];
    }
    
    free(phoneList[delIdx]);
    
    for(i=delIdx; i<numOfData-1; i++)
    {
        phoneList[i]=phoneList[i+1];
    }
    numOfData--;

    /* ���� ���� */
    StoreDataToFileInStruct();

    puts("������ �Ϸ�Ǿ����ϴ�.");
    getchar();
}


/* ��    ��: void ChangePhoneData(void)
 * ��    ��: �̸��� �����Ͽ� ��ȭ��ȣ ����. 
 * ��    ȯ: void.
 *
 */
void ChangePhoneData(void)
{
    int i;
    char serchName[NAME_LEN];
    char newPhoneNumber[PHONE_LEN];
    
    int idxOfMatchingData[LIST_NUM];
    int matchedCount=0;
    int selection=0;
    int delIdx=0;
    
    fputs("���� ����� �̸���? ", stdout);
    gets(serchName);
     
    
    for(i=0; i<numOfData; i++)
    {
        if(!strcmp(phoneList[i]->name, serchName))
            idxOfMatchingData[matchedCount++]=i;
    }
    
    if(matchedCount==0)
    {
        puts("���� ��� �̸��� �������� �ʽ��ϴ�.");
        getchar();
        return;
    }
    else if(matchedCount==1)
    {
        delIdx=idxOfMatchingData[0];
    }
    else
    {
        for(i=0; i<matchedCount; i++)
        {
            printf("NUM. %d \n", i+1);
            ShowPhoneInfoByPtr(phoneList[idxOfMatchingData[i]]);
        }
        
        fputs("����: ", stdout);
        scanf("%d", &selection);
        fflush(stdin);

        delIdx=idxOfMatchingData[selection-1];
    }

    fputs("������ ��ȭ��ȣ��? ", stdout);
    gets(newPhoneNumber);
    strcpy(phoneList[delIdx]->phoneNum, newPhoneNumber);

    /* ���� ���� */
    StoreDataToFileInStruct();

    puts("������ �Ϸ�Ǿ����ϴ�.");
    getchar();
}


/* ��    ��: void StoreDataToFile(void)
 * ��    ��: ��� ������ ���Ͽ� ����. 
 * ��    ȯ: void.
 *
 */
void StoreDataToFile(void)
{
    int i;
    FILE * fp=fopen("phoneData.dat", "w");
    
    fwrite(&numOfData, sizeof(int), 1, fp);  // ������ �� ����.
    for(i=0; i<numOfData; i++)
    {
        /* ���ڿ��� ���� ���� ���� \n�� ���� �߿�! */
        fprintf(fp, "%s\n%s\n", phoneList[i]->name, phoneList[i]->phoneNum);
        free(phoneList[i]);
    }
    
    fclose(fp);
}

/* ��    ��: void StoreDataToFileInStruct(void)
 * ��    ��: ��� ������ ���Ͽ� ����ü ���� ������ ����
 * ��    ȯ: void
 *
 */
void StoreDataToFileInStruct(void)
{
    int i;

    FILE * fp=fopen("phoneDataStruct.dat", "wb");  // ���� ���� ����.
    
    fwrite(&numOfData, sizeof(int), 1, fp);

    for(i=0; i<numOfData; i++)
    {
        fwrite(phoneList[i], sizeof(phoneData), 1, fp);	
    }

    fclose(fp);
}

/* ��    ��: void LoadDataFromFile(void)
 * ��    ��: ��� ������ ���Ϸκ��� ����. 
 * ��    ȯ: void.
 *
 */
void LoadDataFromFile(void)
{
    int i, sLen;
    FILE * fp=fopen("phoneData.dat", "r");
    if(fp==NULL)  // ������ �������� �ʴ´ٸ�.
        return;
    
    fread(&numOfData, sizeof(int), 1, fp);
    
    for(i=0; i<numOfData; i++)
    {
        phoneList[i]=(phoneData*)malloc(sizeof(phoneData));
        fgets(phoneList[i]->name, NAME_LEN, fp);
        
        /* ���ڿ� ������ ���� �Էµ� NULL ���� ���� */
        sLen=strlen(phoneList[i]->name);
        phoneList[i]->name[sLen-1]=0;
        
        fgets(phoneList[i]->phoneNum, PHONE_LEN, fp);
        
        /* ���ڿ� ������ ���� �Էµ� NULL ���� ���� */
        sLen=strlen(phoneList[i]->phoneNum);
        phoneList[i]->phoneNum[sLen-1]=0;
    }
    fclose(fp);
}

/* ��    ��: void LoadDataFromFileInStruct(void)
 * ��    ��: ��� ������ ���Ϸκ��� ����ü ���� ������ ����
 * ��    ȯ: void
 *
 */
void LoadDataFromFileInStruct(void)
{
    int i;
    
    FILE * fp=fopen("phoneDataStruct.dat", "rb");
    
    if(fp==NULL)  // ������ �������� �ʴ´ٸ�.
        return;
    
    fread(&numOfData, sizeof(int), 1, fp);
    
    for(i=0; i<numOfData; i++)
    {
        phoneList[i]=(phoneData*)malloc(sizeof(phoneData));
        fread(phoneList[i], sizeof(phoneData), 1, fp);
    }
    fclose(fp);
}
/* end of file */