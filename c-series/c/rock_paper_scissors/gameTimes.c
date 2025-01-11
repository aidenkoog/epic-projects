
#include "common.h"

static int gameNumOfTimes=0;  
static int youWinNumOfTimes=0;

/* ��    ��: void IncreGameTimes(void)
 * ��    ��: ���� ���� �� ����.
 * ��    ȯ: void.
 *
 */
void IncreGameTimes(void)
{
    gameNumOfTimes++;
}

/* ��    ��: int GetGameTimes(void)
 * ��    ��: ���� ���� �� ��ȯ.
 * ��    ȯ: int.
 *
 */
int GetGameTimes(void)
{
    return gameNumOfTimes;
}

/* ��    ��: void IncreYouWinTimes(void)
 * ��    ��: ���̸��� ���� ��� �� ����.
 * ��    ȯ: void.
 *
 */
void IncreYouWinTimes(void)
{
    youWinNumOfTimes++;
}

/* ��    ��: int GetYouWinTimes(void)
 * ��    ��: ���̸��� ���� ��� �� ��ȯ.
 * ��    ȯ: int.
 *
 */
int GetYouWinTimes(void)
{
    return youWinNumOfTimes;
}

/* ��    ��: int GetPercenOfVictory(void)
 * ��    ��: ���̸��� �·� ���.
 * ��    ȯ: ���̸� �·�.
 *
 */
int GetPercenOfVictory(void)
{
    if(gameNumOfTimes==0)
        return 0;

    // ��ȯ�� double���� int�� ������ ����ȯ �߻�.
    return (double)youWinNumOfTimes/gameNumOfTimes*100;
}


/* ��    ��: void StoreGameTimesToFile(FILE * fp)
 * ��    ��: ���� ��, ��� �� ���Ͽ� ����.
 * ��    ȯ: void.
 *
 */
void StoreGameTimesToFile(FILE * fp)
{
    fwrite(&gameNumOfTimes, sizeof(int), 1, fp);
    fwrite(&youWinNumOfTimes, sizeof(int), 1, fp);
}

/* ��    ��: void LoadGameTimesFromFile(FILE * fp)
 * ��    ��: ���� ��, ��� �� ���Ϸκ��� ����.
 * ��    ȯ: void.
 *
 */
void LoadGameTimesFromFile(FILE * fp)
{
    int readCnt1, readCnt2;
    
    readCnt1 = fread(&gameNumOfTimes, sizeof(int), 1, fp);
    readCnt2 = fread(&youWinNumOfTimes, sizeof(int), 1, fp);
    
    /* ������ ���� ó�� */
    if(readCnt1!=1 || readCnt2!=1)
    {
        gameNumOfTimes=0;
        youWinNumOfTimes=0;
    }
}

/* end of file */