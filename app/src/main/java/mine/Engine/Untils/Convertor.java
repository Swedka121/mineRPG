package mine.Engine.Untils;

import java.util.ArrayList;

public class Convertor {
    public static int[] convertI(ArrayList<Integer> list) {
        int[] intArray = new int[list.size()]; // Create an int array of the same size as the ArrayList
        for (int i = 0; i < list.size(); i++) {
            intArray[i] = list.get(i); // Unbox each Integer to an int
        }

        return intArray;
    }
    public static float[] convertF(ArrayList<Float> list) {
        float[] intArray = new float[list.size()]; // Create an int array of the same size as the ArrayList
        for (int i = 0; i < list.size(); i++) {
            intArray[i] = list.get(i); // Unbox each Integer to an int
        }

        return intArray;
    }
}
