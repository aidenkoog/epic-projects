
#ifndef __GAMETIMES_H__
#define __GAMETIMES_H__

void IncreGameTimes(void);
int GetGameTimes(void);

void IncreYouWinTimes(void);
int GetYouWinTimes(void);

int GetPercenOfVictory(void);

void StoreGameTimesToFile(FILE * fp);
void LoadGameTimesFromFile(FILE * fp);

#endif

/* end of file */