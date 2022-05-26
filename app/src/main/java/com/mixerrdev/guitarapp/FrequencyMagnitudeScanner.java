package com.mixerrdev.guitarapp;

import org.jtransforms.fft.DoubleFFT_1D;

public class FrequencyMagnitudeScanner {
    private double[] window;

    public FrequencyMagnitudeScanner() {
        window = null;
    }

    public double[] extractFrequencyMagnitude(short[] sampleData, int sampleRate) {
        // SampleData + zero padding
        DoubleFFT_1D fft = new DoubleFFT_1D(sampleData.length + 24 * sampleData.length);
        double[] a = new double[(sampleData.length + 24 * sampleData.length) * 2];

        System.arraycopy(applyWindow(sampleData), 0, a, 0, sampleData.length);
        fft.realForward(a);

        // Find the peak magnitude and it's index
        double maxMagnitude = Double.NEGATIVE_INFINITY;
        int maxInd = -1;

        for(int i = 0; i < a.length / 2; ++i) {
            double real  = a[2*i];
            double imaginary  = a[2*i+1];
            double magnitude = Math.sqrt(real * real + imaginary * imaginary);

            if(magnitude > maxMagnitude) {
                maxMagnitude = magnitude;
                maxInd = i;
            }
        }
        // Calculate the frequency
        double frequency = (double)sampleRate * maxInd / (double) (a.length / 2);
        return new double[] {frequency, maxMagnitude};
    }

    private void buildHammWindow(int size) {
        if(window != null && window.length == size) {
            return;
        }
        window = new double[size];
        for(int i = 0; i < size; ++i) {
            window[i] = .54 - .46 * Math.cos(2 * Math.PI * i / (size - 1.0));
        }
    }

    private double[] applyWindow(short[] input) {
        double[] res = new double[input.length];

        buildHammWindow(input.length);
        for(int i = 0; i < input.length; ++i) {
            res[i] = (double)input[i] * window[i];
        }
        return res;
    }
}
