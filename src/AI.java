/**
 * Created by `ktulhy` on 4/22/15.
 */
public class AI {
    public double count = 1000;

    private double res = 0;

    private int firstLayer = 7;
    private int secondLayer = 6;
    private int thirdLayer = 2;

    private double mass1[][] = new double[firstLayer][secondLayer];
    private double mass2[][] = new double[secondLayer][thirdLayer];

    private double mutationFactor = 0.05;

    public AI() {
        for (int i = 0; i < firstLayer; i++) {
            for (int j = 0; j < secondLayer; j++) {
                mass1[i][j] = Math.random() * 0.1 - 0.05;
            }
        }

        for (int i = 0; i < secondLayer; i++) {
            for (int j = 0; j < thirdLayer; j++) {
                mass2[i][j] = Math.random() * 0.1 - 0.05;
            }
        }
    }

    public AI(AI parent) {
        for (int i = 0; i < firstLayer; i++) {
            for (int j = 0; j < secondLayer; j++) {
                mass1[i][j] = parent.mass1[i][j] + (Math.random() - 1/2.) * mutationFactor;
            }
        }

        for (int i = 0; i < secondLayer; i++) {
            for (int j = 0; j < thirdLayer; j++) {
                mass2[i][j] = parent.mass2[i][j] + (Math.random() - 1/2.) * mutationFactor;
            }
        }
    }

    private double f(double inp) {
        return Math.exp(3. * inp)/ (2. * (1. + (Math.exp(3. * inp) - 1) / 2.));
    }

    public double getAPhi(double vect[]) {
        int i;
        int j;

        double secondLayerInp[] = new double[secondLayer];
        double secondLayerOut[] = new double[secondLayer];

        double thirdLayerOut[] = new double[thirdLayer];

        vect[firstLayer - 1] = res;
        for (i = 0; i < firstLayer; i++) {
            for (j = 0; j < secondLayer; j++) {
                secondLayerInp[j] = vect[i] * mass1[i][j];
            }
        }

        for (i = 0; i < secondLayer; i++) {
            secondLayerOut[i] = f(secondLayerInp[i] / firstLayer);
        }

        for (i = 0; i < secondLayer; i++) {
            for (j = 0; j < thirdLayer; j++) {
                thirdLayerOut[j] = secondLayerOut[i] * mass2[i][j];
            }
        }

        res = thirdLayerOut[1] / secondLayer;
        return thirdLayerOut[0] / secondLayer;
    }

}
