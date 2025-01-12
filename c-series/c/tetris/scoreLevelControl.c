
#include "common.h"
#include "keyCurControl.h"

#define LEVEL_DIFF   2    // �ܰ躰 �ӵ� ���� ����
#define LEVER_UP_SCORE_DIFF  20   // ������ �����ϴ� ���ھ� ���� ����

static int curGameLevel=1;
static int curGameScore=0;

/* ��    ��: void ShowCurrentScoreAndLevel(void)
 * ��    ��: ������ ���� ���� ���
 * ��    ȯ: void
 *
 */
void ShowCurrentScoreAndLevel(void)
{
    SetCurrentCursorPos(30, 4);
    printf("�� ���� ����: %d    ��", curGameLevel);
    
    SetCurrentCursorPos(30, 7);
    printf("�� ���� ����: %d    ��", curGameScore);
}

/* ��    ��: void GameLevelUp(void)
 * ��    ��: ���� ���� ���
 * ��    ȯ: void
 *
 */
void GameLevelUp(void)
{
    curGameLevel++;
    KeyDelaySpeedCtl(LEVEL_DIFF);
}

/* ��    ��: void AddGameScore(int score)
 * ��    ��: ���� ���� �߰�
 * ��    ȯ: void
 *
 */
void AddGameScore(int score)
{
    if(score<1)
        return;
    
    curGameScore+=score;
    
    /* ���� ��� Ȯ�� */
    if(curGameScore >= curGameLevel*LEVER_UP_SCORE_DIFF)
        GameLevelUp();
}

