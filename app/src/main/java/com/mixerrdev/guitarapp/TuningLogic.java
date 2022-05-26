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

    public boolean classic = true;

    // Init arrays
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
            this.classic = false;

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

    // Return number of played string
    public int getString(double frequency, double magnitude) {
        int string;

        if (magnitude >= MIN_MAGNITUDE) {
            if (frequency >= firstStringFrequencyRange[0] && frequency <= firstStringFrequencyRange[1])
                string = 1;
            else if (frequency >= secondStringFrequencyRange[0] && frequency <= secondStringFrequencyRange[1])
                string = 2;
            else if (frequency >= thirdStringFrequencyRange[0] && frequency <= thirdStringFrequencyRange[1])
                string = 3;
            else if (frequency >= fourthStringFrequencyRange[0] && frequency <= fourthStringFrequencyRange[1])
                string = 4;
            else
                string = 0;
        } else
            string = -1;

        return string;
    }

    // Return status of played string
    // 1 - higher
    // 2 - lower
    // 3 - tuned
    // 0 - to loud
    // -1 - to quit
    public int getStatus(double frequency, double magnitude) {
        int string = getString(frequency, magnitude);
        int status;
        switch (string) {
            case (1):
                status = statusByFrequency(firstStringTunedFrequency, frequency);
                break;

            case (2):
                status = statusByFrequency(secondStringTunedFrequency, frequency);
                break;

            case (3):
                status = statusByFrequency(thirdStringTunedFrequency, frequency);
                break;

            case (4):
                status = statusByFrequency(fourthStringTunedFrequency, frequency);
                break;

            case (0):
                status = 0;
                break;

            default:
                status = -1;
                break;
        }

        return status;
    }

    public int statusByFrequency(double[] stringParams, double frequency){
        int status;
        if (frequency < stringParams[0])
            status = 1;
        else if (frequency > stringParams[1])
            status = 2;
        else
            status = 3;
        return status;
    }
}
