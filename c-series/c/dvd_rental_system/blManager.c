
#include "common.h"
#include "cusInfo.h"
#include "dvdInfo.h"
#include "dvdInfoAccess.h"
#include "cusInfoAccess.h"
#include "rentInfoAccess.h"
#include "screenOut.h"

/* ��    ��: void InitData(void)
 * ��    ��: ������ �ʱ�ȭ
 * ��    ȯ: void
 *
 */
void InitData(void)
{
    LoadCusListFromFile();    // cusInfoAccess.c�� ���ǵ� �Լ�
    LoadDVDListFromFile();    // dvdInfoAccess.c�� ���ǵ� �Լ�
    LoadRentListFromFile();    // rentInfoAccess.c�� ���ǵ� �Լ�
}

/*---------- cusManager.c�� ���ǵǾ��� �Լ���--------------------------*/

/* ��    ��: void RegistCustomer(void)
 * ��    ��: �ű� ȸ�� ����. 
 * ��    ȯ: void
 *
 */
void RegistCustomer(void)
{
    char ID[ID_LEN];
    char name[NAME_LEN];
    char phoneNum[PHONE_LEN];
    
    /* ID �Է� ���� */
    fputs("ID �Է�: ", stdout);
    gets(ID);
    
    if(IsRegistID(ID))
    {
        puts("�ش� ID�� ��� �߿� �ֽ��ϴ�. �ٸ� ID�� ������ �ּ���");
        getchar();
        return;
    }

    /* �̸� �Է� */
    fputs("�̸� �Է�: ", stdout);
    gets(name);
    
    /* ��ȭ��ȣ �Է� */
    fputs("��ȭ��ȣ �Է�: ", stdout);
    gets(phoneNum);
    
    if(!AddCusInfo(ID, name, phoneNum))
    {
        puts("�������� ������ ���忡 �����Ͽ����ϴ�.");
        getchar();
        return;
    }
    
    puts("������ �Ϸ�Ǿ����ϴ�.");
    getchar();
}

/* ��    ��: void SearchCusInfo(void)
 * ��    ��: ID�� ���� ȸ�� ���� �˻�
 * ��    ȯ: void
 * 
 */
void SearchCusInfo(void)
{
    char ID[ID_LEN];
    cusInfo * cusPtr;
    
    /* ã�� ����� ID �Է� */
    fputs("ã�� ID �Է�: ", stdout);
    gets(ID);

    cusPtr=GetCusPtrByID(ID); 
    if(cusPtr==0)
    {
        puts("�������� �ʴ� ID�Դϴ�.");
        getchar();
        return;
    }

    ShowCustomerInfo(cusPtr);
}


/*---------- dvdManager.c�� ���ǵǾ��� �Լ���--------------------------*/
/* ��    ��: void RegistDVD(void)
 * ��    ��: DVD ���. 
 * ��    ȯ: void
 *
 */
void RegistDVD(void)
{
    char ISBN[ISBN_LEN];
    char title[TITLE_LEN];
    int genre;
    
    /* ISBN �Է� ���� */
    fputs("ISBN �Է�: ", stdout);
    gets(ISBN);
    
    if(IsRegistISBN(ISBN))
    {
        puts("�ش� ISBN�� �̹� ��ϵǾ����ϴ�.");
        getchar();
        return;
    }

    /* ���� �Է� */
    fputs("���� �Է�: ", stdout);
    gets(title);
    
    /* �帣 �Է� */
    fputs("�帣 �Է� (�׼� 1, �ڹ� 2, SF 3, �θ�ƽ 4): ", stdout);
    scanf("%d", &genre);
    fflush(stdin);

    if(!AddDVDInfo(ISBN, title, genre))
    {
        puts("�������� ������ ���忡 �����Ͽ����ϴ�.");
        getchar();
        return;
    }
    
    puts("����� �Ϸ�Ǿ����ϴ�.");
    getchar();
}

/* ��    ��: void SearchDVDInfo(void)
 * ��    ��: ISBN�� �̿��� DVD ���� �˻�
 * ��    ȯ: void
 * 
 */
void SearchDVDInfo(void)
{
    char ISBN[ISBN_LEN];
    dvdInfo * dvdPtr;
    
    /* ã�� ����� ISBN �Է� */
    fputs("ã�� ISBN �Է�: ", stdout);
    gets(ISBN);

    dvdPtr=GetDVDPtrByISBN(ISBN); 
    if(dvdPtr==0)
    {
        puts("��ϵ��� ���� ISBN�Դϴ�.");
        getchar();
        return;
    }

    ShowDVDInfo(dvdPtr);
}

/* ��    ��: void RentDVD(void)
 * ��    ��: ������ DVD�� �뿩�ϴ� ���� ó��
 * ��    ȯ: void
 * 
 */		
void RentDVD(void)
{
    char ISBN[ISBN_LEN];
    int registDVD;
    int registCus;
    int rentSte;

    char cusID[ID_LEN];      // �뿩 ���� ID
    unsigned int rentDay;     // �뿩��

    // 1. �뿩�� DVD ISBN �Է� �� ����.
    fputs("�뿩 DVD ISBN �Է�: ", stdout);
    gets(ISBN);
    
    registDVD=IsRegistISBN(ISBN);
    if(registDVD==0)
    {
        puts("��ϵ��� ���� ISBN �Դϴ�.");
        getchar();
        return;
    }
    
    // 2. �뿩 ������ �������� Ȯ��.
    rentSte=GetDVDRentState(ISBN);
    if(rentSte==RENTED) 
    {
        puts("�뿩 ���̶� �뿩�� �Ұ����մϴ�.");
        getchar();
        return;
    }
    
    // 3. �뿩 ���� ����.
    puts("�뿩�� �����մϴ�. �뿩 ������ �����մϴ�.");
    fputs("�뿩 ���� ID �Է�: ", stdout);
    gets(cusID);
    
    registCus=IsRegistID(cusID);
    if(registCus==0)
    {
        puts("���� ������ �ƴմϴ�.");
        getchar();
        return;
    }
    
    fputs("�뿩 ��¥ �Է�: ", stdout);
    scanf("%u", &rentDay);
    fflush(stdin);

    SetDVDRented(ISBN);
    AddRentList(ISBN, cusID, rentDay);

    puts("�뿩�� �Ϸ�Ǿ����ϴ�.");
    getchar();
}


/* ��    ��: void ReturnDVD(void)
 * ��    ��: ������ DVD�� �ݳ��ϴ� ���� ó��
 * ��    ȯ: void
 * 
 */
void ReturnDVD(void)
{
    char ISBN[ISBN_LEN];
    int registDVD;
    int rentSte;
    
    // 1. �ݳ��� DVD ISBN �Է� �� ����.
    fputs("�ݳ� DVD ISBN �Է�: ", stdout);
    gets(ISBN);
    
    registDVD=IsRegistISBN(ISBN);
    if(registDVD==0)
    {
        puts("��ϵ��� ���� ISBN �Դϴ�.");
        getchar();
        return;
    }    
    
    // 2. �ݳ��� ������ ������� Ȯ��.
    rentSte=GetDVDRentState(ISBN);
    if(rentSte==RETURNED)  // �뿩 ���̶��..
    {
        puts("�뿩���� ���� DVD �Դϴ�.");
        getchar();
        return;
    }
    
    // 3. �ݳ� ���� ����.
    SetDVDReturned(ISBN);
    puts("�ݳ��� �Ϸ�Ǿ����ϴ�.");
    getchar();
}


/* ��    ��: void ShowDVDRentAllCusInfo(void)
 * ��    ��: Ư�� DVD �뿩�� ��ü���� ���.
 * ��    ȯ: void
 * 
 */
void ShowDVDRentAllCusInfo(void)
{
    char ISBN[ISBN_LEN];
    dvdInfo * ptrDVDInfo;
    
    /* ã�� ����� ISBN �Է� */
    fputs("ã�� ISBN �Է�: ", stdout);
    gets(ISBN);
    
    ptrDVDInfo = GetDVDPtrByISBN(ISBN);
    if(ptrDVDInfo==0)
    {
        puts("�������� �ʴ� ISBN �Դϴ�.");
        getchar();
        return;
    }
    
    PrintOutRentAllCusInfo(ISBN);

    puts("��ȸ�� �Ϸ��Ͽ����ϴ�.");
    getchar();         
}

/* ��    ��: void ShowDVDCusAllRentInfo(void)
 * ��    ��: Ư�� ������ DVD �뿩���� ���.
 * ��    ȯ: void
 * 
 */
void ShowDVDCusAllRentInfo(void)
{
    char ID[ID_LEN];
    unsigned int rentStart;
    unsigned int rentEnd;
    cusInfo * ptrCusInfo;

    /* ã�� ����� ID �Է� */
    fputs("ã�� ID �Է�: ", stdout);
    gets(ID);

    ptrCusInfo = GetCusPtrByID(ID);
    if(ptrCusInfo==0)
    {
        puts("���Ե��� ���� ID �Դϴ�.");
        getchar();
        return;
    }
 
    fputs("�뿩 �Ⱓ �Է�: ", stdout);
    scanf("%u %u", &rentStart, &rentEnd);
    fflush(stdin);
    
    if(rentStart>rentEnd)
    {
        puts("�Ⱓ ������ �߸� �ԷµǾ����ϴ�.");
        getchar();
        return;
    }
    
    PrintOutCusAllRentInfo(ID, rentStart, rentEnd);
    puts("��ȸ�� �Ϸ��Ͽ����ϴ�.");
    getchar();
}

/* end of file */