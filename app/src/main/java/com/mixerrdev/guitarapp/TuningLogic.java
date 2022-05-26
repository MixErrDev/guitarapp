package com.mixerrdev.guitarapp;

public class TuningLogic {
    public double[] firstStringFrequencyRange;
    public double[] secondStringFrequencyRange;
    public double[] thirdStringFrequencyRange;
    public double[] fourthStringFrequencyRange;

    public double[] firstStringTunedFrequency;
    public double[] secondStringTunedFrequency;
    public double[] thirdStringTunedFrequency;
    public double[] fourthStringTunedFrequency;

    final double MIN_MAGNITUDE = 80000.0;

    public TuningLogic(boolean classic) {
        if (classic) {
            firstStringFrequencyRange = new double[]{415.0, 465.0};
            secondStringFrequencyRange = new double[]{300.0, 349.0};
            thirdStringFrequencyRange = new double[]{220.0, 299.0};
            fourthStringFrequencyRange = new double[]{350.0, 414.0};

            firstStringTunedFrequency = new double[]{439.5, 440.5};
            secondStringTunedFrequency = new double[]{329.0, 330.0};
            thirdStringTunedFrequency = new double[]{261.0, 262.1};
            fourthStringTunedFrequency = new double[]{391.5, 392.5};
        } else {
            firstStringFrequencyRange = new double[]{415.0, 465.0};
            secondStringFrequencyRange = new double[]{300.0, 349.0};
            thirdStringFrequencyRange = new double[]{220.0, 299.0};
            fourthStringFrequencyRange = new double[]{350.0, 414.0};

            firstStringTunedFrequency = new double[]{493.5, 494.5};
            secondStringTunedFrequency = new double[]{369.5, 370.5};
            thirdStringTunedFrequency = new double[]{293.0, 294.2};
            fourthStringTunedFrequency = new double[]{439.5, 440.5};
        }
    }

    public int getString(double frequency) {
        int string;

        if (frequency >= firstStringFrequencyRange[0] && frequency <= firstStringFrequencyRange[1])
            string = 1;
        else if (frequency >= secondStringFrequencyRange[0] && frequency <= secondStringFrequencyRange[1])
            string = 1;
        else if (frequency >= thirdStringFrequencyRange[0] && frequency <= thirdStringFrequencyRange[1])
            string = 1;
        else if (frequency >= fourthStringFrequencyRange[0] && frequency <= fourthStringFrequencyRange[1])
            string = 1;
        else
            string = 0;

        return string;
    }

    public int getStatus(double frequency) {
        int string = getString(frequency);
        int status;
        switch (string) {
            case (1):
                if (frequency < firstStringTunedFrequency[0])
                    status = 1;
                else if (frequency > firstStringTunedFrequency[1])
                    status = 2;
                else
                    status = 3;
                break;

            case (2):
                if (frequency < secondStringTunedFrequency[0])
                    status = 1;
                else if (frequency > secondStringTunedFrequency[1])
                    status = 2;
                else
                    status = 3;
                break;

            case (3):
                if (frequency < thirdStringTunedFrequency[0])
                    status = 1;
                else if (frequency > thirdStringTunedFrequency[1])
                    status = 2;
                else
                    status = 3;
                break;

            case (4):
                if (frequency < fourthStringTunedFrequency[0])
                    status = 1;
                else if (frequency > fourthStringTunedFrequency[1])
                    status = 2;
                else
                    status = 3;
                break;

            default:
                status = 0;
                break;
        }

        return status;
    }
}
