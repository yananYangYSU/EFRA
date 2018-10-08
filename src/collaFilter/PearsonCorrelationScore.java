package collaFilter;

public class PearsonCorrelationScore {
	private double[] xData;
    private double[] yData;
    private double xMeans;
    private double yMeans;

    private double numerator;

    private double denominator;

    private double pearsonCorrelationScore;

    public double getPearsonCorrelationScore(double[] x, double[] y) {
        this.xData = x;
        this.yData = y;
        this.xMeans = this.getMeans(xData);
        this.yMeans = this.getMeans(yData);
        this.numerator = this.generateNumerator();
        this.denominator = this.generateDenomiator();
        this.pearsonCorrelationScore = this.numerator / this.denominator;
        return this.pearsonCorrelationScore;
    }

    private double generateNumerator() {
        double sum = 0.0;
        for (int i = 0; i < xData.length; i++) { 
            sum += (xData[i] - xMeans) * (yData[i] - yMeans);
        }
        return sum;
    }
    private double generateDenomiator() {
        double xSum = 0.0;
        for (int i = 0; i < xData.length; i++) {
            xSum += (xData[i] - xMeans) * (xData[i] - xMeans);
        }

        double ySum = 0.0;
        for (int i = 0; i < yData.length; i++) {
            ySum += (yData[i] - yMeans) * (yData[i] - yMeans);
        }

        return Math.sqrt(xSum) * Math.sqrt(ySum);
    }

    private double getMeans(double[] datas) {
        double sum = 0.0;
        for (int i = 0; i < datas.length; i++) {
            sum += datas[i];
        }
        return sum / datas.length;
    }

}
