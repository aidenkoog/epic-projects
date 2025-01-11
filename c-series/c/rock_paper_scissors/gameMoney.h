

#ifndef __GAMEMONEY_H__
#define __GAMEMONEY_H__

void SetComMoney(int money);
int GetCurrentComMoney(void);

void SetYouMoney(int money);
int GetCurrentYouMoney(void);

void SetGamblingTableMoney(int money);
int SetGamblingTableMoneyByGamer(int money);

void ComWinMoneyAccount(void);
void YouWinMoneyAccount(void);

void StoreGameMoneyToFile(FILE * fp);
void LoadGameMoneyFromFile(FILE * fp);


#endif

/* end of file */