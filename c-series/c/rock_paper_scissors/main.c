

#include "common.h"
#include "game.h"
#include "gameTimes.h"
#include "gameMoney.h"
#include "gameContinue.h"

int main(void)
{
    int com;
    int you;
    int youMoney;
    int tableMoney;
    int ret;

    //puts("��! ������ �����մϴ�.");
    //puts("");
    //
    //SetComMoney(1000);
    //
    //fputs("�� ����� �Ӵϸ� �Է��ϼ���: ", stdout);
    //scanf("%d", &youMoney);
    //if(youMoney<=0)
	//{
    //    puts("����ġ ���� �Է����� ���α׷��� �����մϴ�.");
    //    return -1;
    //}
    //SetYouMoney(youMoney);

    if(!DoYouWantToContinue())
    {
        puts("��! ������ �����մϴ�.");
        SetComMoney(1000);
		
        fputs("�� ����� �Ӵϸ� �Է��ϼ���: ", stdout);
        scanf("%d", &youMoney);
        if(youMoney<=0)
        {
            puts("����ġ ���� �Է����� ���α׷��� �����մϴ�.");
            return -1;
        }
        SetYouMoney(youMoney);
	}

    printf("����� ���� �Ӵ�: %d \n", GetCurrentYouMoney());
    printf("��ǻ�� ���� �Ӵ�: %d \n\n", GetCurrentComMoney());

    while(1)
    {    
        puts("���ǵ� �����մϴ�!��");
        fputs("�ǵ� �Է�: ", stdout);
        scanf("%d", &tableMoney);
        puts("");

        ret = SetGamblingTableMoneyByGamer(tableMoney);
        if(ret==0 || ret==-1)  
        {
            if(ret==0) 
            {
                puts("�����ϰ� �ִ� �ݾ� ������ �ż���!");
                printf("����� ���� ���� �Ӵ�: %d \n", GetCurrentYouMoney());
            }
            else
            {
                puts("��ǻ�� ���� �ݾ� �ʰ��Դϴ�!");
                printf("��ǻ���� ���� ���� �Ӵ�: %d \n", GetCurrentComMoney());
            }
            continue;
        }
        
        puts("�١١١١١� ���! �١١١١١�!!");
        com=ChoiceOfCom();
        you=ChoiceOfMe();
        puts("");
        
        if(you==EXIT)
            break;
   
        puts("�ڡڡڡڡڡ� ���! �ڡڡڡڡڡ�!!");
        WhoIsWinner(com, you);
    
        fputs("��ǻ���� ����: ", stdout);
        ShowRSP(com);
        puts("");

        fputs("����� ����: ", stdout);
        ShowRSP(you);
        puts("");

        printf("�·�: %d %% \n", GetPercenOfVictory());
        printf("���̸� �Ӵ�: %d \n", GetCurrentYouMoney());
        printf("��ǻ�� �Ӵ�: %d \n\n", GetCurrentComMoney());

        if(GetCurrentComMoney()<=0 || GetCurrentYouMoney()<=0)
            break;
    }
    
    puts("�ޡޡޡޡޡ� ���� ��� �ޡޡޡޡޡ�");
    printf("���� �·�: %d %% \n", GetPercenOfVictory());

    if(GetCurrentComMoney()!=0 && GetCurrentYouMoney()!=0)
        DoYouWantToStore();  // ���ӳ��� ����.
    else
        ClearContinueInfo();

    puts("�̿��� �ּż� ��������~ ");
    return 0;
}