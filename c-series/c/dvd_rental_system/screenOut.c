
#include "common.h"
#include "cusInfo.h"
#include "dvdInfo.h"
#include "rentInfo.h"
#include "screenOut.h"

/* ���α׷� ����� ���� �޴� */
void ShowMenu(void)
{
    system("cls");   //stdlib.h
    
    printf("���������� ��  �� �������������� \n");
    printf(" 1. �ű԰��� \n");
    printf(" 2. �����˻� \n");
    printf("�������������������������������� \n");
    printf(" 3. DVD ��� \n");
    printf(" 4. DVD �˻� \n");
    printf("�������������������������������� \n");
    printf(" 5. DVD �뿩 \n");
    printf(" 6. DVD �ݳ� \n");
    printf("�������������������������������� \n");
    printf(" 7. DVD �뿩���� ��ü ��ȸ\n");
    printf(" 8. ���� �뿩 DVD ��ü ��ȸ\n");
    printf("�������������������������������� \n");
    printf(" 9. ���� \n");
    printf("�������������������������������� \n");
    printf("���á� ");
}


/* ���� ������ �⺻ ���� ��� */
void ShowCustomerInfo(cusInfo * pCus)
{
    printf("���������������������������������� \n");
    printf("�� �� ID: %s \n", pCus->ID);
    printf("�� �� �̸�: %s \n", pCus->name);
    printf("�� �� ��ȭ��ȣ: %s \n", pCus->phoneNum);
    printf("���������������������������������� \n\n");
    
    getchar();
}

/* ���� ������ �⺻ ���� ���(���� ��¿�) */
void ShowCustomerInfoContinue(cusInfo * pCus)
{
    printf("���������������������������������� \n");
    printf("�� �� ID: %s \n", pCus->ID);
    printf("�� �� �̸�: %s \n", pCus->name);
    printf("�� �� ��ȭ��ȣ: %s \n", pCus->phoneNum);
    printf("���������������������������������� \n\n");
}

/* DVD �⺻ ���� ��� */
void ShowDVDInfo(dvdInfo * pDVD)
{
    printf("���������������������������������� \n");
    printf("�� �� ISBN: %s \n", pDVD->ISBN);
    printf("�� �� Ÿ��Ʋ: %s \n", pDVD->title);
    printf("�� �� ����:  "), ShowGenre(pDVD->genre), puts("");
    printf("���������������������������������� \n\n");
    
    getchar();
}

/* DVD�� �帣 ���� ��� */
void ShowGenre(int gen)
{
    switch(gen)
    {
    case ACTION:
        fputs("�׼�", stdout);
        break;
    case COMIC:
        fputs("�ڹ�", stdout);
        break;
    case SF:
        fputs("SF", stdout);
        break;
    case ROMANTIC:
        fputs("�θǽ�", stdout);
        break;
    }
}

/* �뿩 DVD �⺻ ���� ��� */
void ShowDVDRentInfo(dvdRentInfo dvd)
{
    printf("���������������������������������� \n");
    printf("�� �� ISBN: %s \n", dvd.ISBN);
    printf("�� �� �뿩��: %u \n", dvd.rentDay);
    printf("���������������������������������� \n\n");
}

/* end of file */