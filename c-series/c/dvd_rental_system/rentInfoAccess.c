
#include "common.h"
#include "rentInfo.h"
#include "cusInfo.h"
#include "cusInfoAccess.h"
#include "rentInfoAccess.h"
#include "screenOut.h"

#define RENT_LEN  100
#define RENT_BAKCUP_FILE   "rentInfo.dat"

static dvdRentInfo rentList[RENT_LEN];
static int numOfRentCus=0;

/* ��    ��: void AddRentList(char * ISBN, char * cusID, int rentDay)
 * ��    ��: �뿩 ������ ����
 * ��    ȯ: void
 * 
 */
void AddRentList(char * ISBN, char * cusID, int rentDay)
{
    strcpy(rentList[numOfRentCus].ISBN, ISBN);
    strcpy(rentList[numOfRentCus].cusID, cusID);
    rentList[numOfRentCus].rentDay = rentDay;

    numOfRentCus++;
    
    StoreRentListToFile();
}

/* ��    ��: void PrintOutRentAllCusInfo(char * ISBN)
 * ��    ��: Ư�� ISBN�� DVD �뿩 ���� ���� ���
 * ��    ȯ: void
 * 
 */
void PrintOutRentAllCusInfo(char * ISBN)   // CusInfo, ���� ���� ���
{
    int i;
    cusInfo * ptrCusInfo;
    
    for(i=0; i<numOfRentCus; i++)
    {
        if(!strcmp(rentList[i].ISBN, ISBN))
        {
            printf("�뿩��: %d \n", rentList[i].rentDay);
            ptrCusInfo=GetCusPtrByID(rentList[i].cusID);
            ShowCustomerInfoContinue(ptrCusInfo);
        }
    }
}

/* ��    ��: void PrintOutCusAllRentInfo(char * ID, unsigned int start, unsigned int end)
 * ��    ��: ���� �Ⱓ �ȿ� �̷��� Ư�� ������ DVD �뿩 ���� ���
 * ��    ȯ: void
 * 
 */
void PrintOutCusAllRentInfo   //RentInfo, �뿩 ���� ���
    (char * ID, unsigned int start, unsigned int end)
{
    int i;
    
    for(i=0; i<numOfRentCus; i++)
    {
        /* �ش� ID�� �������. */
        if(!strcmp(rentList[i].cusID, ID))
        {
            /* �Ⱓ ���� �������. */
            if(start<=rentList[i].rentDay && rentList[i].rentDay<=end)
                ShowDVDRentInfo(rentList[i]);
        }
    }
}

/* ��    ��: void StoreRentListToFile(void)
 * ��    ��: �뿩 ���� ���Ͽ� ����
 * ��    ȯ: void.
 *
 */
void StoreRentListToFile(void)
{
    FILE * fp=fopen(RENT_BAKCUP_FILE, "wb");
    if(fp==NULL)
        return;

    fwrite(&numOfRentCus, sizeof(int), 1, fp);
    fwrite(rentList, sizeof(dvdInfo), numOfRentCus, fp);

    fclose(fp);
}

/* ��    ��: void LoadRentListFromFile(void)
 * ��    ��: �뿩 ���� ���Ϸκ��� ����
 * ��    ȯ: void.
 *
 */
void LoadRentListFromFile(void)
{
    FILE * fp=fopen(RENT_BAKCUP_FILE, "rb");
    if(fp==NULL)
        return;
 
    fread(&numOfRentCus, sizeof(int), 1, fp);
    fread(rentList, sizeof(dvdInfo), numOfRentCus, fp);

    fclose(fp);
}

/* end of file */