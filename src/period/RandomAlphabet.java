package period;


import java.util.Random;

class RandomAlphabet extends Random {
  int size;
  String type;

  public RandomAlphabet(String _type, int _size) {
    super();
    type = _type;
    size = _size;
  }

  public char next() {
    if      (type.equals("uniform")) return nextUniform();
    else if (type.equals("normal" )) return nextNormal ();
    else return 0;
  }
  
  public char nextUniform() {
    return (char)('a' + nextInt(size));
  }

  public char nextNormal() {
    double value;
    do value = nextGaussian(); while (value < -3 || value > 3);
    return (char)('a' + (int)((value + 3) * size / 6));
  }
}
