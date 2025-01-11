
#include "common.h"
#include "gameTimes.h"
#include "gameMoney.h"

#define FILE_NAME     "gamecont_info.dat"
#define FILE_DEL_CMD     "del gamecont_info.dat"

/* ��    ��: int AvailContinue(void)
 * ��    ��: �̾ �� �����Ͱ� �����ϴ°�. 
 * ��    ȯ: YES 1, NO 0
 *
 */
int AvailContinue(void)
{
    FILE * fp;
    fp=fopen(FILE_NAME, "rb");
    if(fp==NULL)
    {
        return 0;
    }
    else
    {
        fclose(fp);
        return 1;
    }
}

/* ��    ��: int DoYouWantToContinue(void)
 * ��    ��: ���� ���� ���� LOAD. 
 * ��    ȯ: �̾ �� ��� 1�� ��ȯ
 *
 */
int DoYouWantToContinue(void)
{
    FILE * fp;
    int conti;
    
    if(!AvailContinue())
        return 0;
    
    fputs("������ �̾ �Ͻðڽ��ϱ�? (Yes 1, No 0): ", stdout);
    scanf("%d", &conti);
    
    if(conti==0)
        return 0;
    
    fp=fopen(FILE_NAME, "rb");
    if(fp==NULL)
    {
        puts("�̾ �� �����Ͱ� �������� �ʽ��ϴ�");
        return 0;
    }

    // ���� �� ��� �� LOAD
    LoadGameTimesFromFile(fp);
    
    // ���� �Ӵ� ���� LOAD
    LoadGameMoneyFromFile(fp);
    fclose(fp);
    
    return 1;
}

/* ��    ��: int DoYouWantToStore(void)
 * ��    ��: ���� ���� ���� STORE. 
 * ��    ȯ: ����� 1�� ��ȯ
 *
 */
int DoYouWantToStore(void)
{
    FILE * fp;
    int cont;
    
    fputs("���� ������ �����Ͻðڽ��ϱ�? (Yes 1, No 0): ", stdout);
    scanf("%d", &cont);
    
    if(cont==0)
        return 0;
    
    fp=fopen(FILE_NAME, "wb");
    
    // ���� �� ��� �� STORE
    StoreGameTimesToFile(fp);

    // ���� �Ӵ� ���� STORE
    StoreGameMoneyToFile(fp);
    
    fclose(fp);
    return 1;
}

/* ��    ��: void ClearContinueInfo(void)
 * ��    ��: �̾ �����ϱ� ���� ���� ����. 
 * ��    ȯ: void
 *
 */
void ClearContinueInfo(void)
{
    // ���� ����
    FILE * fp;
    fp=fopen(FILE_NAME, "rb");
    
    if(fp!=NULL)
    {
        fclose(fp);
        system(FILE_DEL_CMD);
    }
}
/* end of file */