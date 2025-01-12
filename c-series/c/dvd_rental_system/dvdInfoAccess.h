
#ifndef __DVDACCESS_H__
#define __DVDACCESS_H__

#include "dvdInfo.h"

int AddDVDInfo(char * ISBN, char * title, int genre);
dvdInfo * GetDVDPtrByISBN(char * ISBN);
int IsRegistISBN(char * ISBN);

/* �߰��� �Լ����� ���� */
int SetDVDRented(char * ISBN);
int SetDVDReturned(char * ISBN);
int GetDVDRentState(char * ISBN);

void StoreDVDListToFile(void);
void LoadDVDListFromFile(void);

#endif
/* end of file */