package util;

public interface AccessHelpers {

    void swap(byte[] array, int posA, int posB);

    byte arrByteAccess(boolean rowcol, int iterator, int staticField);
    int arrSum(boolean rowcol, int index);

    int[] arrSumRGB(boolean rowcol, int index);

}
