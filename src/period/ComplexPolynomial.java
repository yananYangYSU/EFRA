package period;


import java.util.Vector;

class ComplexPolynomial extends Vector {
  public ComplexPolynomial() {
    super();
  }

  public ComplexPolynomial(int n) {
    super();
    for (int i = 0; i < n; i++) add(null);
  }

  public Complex getComplex(int index) {
    return (Complex)get(index);
  }

  public ComplexPolynomial reverse() {
    ComplexPolynomial reversed = new ComplexPolynomial();
    for (int i = 0; i < size(); i++) reversed.add(get(size()-1-i));

    return reversed;
  }

  public Vector getReals() {
    Vector reals = new Vector();
    for (int i = 0; i < size(); i++) reals.add(new Double(getComplex(i).real()));

    return reals;
  }
  
  private void evaluateFFT(int n, int k) {
    if (n ==1 || n == 2) {
      Complex c0 = getComplex(k);
      Complex c1 = getComplex(k+1);
      set(k, Complex.add(c0, c1));
      set(k+1, Complex.subtract(c0, c1));
    }
    else {
      for (int i = 0; i < n/2; i++) {
        int j = k + 2*i;
        temp.set(i, get(j));
        temp.set(i+n/2, get(j+1));
      }
      for (int i = 0; i < n; i++) set(k+i, temp.get(i));
      evaluateFFT(n/2, k);
      evaluateFFT(n/2, k+n/2);
      int j = size() / n;
      for (int i = 0; i < n/2; i++) {
        Complex c = Complex.multiply(roots[i*j], getComplex(k+n/2+i));
        temp.set(i, Complex.add(getComplex(k+i), c));
        temp.set(i+n/2, Complex.subtract(getComplex(k+i), c));
      }
      for (int i = 0; i < n; i++) set(k+i, temp.get(i));
    }
  }

  private static ComplexPolynomial temp;
  private static Complex[] roots;

  public void evaluateFFT() {
    temp = new ComplexPolynomial(size());
    roots = Complex.rootsOfUnity(size());
    evaluateFFT(size(), 0);
  }
  
  public static ComplexPolynomial convolute(ComplexPolynomial p, ComplexPolynomial q) {
    p.evaluateFFT();
    q.evaluateFFT();
    ComplexPolynomial r = new ComplexPolynomial();
    for (int i = 0; i < p.size(); i++)
      r.add(Complex.multiply(p.getComplex(i), q.getComplex(i)));
    r.evaluateFFT();
    for (int i = 1; i < r.size()/2; i++) {
      Complex swap = r.getComplex(i);
      r.set(i, r.get(r.size()-i));
      r.set(r.size()-i, swap);
    }
    for (int i = 0; i < r.size(); i++) r.set(i, r.getComplex(i).scale((double)1/r.size()));

    return r;
  }
}
