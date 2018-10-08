package period;
import java.util.*;
import java.math.*;

class BitVector extends BitSet {
  int length;

  public BitVector(int nbits) {
    super(nbits);
    length = nbits;
  }

  public int length() {
    return length;
  }

  public void set(int index, boolean bit) {
    if (bit) set(index); else clear(index);
  }

  public String toString() {
    String s = "";
    for (int i = length-1; i >= 0; i--)
      s += get(i) ? "1" : "0";
    return s;
  }

  public BitVector shiftRight(int nbits) {
    BitVector shifted = new BitVector(length);
    for (int i = nbits; i < super.length(); i++)
      if (get(i)) shifted.set(i-nbits);

    return shifted;
  }

  public BitVector reverse() {
    BitVector reversed = new BitVector(length);
    for (int i = 0; i < length; i++) if (get(i)) reversed.set(length-1-i);

    return reversed;
  }
  
  public static Vector convolute(BitVector u, BitVector v) {
    // convert the bit vectors to complex polynomials
    ComplexPolynomial up = new ComplexPolynomial();
    ComplexPolynomial vp = new ComplexPolynomial();
    for (int i = u.length()-1; i >= 0; i--) {
      up.add(new Complex(u.get(i) ? 1 : 0, 0));
      vp.add(new Complex(v.get(i) ? 1 : 0, 0));
    }

    // convolute the complex polynomials, reverse the result, and return only the real values
    return ComplexPolynomial.convolute(up, vp).reverse().getReals();
  }

  public static Vector my_convolute(BitVector u, BitVector v) {
    // convert the bit vectors to complex polynomials
    ComplexPolynomial up = new ComplexPolynomial();
    ComplexPolynomial vp = new ComplexPolynomial();
    for (int i = u.length()-1; i >= 0; i--) {
      up.add(new Complex(u.get(i) ? Math.pow(2, u.length()-1-i) : 0, 0));
      vp.add(new Complex(v.get(i) ? 1 : 0, 0));
    }

    // convolute the complex polynomials, reverse the result, and return only the real values
    return ComplexPolynomial.convolute(up, vp).reverse().getReals();
  }
}
