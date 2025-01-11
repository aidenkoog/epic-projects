

#ifndef __GAME_H__
#define __GAME_H__

enum {SCISSORS=1, ROCK, PAPER, EXIT};

int ChoiceOfCom(void);
int ChoiceOfMe(void);
void WhoIsWinner(int com, int you);
void ShowRSP(int rsp);

#endif
/* end of file */