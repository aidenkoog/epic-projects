
#include "common.h"
#include "screenOut.h"
#include "dvdInfo.h"
#include "dvdInfoAccess.h"

#define MAX_DVD  100
#define DVD_BAKCUP_FILE   "dvdInfo.dat"

static dvdInfo * dvdList[MAX_DVD];
static int numOfDVD=0;

/* ��    ��: int AddDVDInfo(char * ISBN, char * title, int genre)
 * ��    ��: dvdInfo �Ҵ� �� ����. 
 * ��    ȯ: ���� �� '��ϵ� ������ ��', ���� �� 0�� ��ȯ.
 *
 */
int AddDVDInfo(char * ISBN, char * title, int genre)
{
    dvdInfo * pDVD;
    
    if(numOfDVD>=MAX_DVD)
        return 0;  // �Է� ����

    pDVD=(dvdInfo *)malloc(sizeof(dvdInfo));
    strcpy(pDVD->ISBN, ISBN);
    strcpy(pDVD->title, title);
    pDVD->genre=genre;

    pDVD->rentState=RETURNED;
    dvdList[numOfDVD++]=pDVD;

    /* ���Ͽ� ������ ���� */
    StoreDVDListToFile();

    return numOfDVD;   // �Է� ����
}

/* ��    ��: dvdInfo * GetDVDPtrByISBN(char * ISBN)
 * ��    ��: �ش� ISBN�� ������ ��� �ִ� ������ ������ ��ȯ 
 * ��    ȯ: ��� �ȵ� ISBN�� ��� NULL ������ ��ȯ.
 *
 */
dvdInfo * GetDVDPtrByISBN(char * ISBN)
{
    int i;

    for(i=0; i<numOfDVD; i++)
    {
        if(!strcmp(dvdList[i]->ISBN, ISBN))
            return dvdList[i];
    }
    
    return (dvdInfo *)0;
}

/* ��    ��: int IsRegistISBN(char * ISBN)
 * ��    ��: ��� �� ISBN���� Ȯ��. 
 * ��    ȯ: ��� �Ǿ����� 1, �ƴϸ� 0 ��ȯ.
 *
 */
int IsRegistISBN(char * ISBN)
{
    dvdInfo * pDVD = GetDVDPtrByISBN(ISBN);
    
    if(pDVD==0)
        return 0;  // ���� �ȵ� ���.
    else
        return 1;  // ���� �� ���.
}

/* ��    ��: int SetDVDRented(char * ISBN)
 * ��    ��: �뿩 ���·� ����. 
 * ��    ȯ: ���� 1, ���� 0.
 *
 */
int SetDVDRented(char * ISBN)
{
	dvdInfo * pDVD = GetDVDPtrByISBN(ISBN);
    if(pDVD==0)
        return 0;  // ��� �ȵ� ���.
            
    pDVD->rentState=RENTED;

    /* ���Ͽ� ������ ���� */
    StoreDVDListToFile();

    return 1;
}

/* ��    ��: int SetDVDReturned(char * ISBN)
 * ��    ��: �뿩 ���� ���·� ����. 
 * ��    ȯ: ���� 1, ���� 0.
 * Ư�̻���: ���ڰ� ������ �Լ�.
 *
 */
int SetDVDReturned(char * ISBN)
{
    dvdInfo * pDVD = GetDVDPtrByISBN(ISBN);
    
    if(pDVD==0)
        return 0;  // ��� �ȵ� ���.

    pDVD->rentState=RETURNED;

	/* ���Ͽ� ������ ���� */
	StoreDVDListToFile();

    return 1;
}

/* ��    ��: int GetDVDRentState(char * ISBN)
 * ��    ��: ���� �뿩 ���� ��ȯ 
 * ��    ȯ: �ݳ����� RETURNED, �뿩�� RENTED.
 *           �������� �ʴ� ISBN�� ��� -1 ��ȯ.
 */

int GetDVDRentState(char * ISBN)
{
    dvdInfo * pDVD = GetDVDPtrByISBN(ISBN);

    if(pDVD==0)
        return -1;  // ��� �ȵ� ���.

    return pDVD->rentState;
}


/* ��    ��: void StoreDVDListToFile(void)
 * ��    ��: DVD ���� ���Ͽ� ����
 * ��    ȯ: void.
 *
 */
void StoreDVDListToFile(void)
{
    int i;
    FILE * fp=fopen(DVD_BAKCUP_FILE, "wb");
    if(fp==NULL)
        return;
    
    fwrite(&numOfDVD, sizeof(int), 1, fp);
    
    for(i=0; i<numOfDVD; i++)
        fwrite(dvdList[i], sizeof(dvdInfo), 1, fp);
    
    fclose(fp);
}

/* ��    ��: void LoadDVDListFromFile(void)
 * ��    ��: DVD ���� ���Ϸκ��� ����
 * ��    ȯ: void.
 *
 */
void LoadDVDListFromFile(void)
{
    int i;
    FILE * fp=fopen(DVD_BAKCUP_FILE, "rb");
    if(fp==NULL)
        return;
    
    fread(&numOfDVD, sizeof(int), 1, fp);

    for(i=0; i<numOfDVD; i++)
    {
        dvdList[i]=(dvdInfo *)malloc(sizeof(dvdInfo));
        fread(dvdList[i], sizeof(dvdInfo), 1, fp);
    }
 
    fclose(fp);
}
/* end of file */