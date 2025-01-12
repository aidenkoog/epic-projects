
#include <time.h>
#include "common.h"
#include "point.h"
#include "blockInfo.h"
#include "keyCurControl.h"
#include "blockStageControl.h"
#include "scoreLevelControl.h"

#define  GBOARD_WIDTH    10
#define  GBOARD_HEIGHT   20

#define  GBOARD_ORIGIN_X  4
#define  GBOARD_ORIGIN_Y  2

static int gameBoardInfo[GBOARD_HEIGHT+1][GBOARD_WIDTH+2]={0,};

static int currentBlockModel;
static int curPosX, curPosY;
static int rotateSte;

/* ��    ��: void InitNewBlockPos(int x, int y)
 * ��    ��: ������ ù ��ġ ����
 * ��    ȯ: void
 *
 */
void InitNewBlockPos(int x, int y)
{
    if(x<0 || y<0)
        return;
   
    curPosX=x;
    curPosY=y;

    SetCurrentCursorPos(x, y);
}

/* ��    ��: void ChooseBlock(void)
 * ��    ��: ����� ������ ������ ����
 * ��    ȯ: void
 *
 */
void ChooseBlock(void)
{
    srand((unsigned int)time(NULL));
    
    currentBlockModel = (rand() % NUM_OF_BLOCK_MODEL) * 4;
}

/* ��    ��: int GetCurrentBlockIdx(void)
 * ��    ��: ���� ����ؾ� �ϴ� ������ index ���� ��ȯ
 * ��    ȯ: int
 *
 */
int GetCurrentBlockIdx(void)
{
    return currentBlockModel + rotateSte;
}

/* ��    ��: void ShowBlock(char blockInfo[][4])
 * ��    ��: ���޵� ���ڸ� �����Ͽ� ���� ���
 * ��    ȯ: void
 *
 */
void ShowBlock(char blockInfo[][4])
{
    int y, x;
    point curPos=GetCurrentCursorPos();

    for(y=0; y<4; y++)
    {
        for(x=0; x<4; x++)
        {
            SetCurrentCursorPos(curPos.x+x*2, curPos.y+y);
            
            if(blockInfo[y][x] == 1)
                printf("��");
        }
    }
    SetCurrentCursorPos(curPos.x, curPos.y);
}

/* ��    ��: void DeleteBlock(char blockInfo[][4])
 * ��    ��: ���� ��ġ�� ��µ� ���� ����
 * ��    ȯ: void
 *
 */
void DeleteBlock(char blockInfo[][4])
{
    int y, x;
    point curPos=GetCurrentCursorPos();
    
    for(y=0; y<4; y++)
    {
        for(x=0; x<4; x++)
        {
            SetCurrentCursorPos(curPos.x+x*2, curPos.y+y);
            
            if(blockInfo[y][x] == 1)
                printf("  ");
        }
    }
    SetCurrentCursorPos(curPos.x, curPos.y);
}

/* ��    ��: int DetectCollision(int posX, int posY, char blockModel[][4])
 * ��    ��: ������ �̵� �� ȸ�� ���� ���� �Ǵ�
 * ��    ȯ: �̵� �� ȸ�� ���ɽ� 1 ��ȯ
 *
 */
int DetectCollision(int posX, int posY, char blockModel[][4])
{
    int x, y;

    /* gameBoardInfo �迭�� ��ǥ�� ���� */
    int arrX= (posX-GBOARD_ORIGIN_X)/2;
    int arrY= posY-GBOARD_ORIGIN_Y;

    /* �浹 �˻� */
    for(x=0; x<4; x++)
    {
        for(y=0; y<4; y++)
        {
            /* Short Circuit Evaluation�� ���� �迭 �Ϻθ� �˻� */
            if(blockModel[y][x]==1 && gameBoardInfo[arrY+y][arrX+x]==1)
                return 0;
        }
    }
    
    return 1;
}

/* ��    ��: void DrawSolidBlocks(void)
 * ��    ��: ���� �ǿ� ������ ������ �׸���.
 * ��    ȯ: void
 *
 */
void DrawSolidBlocks(void)
{
    int x, y;
    int cursX, cursY;
    
    for(y=0; y<GBOARD_HEIGHT; y++)
    {
        for(x=1; x<GBOARD_WIDTH+1; x++)
        {
            cursX= x*2 + GBOARD_ORIGIN_X;
            cursY= y + GBOARD_ORIGIN_Y;
            SetCurrentCursorPos(cursX, cursY);
            
            if(gameBoardInfo[y][x]==1)
            {
                printf("��");
            }
            else
            {
                printf("  ");
            }
        }
    }
}

/* ��    ��: void RemoveFillUpLine(void)
 * ��    ��: �� ������ ä���� ������ �����Ѵ�.
 * ��    ȯ: void
 *
 */
void RemoveFillUpLine(void)
{
    int x, y;
    int line;
    
    for(y=GBOARD_HEIGHT-1; y>0; y--)
    {
        for(x=1; x<GBOARD_WIDTH+1; x++)
        {
            if(gameBoardInfo[y][x]!=1)
                break;
        }
        
        if(x==(GBOARD_WIDTH+1))  // ������ �� ä�����ٸ�
        {
            for(line=0; y-line>0; line++)
            {
                memcpy(
                    &gameBoardInfo[y-line][1], &gameBoardInfo[(y-line)-1][1], 
                    GBOARD_WIDTH * sizeof(int)
                );
            }
            
            y++; // �迭 ������ �Ʒ��� �� ĭ�� �̵������Ƿ�
            
            AddGameScore(10);
            ShowCurrentScoreAndLevel();
        }
    }
    
    DrawSolidBlocks();
}

/* ��    ��: int BlockDown(void)
 * ��    ��: ����Ϳ� �׷��� ������ �Ʒ��� �� ĭ ����
 * ��    ȯ: ���� �� 1, ���� �� 0
 *
 */
int BlockDown(void)
{
    if(!DetectCollision(curPosX, curPosY+1, blockModel[GetCurrentBlockIdx()]))
    {
        /* �� ������ ä���� ���� ���� �˻�*/
        AddCurrentBlockInfoToBoard();
        RemoveFillUpLine();
        return 0;
    }

    DeleteBlock(blockModel[GetCurrentBlockIdx()]);
    curPosY+=1;
    
    SetCurrentCursorPos(curPosX, curPosY);
    ShowBlock(blockModel[GetCurrentBlockIdx()]);

    return 1;
}

/* ��    ��: void ShiftLeft(void)
 * ��    ��: ������ �������� �� ĭ �̵�
 * ��    ȯ: void
 *
 */
void ShiftLeft(void)
{
    if(!DetectCollision(curPosX-2, curPosY, blockModel[GetCurrentBlockIdx()]))
      return;
 
    DeleteBlock(blockModel[GetCurrentBlockIdx()]);
    curPosX-=2;
    
    SetCurrentCursorPos(curPosX, curPosY);
    ShowBlock(blockModel[GetCurrentBlockIdx()]);
}

/* ��    ��: void ShiftRight(void)
 * ��    ��: ������ ���������� �� ĭ �̵�
 * ��    ȯ: void
 *
 */
void ShiftRight(void)
{
    if(!DetectCollision(curPosX+2, curPosY, blockModel[GetCurrentBlockIdx()]))
        return;
 
    DeleteBlock(blockModel[GetCurrentBlockIdx()]);
    curPosX+=2;
    
    SetCurrentCursorPos(curPosX, curPosY);
    ShowBlock(blockModel[GetCurrentBlockIdx()]);
}

/* ��    ��: void RotateBlock(void)
 * ��    ��: ������ 90�� ȸ��
 * ��    ȯ: void
 *
 */
void RotateBlock(void)
{
    int nextRotSte;
    int beforeRotSte=rotateSte;  // ������ ���� ����
    
    DeleteBlock(blockModel[GetCurrentBlockIdx()]);

    nextRotSte=rotateSte+1;
    nextRotSte%=4;
    rotateSte=nextRotSte;

    if(!DetectCollision(curPosX, curPosY, blockModel[GetCurrentBlockIdx()]))
    {
        rotateSte=beforeRotSte;
        return;
    }
    
    SetCurrentCursorPos(curPosX, curPosY);
    ShowBlock(blockModel[GetCurrentBlockIdx()]);
}

/* ��    ��: void SolidCurrentBlock(void)
 * ��    ��: ������ ������ �ٴ����� �̵����� ������.
 * ��    ȯ: void
 *
 */
void SolidCurrentBlock(void)
{
    while(BlockDown());
}

/* ��    ��: void DrawGameBoard(void)
 * ��    ��: ���� ���� ��� ���� �׸���.
 * ��    ȯ: void
 *
 */
void DrawGameBoard(void)
{
    int x, y;

    /* �ð����� �κ� ó�� */
    for(y=0; y<=GBOARD_HEIGHT; y++)
    {
        SetCurrentCursorPos(GBOARD_ORIGIN_X, GBOARD_ORIGIN_Y+y);

        if(y==GBOARD_HEIGHT)
            printf("��");
        else
            printf("��");
    }
    
    for(y=0; y<=GBOARD_HEIGHT; y++)
    {
        SetCurrentCursorPos(GBOARD_ORIGIN_X+(GBOARD_WIDTH+1)*2, GBOARD_ORIGIN_Y+y);

        if(y==GBOARD_HEIGHT)
            printf("��");
        else
            printf("��");
    }
    
    for(x=1; x<GBOARD_WIDTH+1; x++)
    {
        SetCurrentCursorPos(GBOARD_ORIGIN_X+x*2, GBOARD_ORIGIN_Y+GBOARD_HEIGHT);
        printf("��");
    }

    SetCurrentCursorPos(0, 0);	

    /* ������ �κ� ó�� */
    for(y=0; y<GBOARD_HEIGHT; y++)
    {
        gameBoardInfo[y][0]=1;
        gameBoardInfo[y][GBOARD_WIDTH+1]=1;
    }
    
    for(x=0; x<GBOARD_WIDTH+2; x++)
    {
        gameBoardInfo[GBOARD_HEIGHT][x]=1;
    }
}

/* ��    ��: void AddCurrentBlockInfoToBoard(void)
 * ��    ��: �迭�� ���� ������ ������ �߰��Ѵ�.
 * ��    ȯ: void
 *
 */
void AddCurrentBlockInfoToBoard(void)
{
    int x, y;

    int arrCurX;
    int arrCurY;
    
    for(y=0; y<4; y++)
    {
        for(x=0; x<4; x++)
        {
            /* Ŀ�� ��ġ ������ �迭 index ������ ���� */
            arrCurX=(curPosX-GBOARD_ORIGIN_X)/2;
            arrCurY=curPosY-GBOARD_ORIGIN_Y;

            if(blockModel[GetCurrentBlockIdx()][y][x]==1)
                gameBoardInfo[arrCurY+y][arrCurX+x]=1;
        }
    }
}

/* ��    ��: int IsGameOver(void)
 * ��    ��: ������ ����Ǿ����� Ȯ���ϴ� �Լ�
 * ��    ȯ: ���� ���� �� 1 ��ȯ
 *
 */
int IsGameOver(void)
{
    if(!DetectCollision(curPosX, curPosY, blockModel[GetCurrentBlockIdx()]))
        return 1;
    else
        return 0;	 
}

/* end of file */