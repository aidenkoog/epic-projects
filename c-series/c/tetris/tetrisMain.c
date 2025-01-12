
#include <Windows.h>
#include "common.h"
#include "keyCurControl.h"
#include "blockStageControl.h"
#include "scoreLevelControl.h"

#define START_CURPOS_X         (5*2)
#define START_CURPOS_Y         (0)

int main(void)
{
    /* ���� �ӵ� ���� */
    InitKeyDelayRate(5);

    /* Ŀ�� ������ ���� */
    RemoveCursor();

    /* ���� ���� �׸��� */
    DrawGameBoard();

    /* ���� ����, ���� ǥ�� */
    ShowCurrentScoreAndLevel();
    
    /* �ݺ������� ���ο� ������ ���� */
    while(1)
    {
        /* �� ������ ������ġ ���� */
        InitNewBlockPos(START_CURPOS_X, START_CURPOS_Y);
        
        /* ���� ���� */
        ChooseBlock();
        
        /* ���� ���� Ȯ�� */
        if(IsGameOver())
            break;
         
        /* ������ �۾� ���� */
        while(1)
        {
            /* ������ �Ʒ��� �� ĭ �̵� */
            if(BlockDown()==0) // ���� �̵� ���� �� 
            {
                //AddCurrentBlockInfoToBoard(); /* ȣ�� ��ġ�� �̵� */
                break;
            }
            
            /* ���̸� Ű �Է� ó�� */
            if(ProcessKeyInput())
                break;   // SPACE �� �Է� ��
        }
    }
    
    SetCurrentCursorPos(10, 10);
    puts("GAME OVER ^^");
    
    return 0;
}

/* end of file */