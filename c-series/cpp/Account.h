
#ifndef __ACCOUNT_H__
#define __ACCOUNT_H__

#include "String.h"

class Account
{
private:
	int accID; 
	int balance;    
	String cusName; // char * cusName;
public:
	Account(int ID, int money, String name);
	int GetAccID() const;
	virtual void Deposit(int money);
	int Withdraw(int money) ;
	void ShowAccInfo() const ;
};
#endif