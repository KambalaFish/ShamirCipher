package com.company;

import java.util.Random;
import java.util.Scanner;

public class ShamirCipher {
    private final int p;
    private final Random random;
    public ShamirCipher(int p){
        random = new Random();
        this.p = p;
    }
    private EuclidRow generalizedEuclidAlgorithm(int a, int b){
//        swap
        if (b>a){
            a = a + b;
            b = a - b;
            a = a - b;
        }
        EuclidRow u = new EuclidRow(a, 1, 0);
        EuclidRow v = new EuclidRow(b, 0, 1);
        EuclidRow t = new EuclidRow(0,0,0);

        while (v.gcd!=0){
            int q = u.gcd / v.gcd;
            t.gcd = u.gcd % v.gcd;
            t.a = u.a - q * v.a;
            t.b = u.b - q * v.b;
            u.set(v);
            v.set(t);
        }
        return u;
    }

    private int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    private int getC(){
        int c = generateRandomNumber(2, p-2);
        while (generalizedEuclidAlgorithm(p-1, c).gcd!=1){
            c = generateRandomNumber(2, p-2);
        }
        return c;
    }

    private int getD(int c){
        int d = generalizedEuclidAlgorithm(p-1, c).b;
        if (d<0)
            return d + (p-1);
        return d;
    }

    private int calculatePowerByMod(int base, int power) {
        int result = 1;
        while (power > 0) {
            if ((power & 1) == 1)
                result = (result * base) % p;
            base = (base * base) % p;
            power = power >> 1;
        }
        return result;
    }

    public void encrypt(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input message: ");
        int message = sc.nextInt();
        int cA, cB, dA, dB;
        int x1, x2, x3, x4;
        if (message>=p){
            System.out.println("Message must be less than P number");
            return;
        }

        cA = getC();
        cB = getC();

        dA = getD(cA);
        dB = getD(cB);

        System.out.println("cA = "+cA+" dA = "+dA);
        System.out.println("cB = "+cB+" dB = "+dB);

        System.out.println("Alice wants to send Bob message: "+message);
        x1 = calculatePowerByMod(message, cA);
        System.out.println("Alice sends Bob x1 = " + x1);
        x2 = calculatePowerByMod(x1, cB);
        System.out.println("Bob sends Alice x2 = " + x2);
        x3 = calculatePowerByMod(x2, dA);
        System.out.println("Alice sends Bob x3 = " + x3);
        x4 = calculatePowerByMod(x3, dB);
        System.out.println("Bob calculated x4 = " + x4 + " which was Alice's message.");

    }
}
