
#include <time.h>
#include "common.h"
#include "game.h"
#include "gameTimes.h"
#include "gameMoney.h"

/* ��    ��: int ChoiceOfCom(void).
 * ��    ��: ������ ���� ��ȯ.
 * ��    ȯ: 1~3 ������ ���� ��ȯ.
 *
 */
int ChoiceOfCom(void) 
{
    srand((unsigned int)time(NULL));
    return rand()%3+1;
}

/* ��    ��: int ChoiceOfMe(void).
 * ��    ��: ������� ������ �Է¹���.
 * ��    ȯ: ����� �Է�.
 *
 */
int ChoiceOfMe(void)
{
    int choice;

    fputs("����(1) ����(2) ��(3) ����(4) ����? ", stdout);

    scanf("%d", &choice);
    return choice;
}

/* ��    ��: void WhoIsWinner(int com, int you)
 * ��    ��: ���ڸ� ����. 
 * ��    ȯ: void
 *
 */
void WhoIsWinner(int com, int you)
{
    int outcome=you-com;
    IncreGameTimes(); 

    switch(outcome)
    {
    case 0:     
        puts("�����ϴ�.");
        return;
    
    case  1: 
    case -2:
        puts("����� �����Դϴ�.");
        IncreYouWinTimes();
        YouWinMoneyAccount();
        return;
    }
    
    puts("��ǻ�Ͱ� �����̱���!");
    ComWinMoneyAccount();
}

/* ��    ��: void ShowRSP(int rsp)
 * ��    ��: ����� ���� ���ڿ�(����, ����, ��) ���. 
 * ��    ȯ: void
 *
 */
void ShowRSP(int rsp)
{
    switch(rsp)
    {
    case SCISSORS:
        fputs("����", stdout);
        break;
    case ROCK:
        fputs("����", stdout);
        break;
    case PAPER:
        fputs("��", stdout);
        break;
    }
}

/* end of file */