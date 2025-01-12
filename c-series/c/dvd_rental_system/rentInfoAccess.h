
#ifndef __RENTACCESS_H__
#define __RENTACCESS_H__

void AddRentList(char * ISBN, char * cusID, int rentDay);
void PrintOutRentAllCusInfo(char * ISBN);
void PrintOutCusAllRentInfo(char * ID, unsigned int start, unsigned int end);

void StoreRentListToFile(void);
void LoadRentListFromFile(void); 

#endif
/* end of file */