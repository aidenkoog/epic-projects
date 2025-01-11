

#include "common.h"
#include "screenOut.h"
#include "phoneFunc.h"

enum {INPUT=1, SHOWALL, SEARCH, DELETE, CHANGE, QUIT};

/* ��    ��: int main (void)
 * ��    ��: ����� ���� ó��. 
 * ��    ȯ: ���� ���� �� 0.
 *
 */
int main(void)
{
    int inputMenu = 0;

    /* ���α׷� ���۰� ���ÿ� ������ Load! */
    //LoadDataFromFile();
    LoadDataFromFileInStruct();
    
    while(1)
    {
        ShowMenu();
        fputs("�����ϼ���: ", stdout);
        scanf("%d", &inputMenu);
        fflush(stdin);
  		        
        switch(inputMenu)
        {
        case INPUT:
            InputPhoneData();
            break;
        
        case SHOWALL:
            ShowAllData();
            break;

        case SEARCH:
            SearchPhoneData();
            break;

        case DELETE:
            DeletePhoneData();
            break;        
        
        case CHANGE:
            ChangePhoneData();
            break;
        }
		
        if(inputMenu==QUIT)
        {
            /* ���α׷� ����� ������ Store! */
            // StoreDataToFile();
            StoreDataToFileInStruct();
            puts("�̿��� �ּż� ��������~");
            break;
        }
    }    
    return 0;
}

/* end of file */