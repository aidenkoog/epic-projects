
#ifndef __DVDINFO_H__
#define __DVDINFO_H__

#include "cusInfo.h"

#define ISBN_LEN   30
#define TITLE_LEN  30

enum {ACTION=1, COMIC, SF, ROMANTIC};    // �帣 ����
enum {RENTED, RETURNED};    // �뿩 ���¸� ��Ÿ���� ���!

typedef struct __dvdInfo 
{
    char ISBN[ISBN_LEN];
    char title[TITLE_LEN];
    int genre;
    int rentState;
}dvdInfo;

#endif
/* end of file */