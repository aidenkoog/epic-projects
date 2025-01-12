
#ifndef __RENTINFO_H__
#define __RENTINFO_H__

#include "dvdInfo.h"
#include "cusInfo.h"

typedef struct __dvdRentInfo 
{
    char cusID[ID_LEN];    // �뿩 ���� ID
    char ISBN[ISBN_LEN];
    unsigned int rentDay;    // �뿩��
} dvdRentInfo;

#endif
/* end of file */