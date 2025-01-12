
#include "common.h"
#include "cusInfo.h"
#include "cusInfoAccess.h"

#define MAX_CUSTOMER  100
#define CUS_BAKCUP_FILE   "cusInfo.dat"

static cusInfo * cusList[MAX_CUSTOMER];
static int numOfCustomer=0;

/* ��    ��: int AddCusInfo (char * ID, char * name, char * num)
 * ��    ��: cusInfo �Ҵ� �� ����. 
 * ��    ȯ: ���� �� '��ϵ� ������ ����', ���� �� 0�� ��ȯ.
 *
 */
int AddCusInfo(char * ID, char * name, char * num)
{
    cusInfo * pCus;
    
    if(numOfCustomer>=MAX_CUSTOMER)
        return 0;  // �Է� ����

    pCus=(cusInfo*)malloc(sizeof(cusInfo));
    strcpy(pCus->ID, ID);
    strcpy(pCus->name, name);
    strcpy(pCus->phoneNum, num);

    cusList[numOfCustomer++]=pCus;

    /* ���Ͽ� ������ ���� */
    StoreCusListToFile();

    return numOfCustomer;   // �Է� ����
}


/* ��    ��: cusInfo * GetCusPtrByID(char * ID)
 * ��    ��: �ش� ID�� ������ ��� �ִ� ������ ������ ��ȯ 
 * ��    ȯ: ��� �ȵ� ID�� ��� NULL ������ ��ȯ.
 *
 */
cusInfo * GetCusPtrByID(char * ID)
{
    int i;

    for(i=0; i<numOfCustomer; i++)
    {
        if(!strcmp(cusList[i]->ID, ID))
            return cusList[i];
    }
    
    return (cusInfo *)0;
}


/* ��    ��: int IsRegistID(char * ID)
 * ��    ��: ���� �� ID���� Ȯ��. 
 * ��    ȯ: ���� �Ǿ����� 1, �ƴϸ� 0 ��ȯ.
 *
 */
int IsRegistID(char * ID)
{
    cusInfo * pCus = GetCusPtrByID(ID);

    if(pCus==0)
        return 0;  // ���� �ȵ� ���.
    else
        return 1;  // ���� �� ���.
}


/* ��    ��: void StoreCusListToFile(void)
 * ��    ��: ���� ���� ���Ͽ� ����
 * ��    ȯ: void.
 *
 */
void StoreCusListToFile(void)
{
    int i;

    FILE * fp=fopen(CUS_BAKCUP_FILE, "wb");
    if(fp==NULL)
        return;

    fwrite(&numOfCustomer, sizeof(int), 1, fp);

    for(i=0; i<numOfCustomer; i++)
        fwrite(cusList[i], sizeof(cusInfo), 1, fp);

    fclose(fp);
}

/* ��    ��: void LoadCusListFromFile(void)
 * ��    ��: ���� ���� ���Ϸκ��� ����
 * ��    ȯ: void.
 *
 */
void LoadCusListFromFile(void)
{
    int i;

    FILE * fp=fopen(CUS_BAKCUP_FILE, "rb");
    if(fp==NULL)
        return;

    fread(&numOfCustomer, sizeof(int), 1, fp);
    
    for(i=0; i<numOfCustomer; i++)
    {
        cusList[i]=(cusInfo *)malloc(sizeof(cusInfo));
        fread(cusList[i], sizeof(cusInfo), 1, fp);
    }

    fclose(fp);
}

/* end of file */