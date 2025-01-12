
#ifndef __CUSACCESS_H__
#define __CUSACCESS_H__

#include "cusInfo.h"

int AddCusInfo(char * ID, char * name, char * num);
cusInfo * GetCusPtrByID(char * ID);
int IsRegistID(char * ID);

void StoreCusListToFile(void);
void LoadCusListFromFile(void);

#endif

/* end of file */