
#ifndef __SCREENOUT_H__
#define __SCREENOUT_H__

#include "cusInfo.h"
#include "dvdInfo.h"
#include "rentInfo.h"

void ShowMenu(void);
void ShowCustomerInfo(cusInfo * pCus);
void ShowCustomerInfoContinue(cusInfo * pCus);
void ShowDVDInfo(dvdInfo * pDVD);
void ShowGenre(int gen);
void ShowDVDRentInfo(dvdRentInfo dvd);

#endif

/* end of file */