package period;


import java.util.Random;

class RandomNumbers extends Random {
  double min;
  double max;
  String type;

  public RandomNumbers(String _type, int _size) {
    super();
    type = _type;
    min = 0;
    max = _size;
  }

  public RandomNumbers(String _type, double _min, double _max) {
    super();
    type = _type;
    min = _min;
    max = _max;
  }

  public int nextInt() {
    if      (type.equals("uniform")) return nextIntUniform();
    else if (type.equals("normal" )) return nextIntNormal ();
    else return 0;
  }

  private int nextIntUniform() {
    return (int)(min + nextInt((int)(max - min)));
  }

  private int nextIntNormal() {
    return (int)nextDoubleNormal();
  }

  public double nextDouble() {
    if      (type.equals("uniform")) return nextDoubleUniform();
    else if (type.equals("normal" )) return nextDoubleNormal ();
    else return 0;
  }  

  private double nextDoubleUniform() {
    return min + nextDouble() * (max - min);
  }

  private double nextDoubleNormal() {
    double value;
    do value = nextGaussian(); while (value < -3 || value > 3);
    
    return min + (value + 3) * (max - min) / 6;
  }
}
