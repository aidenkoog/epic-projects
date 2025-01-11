#include<stdio.h>

typedef struct _Complex
{
	double real;
	double imag;
} Complex;

Complex Add(Complex c1, Complex c2);
Complex Multi(Complex c1, Complex c2);
void ShowData(Complex c);

int main(void)
{
	Complex c1, c2;
	printf("복소수 입력1(실수 허수): ");
	scanf("%lf %lf", &c1.real, &c1.imag);

	printf("복소수 입력2(실수 허수): ");
	scanf("%lf %lf", &c2.real, &c2.imag);

	printf("합의 결과] ");
	ShowData(Add(c1, c2));

	printf("곱의 결과] ");
	ShowData(Multi(c1, c2));
	return 0;
}

Complex Add(Complex c1, Complex c2)
{
	Complex temp;
	temp.real=c1.real+c2.real;
	temp.imag=c1.imag+c2.imag;
	return temp;
}

Complex Multi(Complex c1, Complex c2)
{
	Complex temp;
	temp.real=c1.real*c2.real-c1.imag*c2.imag;
	temp.imag=c1.real*c2.imag+c1.imag*c2.real;
	return temp;
}

void ShowData(Complex c)
{
	printf("실수부: %f, 허수부: %f \n", c.real, c.imag);
}