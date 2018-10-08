package period;

import java.util.*;

import org.omg.CORBA.SystemException;

import java.math.*;
import java.io.*;

public class TimeSeries {
  String series;
  Vector alphabet;
  Pattern periodicpattern;

  public TimeSeries(String _series, Vector _alphabet) {
    series = _series;
    alphabet = _alphabet;
  }

  public TimeSeries(String _series) {
    series = _series;
    if(1 == series.length()%2){
    	series = series.substring(0, series.length()-1);	
    }
    alphabet = new Vector();
    for (int i = 0; i < series.length(); i++) {
      Character c = new Character(series.charAt(i));
      if (!alphabet.contains(c)) alphabet.add(c);
    }
  }

  public TimeSeries(String _series, Pattern _periodicpattern) {
    series = _series;
    periodicpattern = _periodicpattern;

    alphabet = new Vector();
    for (int i = 0; i < series.length(); i++) {
      Character c = new Character(series.charAt(i));
      if (!alphabet.contains(c)) alphabet.add(c);
    }
  }

  public String toString() {
    return series;
  }

  public int length() {
    return series.length();
  }

  public int alphabetSize() {
    return alphabet.size();
  }

  public char symbolAt(int index) {
    return series.charAt(index);
  }

  public Pattern periodicPattern() {
    return periodicpattern;
  }

  public static TimeSeries generate(String type, int dsize, int asize) {
    RandomAlphabet randomalphabet = new RandomAlphabet(type, asize);
    
    String _series = new String();
    for (int i = 0; i < dsize; i++)
      _series += randomalphabet.next();

    return new TimeSeries(_series);
  }

  public static TimeSeries generatePeriodic(String type, int dsize, int asize, int period) {
    TimeSeries t = generate(type, period, asize);
    t.periodicpattern = new Pattern(t.series);

    for (int i = 0; i < Math.ceil(Math.log((double)dsize/period)/Math.log(2)); i++)
      t.series += t.series;

    t.series = t.series.substring(0, dsize);

    return t;
  }

  public static TimeSeries generatePartialPeriodic(String type, int dsize, int asize, int period) {
    TimeSeries t = generatePeriodic(type, dsize, asize, period);
    StringBuffer seriesbuffer = new StringBuffer(t.series);

    RandomNumbers noise = new RandomNumbers("uniform", period);
    RandomAlphabet randomalphabet = new RandomAlphabet(type, t.alphabetSize());

    int noofmissing = 0; do noofmissing = noise.nextInt(); while (noofmissing == 0);
    int[] missing = new int[noofmissing];
    for (int i = 0; i < noofmissing; i++) {
      missing[i] = noise.nextInt();
      t.periodicpattern = t.periodicpattern.removeAllAtIndex(missing[i]);
    }

    for (int j = 0; j < dsize/period; j++)
      for (int i = 0; i < noofmissing; i++)
        seriesbuffer.setCharAt(period*j + missing[i], randomalphabet.next());

    return new TimeSeries(seriesbuffer.toString(), t.periodicpattern);
  }

  public static TimeSeries generatePartialPeriodic(String type, int dsize, int asize, int period, double miss) {
    TimeSeries t = generatePeriodic(type, dsize, asize, period);
    StringBuffer seriesbuffer = new StringBuffer(t.series);

    RandomNumbers noise = new RandomNumbers("uniform", period);
    RandomAlphabet randomalphabet = new RandomAlphabet(type, t.alphabetSize());

    int noofmissing = (int)(miss*period);
    int[] missing = new int[noofmissing];
    for (int i = 0; i < noofmissing; i++) {
      missing[i] = noise.nextInt();
      t.periodicpattern = t.periodicpattern.removeAllAtIndex(missing[i]);
    }

    for (int j = 0; j < dsize/period; j++)
      for (int i = 0; i < noofmissing; i++)
        seriesbuffer.setCharAt(period*j + missing[i], randomalphabet.next());

    return new TimeSeries(seriesbuffer.toString(), t.periodicpattern);
  }

  public TimeSeries introduceNoise(String type, double rnoise, double inoise, double dnoise) {
    StringBuffer seriesbuffer = new StringBuffer(series);
    RandomNumbers noise = new RandomNumbers("uniform", length());
    RandomAlphabet randomalphabet = new RandomAlphabet(type, alphabetSize());

    for (int i = 0; i < (int)(rnoise*length()); i++)
      seriesbuffer.setCharAt(noise.nextInt(), randomalphabet.next());

    for (int i = 0; i < (int)(inoise*length()); i++)
      seriesbuffer.insert(noise.nextInt(), randomalphabet.next());

    for (int i = 0; i < (int)(dnoise*length()); i++) {
      int index;
      do { index = noise.nextInt(); } while (index >= seriesbuffer.length());
      seriesbuffer.deleteCharAt(index);
    }

    int diff = length() - seriesbuffer.length();
    if (diff > 0)
      for (int i = 0; i < diff; i++)
        seriesbuffer.append(randomalphabet.next());
    else seriesbuffer.setLength(length());

    return new TimeSeries(seriesbuffer.toString(), periodicpattern);
  }

  public TimeSeries introduceNoise(String type, double rnoise, double idnoise) {
    return introduceNoise(type, rnoise, idnoise/2, idnoise/2);
  }

  public TimeSeries introduceNoise(String type, double ridnoise) {
    return introduceNoise(type, ridnoise/3, ridnoise/3, ridnoise/3);
  }

  // Segment Periodicity

  public double[] mineSegmentPeriodicity(int repetitions) {
    Complex[] roots = Complex.rootsOfUnity(alphabetSize());

    double[] score = new double[length()];
    for (int i = 0; i < length(); i++) score[i] = 0;

    for (int k = 0; k < repetitions; k++) {
      // random uniform mapping
      RandomNumbers random = new RandomNumbers("uniform", alphabetSize());
      int[] mapping = new int[alphabetSize()];
      for (int i = 0; i < alphabetSize(); i++)
        mapping[i] = random.nextInt();
      
      ComplexPolynomial dp = new ComplexPolynomial(); // for the text
      ComplexPolynomial pp = new ComplexPolynomial(); // for the pattern
      for (int i = 0; i < length(); i++) {
        dp.add(roots[mapping[series.charAt(length()-1-i) % alphabetSize()]]); // filled reversly
        pp.add(roots[mapping[series.charAt(i) % alphabetSize()]].conjugate());
      }

      // convolute the polynomials, reverse the result, and project only the real values
      Vector scorek = ComplexPolynomial.convolute(dp, pp).reverse().getReals();
      for (int i = 0; i < length(); i++)
        score[i] += ((Double)scorek.get(i)).doubleValue();
    }

    // average the iterations
    for (int i = 0; i < length(); i++)
      score[i] = score[i] < 0 ? 0 : score[i] / repetitions;

    return score;
  }

  public Hashtable mineSegmentPeriodicity(double threshold, int repetitions) {
    double[] score = mineSegmentPeriodicity(repetitions);

    // output the periods
    Hashtable segmentperiodicities = new Hashtable();
    for (int period = 1; period <= length()/2; period++) {
      double freq = score[period] / length();
      if (freq >= threshold)
        segmentperiodicities.put(new Integer(period), new Double(freq));
    }

    // checks over the score vector
    // relative error = ||x - x`|| / ||x||
    // where x is the exact score vector and x` is the estimated score vector
    String seriesseries = series + series;
    double nom = 0; double den = 0;
    for (int i = 1; i <= length()/2; i++) {
      int matches = 0;
      for (int j = 0; j < length(); j++)
        matches += series.charAt(j) == seriesseries.charAt(j+i) ? 1 : 0;
      nom += Math.pow(matches - score[i], 2);
      den += Math.pow(matches, 2);
    }
    segmentperiodicities.put(new Integer(0), new Double(Math.sqrt(nom/den)));

    return segmentperiodicities;
  }

  public double mineSpecificSegmentPeriodicity(int period, int repetitions) {
    return mineSegmentPeriodicity(repetitions)[period] / length();
  }

  public long timeToMineSegmentPeriodicity(int repetitions) {
    long time = 0;

    Complex[] roots = Complex.rootsOfUnity(alphabetSize());

    for (int k = 0; k < repetitions; k++) {
      // random uniform mapping
      RandomNumbers random = new RandomNumbers("uniform", alphabetSize());
      int[] mapping = new int[alphabetSize()];
      for (int i = 0; i < alphabetSize(); i++)
        mapping[i] = random.nextInt();
      
      ComplexPolynomial dp = new ComplexPolynomial(); // for the text
      ComplexPolynomial pp = new ComplexPolynomial(); // for the pattern
      for (int i = 0; i < length(); i++) {
        dp.add(roots[mapping[series.charAt(length()-1-i) % alphabetSize()]]); // filled reversly
        pp.add(roots[mapping[series.charAt(i) % alphabetSize()]].conjugate());
      }

      Date start = new Date();

      // convolute the polynomials, reverse the result, and project only the real values
      ComplexPolynomial.convolute(dp, pp).reverse().getReals();

      time += new Date().getTime() - start.getTime();
    }

    return time;
  }

  // Symbol Periodicity

  class SymbolPeriodicity {
    int period;
    char symbol;
    int position;

    SymbolPeriodicity(int _period, char _symbol, int _position) {
      period = _period;
      symbol = _symbol;
      position = _position;
    }

    public int hashCode() {
      return period + (int)symbol + position;
    }

    public boolean equals(Object object) {
      SymbolPeriodicity sp = (SymbolPeriodicity)object;
      return period == sp.period && symbol == sp.symbol && position == sp.position;
    }

    public String toString() {
      return period + ": " + symbol + " @ " + position;
    }
  }

	public SymbolPeriodicity newSymbolPeriodicity(int period, char symbol, int position) {
    return new SymbolPeriodicity(period, symbol, position);
  }

  public int[][][] mineSymbolPeriodicity() {
    // code the letters to a vector of bits
    BitVector bits = new BitVector(alphabetSize()*length()*2);
    for (int i = 0; i < length(); i++)
      bits.set(alphabetSize()*(length()-1-i) + alphabet.indexOf(new Character(symbolAt(i))));

    // O(n log n) algorithm with FFT
    /*
    Vector v = BitVector.convolute(bits.reverse(), bits);
    for (int i = 1; i <= length()-1; i++)
      System.out.println(i + "\t" + v.get(i*alphabetSize()));
    */
    /*
    Vector v = BitVector.my_convolute(bits.reverse(), bits);
    for (int i = 1; i <= length()/2; i++) {
      BigDecimal bd = new BigDecimal(((Double)v.get(i*alphabetSize())).doubleValue());
      BigInteger b = bd.add(new BigDecimal(0.5)).toBigInteger();
      System.out.print(i + "\t" + bd + "\t" + b + "\t");
      for (int j = 0; j < b.bitLength(); j++)
        if (b.testBit(j)) System.out.print(j + " ");
      System.out.println();
    }
    */
    
    // naive O(n^2) algorithm
    int counts[][][] = new int[length()/2][alphabetSize()][length()/2]; // counting each letter at each position
    for (int i = 1; i <= length()/2; i++) {
      BitVector shifted = bits.shiftRight(alphabetSize()*i); // shift one letter at a time
      shifted.and(bits); // and operation captures the aligned 1's

      // k is for the letter and l is for the position
      for (int k = 0; k < alphabetSize(); k++) for (int l = 0; l < i; l++)
        counts[i-1][k][l] = 0;

      // count when aligned 1's occur
      for (int j = 0; j < shifted.length()-alphabetSize()*i; j++) if (shifted.get(j))
        counts[i-1][j%alphabetSize()][(length()-i-1-j/alphabetSize())%i]++;
    }

    return counts;
  }

  public Hashtable mineSymbolPeriodicity(double threshold) {
    int[][][] counts = mineSymbolPeriodicity();

    // output letter periodicities
    Hashtable symbolperiodicities = new Hashtable();
    for (int period = 1; period <= length()/2; period++)
      for (int symbolindex = 0; symbolindex < alphabetSize(); symbolindex++)
        for (int position = 0; position < period; position++) {
          double freq = counts[period-1][symbolindex][position] / (double)((length()-position+period-1)/period-1);
          if (freq >= threshold) {
            char symbol = ((Character)alphabet.get(symbolindex)).charValue();
            symbolperiodicities.put(new SymbolPeriodicity(period, symbol, position), new Double(freq));
          }
        }

    return symbolperiodicities;
  }

  public double mineSpecificSymbolPeriodicity(int period, char symbol, int position) {
    int symbolindex = alphabet.indexOf(new Character(symbol));
    return mineSymbolPeriodicity()[period-1][symbolindex][position] / (double)((length()-position+period-1)/period-1);
  }

  public double minePeriodicPatternSymbolPeriodicity() {
    int[][][] counts = mineSymbolPeriodicity();
    int period = periodicpattern.size();
    double freq = 0;
    for (int position = 0; position < period; position++) {
      int symbolindex = alphabet.indexOf(periodicpattern.firstItemAtIndex(position));
      freq += counts[period-1][symbolindex][position] / (double)((length()-position+period-1)/period-1);
    }
    return freq / period;
  }

  public long timeToMineSymbolPeriodicity() {
    // code the letters to a vector of bits
    BitVector bits = new BitVector(alphabetSize()*length());
    for (int i = 0; i < length(); i++)
      bits.set(alphabetSize()*(length()-1-i) + alphabet.indexOf(new Character(symbolAt(i))));

    Date start = new Date();

    // O(n log n) algorithm with FFT
    BitVector.convolute(bits.reverse(), bits);

    return new Date().getTime() - start.getTime();
  }

  // Relaxed Periods (IKM00 Implementation)

  class RelaxedPeriod implements Comparable {
    int period;
    double score;

    RelaxedPeriod(int _period, double _score) {
      period = _period;
      score = _score;
    }
    
    public int compareTo(Object _relaxedperiod) {
      return new Double(score).compareTo(new Double(((RelaxedPeriod)_relaxedperiod).score));
    }
  }
  
  public double[] mineRelaxedPeriods() {
    RandomNormalNormalized values = new RandomNormalNormalized();
    
    int n = length();
    int k = (int)Math.log(n); // sketch size
    double[] c = new double[n/2 + 1];
    
    for (int t = 1; t <= n/2; t++) { // possible period values
      double[][] v = new double[k][t]; // random normally distributed normalized vectors
      for (int i = 0; i < k; i++) for (int j = 0; j < t; j++) v[i][j] = values.next();
      double[][] sketch = new double[n/t][k]; // sketch vectors
      for (int l = 0; l < n/t; l++)
        for (int i = 0; i < k; i++) {
          double s = 0;
          for (int j = 0; j < t; j++) s += alphabet.indexOf(new Character(symbolAt(l*t+j))) * v[i][j];
          sketch[l][i] = s;
        }
      c[t] = 0;
      for (int l = 1; l < n/t; l++) {
        // compute the distance between sketch 0 and sketch l
        double d = 0;
        for (int i = 0; i < k; i++) d += Math.pow(sketch[0][i] - sketch[l][i], 2);
        c[t] += Math.sqrt(d);
      }
    }

    // normalize the values: (max - value) / (max - min)
    //double min = c[1], max = c[1];
    //for (int t = 2; t <= n/2; t++) {
    //if (c[t] < min) min = c[t];
    //if (c[t] > max) max = c[t];
    //}
    //for (int t = 1; t <= n/2; t++) c[t] = (max - c[t]) / (max - min);
    
    return c;
  }

  public Hashtable mineRelaxedPeriods(double threshold) {
    double[] score = mineRelaxedPeriods();
    
    // sorting the periods according to their score values
    RelaxedPeriod[] relaxedperiod = new RelaxedPeriod[length()/2];
    for (int period = 1; period <= length()/2; period++)
      relaxedperiod[period-1] = new RelaxedPeriod(period, score[period]);
    java.util.Arrays.sort(relaxedperiod);

    // normalizing the ranks of the periods
    Hashtable relaxedperiods = new Hashtable();
    for (int i = 0; i < length()/2; i++) {
      double normrank = (i != 0 && relaxedperiod[i].score == relaxedperiod[i-1].score) ?
        ((Double)relaxedperiods.get(new Integer(relaxedperiod[i-1].period))).doubleValue() :
        1 - (double)i / (length()/2 - 1);
      relaxedperiods.put(new Integer(relaxedperiod[i].period), new Double(normrank));
    }

    return relaxedperiods;
  }

  public double mineSpecificRelaxedPeriod(int period) {
    return ((Double)mineRelaxedPeriods(0).get(new Integer(period))).doubleValue();
  }

  // Partial Periodic Patterns

  private Vector segment(int length) {
    Vector segments = new Vector();
    for (int i = 0; i < length() / length; i++)
      segments.add(new Pattern(series.substring(length*i, length*(i+1))));
    return segments;
  }

  public MaxSubpatternTree buildMaxSubpatternTree(int period, double threshold, int window, double prethreshold) {
    // prethreshold is for the hysteresis approach

    // partition the time series into segments of length equals to period
    Vector segments = segment(period);

    // operate only on window size of the segments

    // count the one patterns
    CountTable onepatterns = new CountTable();
    for (int i = 0; i < window; i++)
      onepatterns.addAll(((Pattern)segments.get(i)).getOnePatterns());

    // construct the max pattern from the frequent one patterns
    int mincount = (int)(threshold * window);
    HashSet top = new HashSet(onepatterns.top(mincount).keySet());
    Pattern maxpattern = Pattern.unionAll(top);

    // construct the maxsubpattern tree such that the max pattern is the root
    MaxSubpatternTree maxsubpatterntree = new MaxSubpatternTree(maxpattern);
    for (int i = 0; i < window; i++) {
      Pattern hit = Pattern.intersect(maxpattern, (Pattern)segments.get(i));
      if (hit.lLength() > 1) maxsubpatterntree.insert(hit);
    }

    // consider the other windows
    int count = window;
    for (int k = 1; k <= segments.size()/window; k++) {
      // update the one patterns list
      int end = (k == segments.size()/window) ? segments.size() : (k+1)*window;
      for (int i = k*window; i < end; i++)
        onepatterns.addAll(((Pattern)segments.get(i)).getOnePatterns());

      // update the max pattern
      count += end - k*window;
      mincount = (int)(threshold * count);
      top.addAll(onepatterns.top(mincount).keySet());
      int premincount = (int)(prethreshold * count);
      top.retainAll(onepatterns.top(premincount).keySet());
      maxpattern = Pattern.unionAll(top);

      // update the maxsubpattern tree
      maxsubpatterntree = maxsubpatterntree.updateRoot(maxpattern);
      for (int i = k*window; i < end; i++) {
        Pattern hit = Pattern.intersect(maxpattern, (Pattern)segments.get(i));
        if (hit.lLength() > 1) maxsubpatterntree.insert(hit);
      }

    }

    return maxsubpatterntree;
  }

  public Hashtable minePartialPeriodicPatterns(int period, double threshold, int window, double prethreshold) {
    MaxSubpatternTree maxsubpatterntree = buildMaxSubpatternTree(period, threshold, window, prethreshold);

    // traverse the maxsubpattern tree to calculate the patterns and their exact counts
    CountTable patterns = maxsubpatterntree.traverse();

    // return  only the frequent patterns
    int count = length() / period;
    int mincount = (int)(threshold * count);

    return patterns.top(mincount).scale((double)1/count);
  }

  public Hashtable mineSpecificPartialPeriodicPatterns(Collection patterns, int period, double threshold, int window, double prethreshold) {
    MaxSubpatternTree maxsubpatterntree = buildMaxSubpatternTree(period, threshold, window, prethreshold);

    // traverse the maxsubpattern tree to calculate the patterns and their exact counts
    CountTable patternscount = maxsubpatterntree.traverse();

    boolean treeisupdated = false;
    // insert the pattern that is not in the tree
    for (Iterator i = patterns.iterator(); i.hasNext();) {
      Pattern pattern = (Pattern)i.next();
      if (!patternscount.containsKey(pattern) && pattern.isSubpattern(maxsubpatterntree.rootNodePattern())) {
        maxsubpatterntree.insert(pattern, 0);
        treeisupdated = true;
      }
    }

    if (treeisupdated)
      // traverse the maxsubpattern tree to calculate the patterns and their exact counts
      patternscount = maxsubpatterntree.traverse();

    // retain only the input patterns
    Hashtable partialperiodicpatterns = new Hashtable();
    for (Iterator i = patterns.iterator(); i.hasNext();) {
      Pattern pattern = (Pattern)i.next();
      double score = patternscount.containsKey(pattern) ? (double)patternscount.getCount(pattern) / (length() / period) : 0;
      partialperiodicpatterns.put(pattern, new Double(score));
    }

    return partialperiodicpatterns;
  }
  
  /*
  public double mineSpecificPartialPeriodicPattern(Pattern pattern, double threshold) {
    int period = pattern.size();
    return mineSpecificPartialPeriodicPatternOnline(pattern, threshold, length()/period, threshold);
  }

  public double mineSpecificPartialPeriodicPatternOnline(Pattern pattern, double threshold, int window, double prethreshold) {
    int period = pattern.size();

    MaxSubpatternTree maxsubpatterntree = buildMaxSubpatternTree(period, prethreshold, window);

    if (!pattern.isSubpattern(maxsubpatterntree.rootNodePattern())) return 0;
    
    maxsubpatterntree.insert(pattern, 0);
    return maxsubpatterntree.traverse().getCount(pattern) / (length() / period);
  }

  public Hashtable mineSpecificPartialPeriodicPatterns(Collection patterns, double threshold) {
    if (patterns.isEmpty()) return null;

    int period = ((Pattern)patterns.iterator().next()).size();
    return mineSpecificPartialPeriodicPatternsOnline(patterns, threshold, length()/period, threshold);
  }

  public Hashtable mineSpecificPartialPeriodicPatternsOnline(Collection patterns, double threshold, int window, double prethreshold) {
    if (patterns.isEmpty()) return null;

    int period = ((Pattern)patterns.iterator().next()).size();

    MaxSubpatternTree maxsubpatterntree = buildMaxSubpatternTree(period, prethreshold, window);

    // traverse the maxsubpattern tree to calculate the patterns and their exact counts
    CountTable patternscount = maxsubpatterntree.traverse();

    boolean treeisupdated = false;
    // insert the pattern that is not in the tree
    for (Iterator i = patterns.iterator(); i.hasNext();) {
      Pattern pattern = (Pattern)i.next();
      if (!patternscount.containsKey(pattern) && pattern.isSubpattern(maxsubpatterntree.rootNodePattern())) {
        maxsubpatterntree.insert(pattern, 0);
        treeisupdated = true;
      }
    }

    if (treeisupdated)
      // traverse the maxsubpattern tree to calculate the patterns and their exact counts
      patternscount = maxsubpatterntree.traverse();

    // retain only the input patterns
    Hashtable partialperiodicpatterns = new Hashtable();
    for (Iterator i = patterns.iterator(); i.hasNext();) {
      Pattern pattern = (Pattern)i.next();
      double score = patternscount.containsKey(pattern) ? (double)patternscount.getCount(pattern) / (length() / period) : 0;
      partialperiodicpatterns.put(pattern, new Double(score));
    }

    return partialperiodicpatterns;
  }
  */

  // Periodicity Mining from Streams
  public void minePeriodicityOnline(int window, double threshold) {
    // the time series will be treated as a stream

    // wait for a window size of elements, the max period that will be discovered is window/2
    // i.e., work on the first window of elements then slide the window with window/2 elements

    int counts[][][] = new int[window/2][alphabetSize()][window/2]; // counting each letter at each position
    MaxSubpatternTree maxsubpatterntrees[] = new MaxSubpatternTree[window/2]; // a tree for every possible period
    for (int m = 0; m <= length()-window; m+=window/2) {
      // the following code is from mineSymbolPeriodicity with length() = window
      BitVector bits = new BitVector(alphabetSize()*window);
      for (int i = 0; i < window; i++)
        bits.set(alphabetSize()*(window-1-i) + alphabet.indexOf(new Character(symbolAt(i+m))));

      for (int i = 1; i <= window/2; i++) {
        BitVector shifted = bits.shiftRight(alphabetSize()*i); // shift one letter at a time
        shifted.and(bits); // and operation captures the aligned 1's

        // count when aligned 1's occur
        for (int j = 0; j < shifted.length()-alphabetSize()*(m == 0 ? i : window/2); j++) if (shifted.get(j))
          counts[i-1][j%alphabetSize()][(window+m-i-1-j/alphabetSize())%i]++;
        /*
        // at every stage, form the maxpattern from the frequent letters at this period
        int period = i;
        Pattern maxpattern = new Pattern(period);
        for (int symbolindex = 0; symbolindex < alphabetSize(); symbolindex++) for (int position = 0; position < period; position++) {
          double freq = counts[period-1][symbolindex][position] / (double)((window+m-position+period-1)/period-1);
          if (freq >= threshold)
            maxpattern = maxpattern.addItemAtIndex((Character)alphabet.get(symbolindex), position);
        }

        // check if a tree exists for this period
        if (maxsubpatterntrees[period-1] == null) {
          if (maxpattern.lLength() > 1)
            maxsubpatterntrees[period-1] = new MaxSubpatternTree(maxpattern, window);
        }
        else {
          if (maxpattern.lLength() > 1)
            maxsubpatterntrees[period-1] = maxsubpatterntrees[period-1].updateRoot(maxpattern);
          else maxsubpatterntrees[period-1] = null;
        }
        if (maxsubpatterntrees[period-1] != null) // accumulate the buffer
          maxsubpatterntrees[period-1].accumulateBuffer(m==0 ? series.substring(0, window) : series.substring(window+m-window/2, window+m));
          */
      }
    }

    for (int period = 1; period <= window/2; period++)
      for (int symbolindex = 0; symbolindex < alphabetSize(); symbolindex++)
        for (int position = 0; position < period; position++)
          if (counts[period-1][symbolindex][position] != 0)
            System.out.println(period + "\t" + symbolindex + "\t" + position + "\t" + counts[period-1][symbolindex][position]);
  }

  // Periodicity Mining from Streams
  public long minePeriodicityOnline(int window[], double threshold) {
    long time = 0;

    OrderedDuplicateMap windowslist = new OrderedDuplicateMap();
    for (int i = 0; i < window.length; i++) windowslist.put(new Integer(window[i]), new Integer(-i-1));

    int windowmax = window[window.length-1];
    int counts[][][] = new int[windowmax/2][alphabetSize()][windowmax/2]; // counting each letter at each position
    MaxSubpatternTree maxsubpatterntrees[] = new MaxSubpatternTree[windowmax/2]; // a tree for every possible period
    int m[] = new int[window.length];

    while (!windowslist.isEmpty()) {
      int pos = ((Integer)windowslist.firstKey()).intValue();
      int index = ((Integer)windowslist.remove(windowslist.firstKey())).intValue();
      if (index < 0) {
        // first encounter
        index = -index;

        m[index-1] = 0;
      }
      else {
        // after sliding
        m[index-1] += window[index-1]/2;
      }

      BitVector bits = new BitVector(alphabetSize()*window[index-1]);
      for (int i = 0; i < window[index-1]; i++)
        bits.set(alphabetSize()*(window[index-1]-1-i) + alphabet.indexOf(new Character(symbolAt(i+m[index-1]))));

      Date start = new Date();

      for (int i = index == 1 ? 1 : window[index-2]/2 + 1; i <= window[index-1]/2; i++) {
        BitVector shifted = bits.shiftRight(alphabetSize()*i); // shift one letter at a time
        shifted.and(bits); // and operation captures the aligned 1's

        // count when aligned 1's occur
        for (int j = 0; j < shifted.length()-alphabetSize()*(m[index-1] == 0 ? i : window[index-1]/2); j++) if (shifted.get(j))
          counts[i-1][j%alphabetSize()][(window[index-1]+m[index-1]-i-1-j/alphabetSize())%i]++;

        // at every stage, form the maxpattern from the frequent letters at this period
        int period = i;
        Pattern maxpattern = new Pattern(period);
        for (int symbolindex = 0; symbolindex < alphabetSize(); symbolindex++) for (int position = 0; position < period; position++) {
          double freq = counts[period-1][symbolindex][position] / (double)((window[index-1]+m[index-1]-position+period-1)/period-1);
          if (freq >= threshold)
            maxpattern = maxpattern.addItemAtIndex((Character)alphabet.get(symbolindex), position);
        }

        // check if a tree exists for this period
        if (maxsubpatterntrees[period-1] == null) {
          if (maxpattern.lLength() > 1)
            maxsubpatterntrees[period-1] = new MaxSubpatternTree(maxpattern, window[index-1]);
        }
        else {
          if (maxpattern.lLength() > 1)
            maxsubpatterntrees[period-1] = maxsubpatterntrees[period-1].updateRoot(maxpattern);
          else maxsubpatterntrees[period-1] = null;
        }
        if (maxsubpatterntrees[period-1] != null) // accumulate the buffer
          maxsubpatterntrees[period-1].accumulateBuffer(m[index-1] == 0 ?
                                                        series.substring(0, window[index-1]) :
                                                        series.substring(window[index-1]+m[index-1]-window[index-1]/2, window[index-1]+m[index-1]));
      }

      time += new Date().getTime() - start.getTime();

      //System.out.println(pos + "\t" + time);

      pos += window[index-1]/2;
      if (pos <= length()) windowslist.put(new Integer(pos), new Integer(index));
    }

    return time;
    /*
    Hashtable symbolperiodicities = new Hashtable();
		for (int period = 1; period <= windowmax/2; period++)
      for (int symbolindex = 0; symbolindex < alphabetSize(); symbolindex++)
        for (int position = 0; position < period; position++)
          if (counts[period-1][symbolindex][position] != 0) {
						double freq = counts[period-1][symbolindex][position] / (double)((length()-position+period-1)/period-1);
						if (freq >= threshold) {
							char symbol = ((Character)alphabet.get(symbolindex)).charValue();
							symbolperiodicities.put(new SymbolPeriodicity(period, symbol, position), new Double(freq));
						}
					}

    return symbolperiodicities;
    */
  }

  // Periodicity Mining using Wavelets (AWSOM)
  public long awsom() {
    long time = 0;
    Date start = new Date();

    int numlevels = (int)(Math.log(length())/Math.log(2)) + 1; // treat the timeseries itself as level 0
    double[][] wavelets = new double[numlevels][];
    double[][] averages = new double[numlevels][];
    // wavelets[0] is not defined
    averages[0] = new double[length()];
    for (int i = 1; i < numlevels; i++) {
      int length = averages[i-1].length/2;
      averages[i] = new double[length];
      wavelets[i] = new double[length];
    }

    // initialize the averages 0th level
    // Note 1: The symbols are mapped to integers corresponding to their positions in the alphabet vector
    for (int i = 0; i < length(); i++) averages[0][i] = alphabet.indexOf(new Character(symbolAt(i)));

    // compute all levels' averages and wavelets
    for (int j = 1; j < numlevels; j++)
      for (int i = 0; i < averages[j].length; i++) {
        averages[j][i] = (averages[j-1][2*i+1] + averages[j-1][2*i]) / Math.sqrt(2);
        wavelets[j][i] = (averages[j-1][2*i+1] - averages[j-1][2*i]) / Math.sqrt(2);
      }

    time += new Date().getTime() - start.getTime();

    for (int j = 1; j < numlevels; j++) {
      String data = "";
      for (int i = 0; i < wavelets[j].length; i++) {
        data += wavelets[j][i] + "\t";
      }

      try {
        start = new Date();
        BufferedWriter tempfile = new BufferedWriter(new FileWriter("hala.data"));
        tempfile.write(data, 0, data.length());
        tempfile.close();
        long filetime = new Date().getTime() - start.getTime();

        for (int i = 1; i < 10; i++) {
          start = new Date();
          Process p = Runtime.getRuntime().exec("hala " + i + " l hala.data");
          p.waitFor();
          time += new Date().getTime() - start.getTime() - filetime;
        }
      } catch (Exception e) {}
    }

    return time;
  }

  public double awsom(int period) {
    int numlevels = (int)(Math.log(length())/Math.log(2)) + 1; // treat the timeseries itself as level 0
    double[][] wavelets = new double[numlevels][];
    double[][] averages = new double[numlevels][];
    // wavelets[0] is not defined
    averages[0] = new double[length()];
    for (int i = 1; i < numlevels; i++) {
      int length = averages[i-1].length/2;
      averages[i] = new double[length];
      wavelets[i] = new double[length];
    }

    // initialize the averages 0th level
    // Note 1: The symbols are mapped to integers corresponding to their positions in the alphabet vector
    for (int i = 0; i < length(); i++) averages[0][i] = alphabet.indexOf(new Character(symbolAt(i)));

    // compute all levels' averages and wavelets
    for (int j = 1; j < numlevels; j++)
      for (int i = 0; i < averages[j].length; i++) {
        averages[j][i] = (averages[j-1][2*i+1] + averages[j-1][2*i]) / Math.sqrt(2);
        wavelets[j][i] = (averages[j-1][2*i+1] - averages[j-1][2*i]) / Math.sqrt(2);
      }

    int l = (int)(Math.log(period)/Math.log(2));
    String data = "";
    for (int i = 0; i < wavelets[l].length; i++) {
      data += wavelets[l][i] + "\t";
    }

    double max = 0;

    try {
      BufferedWriter tempfile = new BufferedWriter(new FileWriter("hala.data"));
      tempfile.write(data, 0, data.length());
      tempfile.close();

      for (int i = 1; i < 10; i++) {
        Process p = Runtime.getRuntime().exec("hala " + i + " l hala.data");
        p.waitFor();
        BufferedReader hala = new BufferedReader(new InputStreamReader(p.getInputStream()));
        for (int k = 0; k < i; k++) {
          double value = Double.parseDouble(hala.readLine());
          if (value <= 1 && value > max) max = value;
        }
      }
    } catch (Exception e) {}

    return max;
  }

  // Periodicity Mining using DTW
  public Hashtable dtw() {
    int[][] cost = new int[length()][length()];
    for (int i = 0; i < length(); i++) {
      //for (int j = 0; j < i; j++) System.out.print("\t");
      for (int j = i; j < length(); j++) {
        int dist = symbolAt(i) == symbolAt(j) ? 0 : 1;
        int sofar = (i == 0 || j == 0) ? 0 : Math.min(cost[i-1][j-1], /*Math.min(*/cost[i-1][j]);//, cost[i][j-1]));
        cost[i][j] = dist + sofar;
        //System.out.print(cost[i][j] + "\t");
      }
      //System.out.println();
    }

    Hashtable periods = new Hashtable();

    for (int p = 1; p <= length()/2; p++) {
      int err = cost[length()-1-p][length()-1];
      double conf = (double)(length()-p-err) / (length()-p);
      //if (err <= cost[length()-1-(p-1)][length()-1] && err <= cost[length()-1-(p+1)][length()-1]) {
        //double conf = (double)(length()-p-err) / (length()-p);
        //System.out.println(p + "\t" + conf);// + "\t" + mineSpecificSegmentPeriodicity(p, 1));
        periods.put(new Integer(p), new Double(conf));
        //}
      //else periods.put(new Integer(p), new Double(conf));
    }

    return periods;
  }

  public long dtw_time() {
    Date start = new Date();
    
    int[][] cost = new int[length()][length()];
    for (int i = 0; i < length(); i++) {
      //for (int j = 0; j < i; j++) System.out.print("\t");
      for (int j = i; j < length(); j++) {
        int dist = symbolAt(i) == symbolAt(j) ? 0 : 1;
        int sofar = (i == 0 || j == 0) ? 0 : Math.min(cost[i-1][j-1], /*Math.min(*/cost[i-1][j]);//, cost[i][j-1]));
        cost[i][j] = dist + sofar;
        //System.out.print(cost[i][j] + "\t");
      }
      //System.out.println();
    }

    //Hashtable periods = new Hashtable();

    for (int p = 1; p <= length()/2; p++) {
      int err = cost[length()-1-p][length()-1];
      double conf = (double)(length()-p-err) / (length()-p);
      //if (err <= cost[length()-1-(p-1)][length()-1] && err <= cost[length()-1-(p+1)][length()-1]) {
        //double conf = (double)(length()-p-err) / (length()-p);
        //System.out.println(p + "\t" + conf);// + "\t" + mineSpecificSegmentPeriodicity(p, 1));
      //periods.put(new Integer(p), new Double(conf));
        //}
      //else periods.put(new Integer(p), new Double(conf));
    }

    return (new Date().getTime() - start.getTime());
  }

  public Hashtable dtw_stream(int window, int slide) {
    int[][] dist = new int[length()][length()];
    for (int m = 0; m < length(); m+=slide) {
      if (m != 0) {
        for (int i = m; i < m+window-slide; i++)
          for (int j = m+window-slide; j < Math.min(m+window, length()); j++)
            dist[i][j] = symbolAt(i) == symbolAt(j) ? 0 : 1;

        for (int i = 0; i < m; i++)
          for (int j = m+window-slide; j < Math.min(m+window, length()); j++) {
            int k = m;
            while (k < m+window-slide && dist[i][k] == 1 && dist[k][j] == 1) k++;
            dist[i][j] = (k < m+window-slide && dist[i][k] == 0 && dist[k][j] == 0) ? 0 : 1;
          }
      }
      for (int i = (m == 0 ? 0 : m+window-slide); i < Math.min(m+window, length()); i++) {
        for (int j = i; j < Math.min(m+window, length()); j++)
          dist[i][j] = symbolAt(i) == symbolAt(j) ? 0 : 1;
      }
    }
    
    int[][] cost = new int[length()][length()];
    for (int i = 0; i < length(); i++) {
      //for (int j = 0; j < i; j++) System.out.print("\t");
      for (int j = i; j < length(); j++) {
        int sofar = (i == 0 || j == 0) ? 0 : Math.min(cost[i-1][j-1], /*Math.min(*/cost[i-1][j]);//, cost[i][j-1]));
        cost[i][j] = dist[i][j] + sofar;
        //System.out.print(dist[i][j] + "\t");
      }
      //System.out.println();
    }
    

    Hashtable periods = new Hashtable();

    for (int p = 1; p <= length()/2; p++) {
      int err = cost[length()-1-p][length()-1];
      double conf = (double)(length()-p-err) / (length()-p);
      //if (err <= cost[length()-1-(p-1)][length()-1] && err <= cost[length()-1-(p+1)][length()-1]) {
        //double conf = (double)(length()-p-err) / (length()-p);
        //System.out.println(p + "\t" + conf);// + "\t" + mineSpecificSegmentPeriodicity(p, 1));
        periods.put(new Integer(p), new Double(conf));
        //}
    }

    return periods;
  }

  public long dtw_stream_time(int window, int slide) {
    Date start = new Date();
    
    int[][] dist = new int[length()][length()];
    for (int m = 0; m < length(); m+=slide) {
      if (m != 0) {
        for (int i = m; i < m+window-slide; i++)
          for (int j = m+window-slide; j < Math.min(m+window, length()); j++)
            dist[i][j] = symbolAt(i) == symbolAt(j) ? 0 : 1;

        for (int i = 0; i < m; i++)
          for (int j = m+window-slide; j < Math.min(m+window, length()); j++) {
            int k = m;
            while (k < m+window-slide && dist[i][k] == 1 && dist[k][j] == 1) k++;
            dist[i][j] = (k < m+window-slide && dist[i][k] == 0 && dist[k][j] == 0) ? 0 : 1;
          }
      }
      for (int i = (m == 0 ? 0 : m+window-slide); i < Math.min(m+window, length()); i++) {
        for (int j = i; j < Math.min(m+window, length()); j++)
          dist[i][j] = symbolAt(i) == symbolAt(j) ? 0 : 1;
      }
    }
    
    int[][] cost = new int[length()][length()];
    for (int i = 0; i < length(); i++) {
      //for (int j = 0; j < i; j++) System.out.print("\t");
      for (int j = i; j < length(); j++) {
        int sofar = (i == 0 || j == 0) ? 0 : Math.min(cost[i-1][j-1], /*Math.min(*/cost[i-1][j]);//, cost[i][j-1]));
        cost[i][j] = dist[i][j] + sofar;
        //System.out.print(dist[i][j] + "\t");
      }
      //System.out.println();
    }
    

    //Hashtable periods = new Hashtable();

    for (int p = 1; p <= length()/2; p++) {
      int err = cost[length()-1-p][length()-1];
      double conf = (double)(length()-p-err) / (length()-p);
      //if (err <= cost[length()-1-(p-1)][length()-1] && err <= cost[length()-1-(p+1)][length()-1]) {
        //double conf = (double)(length()-p-err) / (length()-p);
        //System.out.println(p + "\t" + conf);// + "\t" + mineSpecificSegmentPeriodicity(p, 1));
      //periods.put(new Integer(p), new Double(conf));
        //}
    }

    return (new Date().getTime() - start.getTime());
  }

  public void editDistance() {
    int dist[][] = new int[length()+1][length()+1];
    for (int i = 0; i < length()+1; i++) dist[i][0] = i;
    for (int j = 0; j < length()+1; j++) dist[0][j] = j;
    
    for (int i = 1; i < length()+1; i++) {
      for (int j = 1; j < length()+1; j++) {
        int d = Math.min(dist[i-1][j]+1, dist[i][j-1]+1);
        if (symbolAt(i-1) == symbolAt(j-1)) d = Math.min(d, dist[i-1][j-1]);
        dist[i][j] = d;
        System.out.print(dist[i][j] + "\t");
      }
      System.out.println();
    }
  }
}

class RandomNormalNormalized extends Random {
  public double next() {
    double value;
    do value = nextGaussian(); while (value < -1 || value > 1);
    return value;
  }
}
