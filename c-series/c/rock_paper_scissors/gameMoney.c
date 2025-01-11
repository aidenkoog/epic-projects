
#include "common.h"

static int moneyCom;   // ��ǻ�� ���� �Ӵ�
static int moneyYou;   // ���̸� ���� �Ӵ�

static int gamblingTableMoney;

/* ��    ��: void SetComMoney(int money)
 * ��    ��: ��ǻ�� �Ӵ� ����.
 * ��    ȯ: void.
 *
 */
void SetComMoney(int money)
{
    if(money<0)
    {
        // ������ �˸��� ���� �޽��� ��� ����.
        return;
    }
    
    moneyCom=money;
}

/* ��    ��: int GetCurrentComMoney(void)
 * ��    ��: ��ǻ�� �Ӵ� �ܾ� ��ȯ.
 * ��    ȯ: int.
 *
 */
int GetCurrentComMoney(void)
{
    return moneyCom;
}

/* ��    ��: void SetYouMoney(int money)
 * ��    ��: ���̸� �Ӵ� ����.
 * ��    ȯ: void.
 *
 */
void SetYouMoney(int money)
{
    if(money<0)
    {
        // ������ �˸��� ���� �޽��� ��� ����.
        return;
    }
    
    moneyYou=money;
}

/* ��    ��: int GetCurrentYouMoney(void)
 * ��    ��: ���̸� �Ӵ� �ܾ� ��ȯ.
 * ��    ȯ: int.
 *
 */
int GetCurrentYouMoney(void)
{
    return moneyYou;
}

/* ��    ��: void SetGamblingTableMoney(int money)
 * ��    ��: �ǵ��� ũ�� ����.
 * ��    ȯ: void
 *
 */
void SetGamblingTableMoney(int money)
{
    if(money<0)
        gamblingTableMoney=0;
    else
        gamblingTableMoney=money;
}

/* ��    ��: void ComWinMoneyAccount(void)
 * ��    ��: ��ǻ�� �¸��� �Ӵ� ����.
 * ��    ȯ: void
 *
 */
void ComWinMoneyAccount(void)  
{
    moneyYou-=gamblingTableMoney;
    moneyCom+=gamblingTableMoney;
}

/* ��    ��: void YouWinMoneyAccount(void)
 * ��    ��: ���̸� �¸��� �Ӵ� ����.
 * ��    ȯ: void
 *
 */
void YouWinMoneyAccount(void)
{
    moneyCom-=gamblingTableMoney;
    moneyYou+=gamblingTableMoney;
}

/* ��    ��: int SetGamblingTableMoneyByGamer(int money)
 * ��    ��: �ǵ��� ũ�⸦ ���ѵ� ���� ������ ����.
 * ��    ȯ: ���� �Է½� 1 ��ȯ.
 *
 */
int SetGamblingTableMoneyByGamer(int money)
{
    if(money>moneyYou)
        return 0;   
    else if(money>moneyCom)
        return -1;  
    
    SetGamblingTableMoney(money);
    return 1; 
}

/* ��    ��: void StoreGameMoneyToFile(FILE * fp)
 * ��    ��: ��ǻ�Ϳ� ���̸��� ���� �Ӵ� ���Ͽ� ����.
 * ��    ȯ: void.
 *
 */
void StoreGameMoneyToFile(FILE * fp)
{
    fwrite(&moneyCom, sizeof(int), 1, fp);
    fwrite(&moneyYou, sizeof(int), 1, fp);
}

/* ��    ��: void LoadGameMoneyFromFile(FILE * fp)
 * ��    ��: ��ǻ�Ϳ� ���̸��� �Ӵ� ���� ���Ϸκ��� ����.
 * ��    ȯ: void.
 *
 */
void LoadGameMoneyFromFile(FILE * fp)
{
    int readCnt1, readCnt2;
    
    readCnt1 = fread(&moneyCom, sizeof(int), 1, fp);
    readCnt2 = fread(&moneyYou, sizeof(int), 1, fp);

    /* ������ ���� ó�� */
    if(readCnt1!=1 || readCnt2!=1)
    {
		// ���� �߻��� ������ 1000������ ����.
        moneyCom=1000;
        moneyYou=1000;
    }
}


/* end of file */