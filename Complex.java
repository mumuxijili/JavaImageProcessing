/**
 * 复数类
 * @author cv
 *
 */
public class Complex {
	public double realPart;
	public double imaginaryPart;
	
	public Complex()
	{
		this.realPart = 0;
		this.imaginaryPart = 0;
	}
	
	public Complex(double realPart)
	{
		this.realPart = realPart;
		this.imaginaryPart = 0;
	}
	
	public Complex(double realPart,double imaginaryPart)
	{
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}
	
	public void setrealPart(double realPart)
	{
		this.realPart = realPart;
	}
	
	public void setimaginaryPart(double imaginaryPart)
	{
		this.imaginaryPart = imaginaryPart;
	}
	
	public double getrealPart()
	{
		return this.realPart;
	}
	
	public double getimaginaryPart()
	{
		return this.imaginaryPart;
	}
	
	//加法	
	public Complex Add(Complex c1,Complex c2)
	{
		Complex c = new Complex(0,0);
		c.realPart = c1.realPart + c2.realPart;
		c.imaginaryPart = c1.imaginaryPart + c2.imaginaryPart;
		return c;
	}
	
	//减法
	public Complex Sub(Complex c1,Complex c2)
	{
		Complex c = new Complex(0,0);
		c.realPart = c1.realPart - c2.realPart;
		c.imaginaryPart = c1.imaginaryPart - c2.imaginaryPart;
		return c;
	}
	
	//乘法
	public Complex Mul(Complex c1,Complex c2)
	{
		Complex c = new Complex(0,0);
		c.realPart = c1.realPart * c2.realPart - c1.imaginaryPart * c2.imaginaryPart;
		c.imaginaryPart = c1.realPart * c2.imaginaryPart + c2.realPart * c1.imaginaryPart;
		return c;
	}
}
