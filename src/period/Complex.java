package period;

class Complex implements Comparable {
  double re;
  double im;

  public Complex(double _re, double _im) {
    re = _re;
    im = _im;
  }

  public double real() {
    return re;
  }

  public double imaginary() {
    return im;
  }

  public Complex conjugate() {
    return new Complex(re, -im);
  }

  public Complex scale(double s) {
    return new Complex(re*s, im*s);
  }

  public int compareTo(Object o) {
    Complex c = (Complex)o;
    return re != c.re ?
      new Double(re).compareTo(new Double(c.re)) :
      new Double(im).compareTo(new Double(c.im)) ;
  }

  public String toString() {
    return re + " + i" + im;
  }

  public static Complex Zero = new Complex(0, 0);

  public static Complex add(Complex z1, Complex z2) {
    return new Complex(z1.re+z2.re, z1.im+z2.im);
  }

  public static Complex subtract(Complex z1, Complex z2) {
    return new Complex(z1.re-z2.re, z1.im-z2.im);
  }

  public static Complex multiply(Complex z1, Complex z2) {
    return new Complex(z1.re*z2.re - z1.im*z2.im, z1.re*z2.im + z1.im*z2.re);
  }

  public static Complex[] rootsOfUnity(int no) {
    Complex[] roots = new Complex[no];
    double step = 8 * Math.atan(1) / no;
    for (int i = 0; i < no; i++)
      roots[i] = new Complex(Math.cos(i*step), Math.sin(i*step));
    return roots;
  }
}
