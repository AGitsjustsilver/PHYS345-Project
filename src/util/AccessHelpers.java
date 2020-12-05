package util;

public interface AccessHelpers {

    void swap(int posA, int posB);

    byte arrByteAccess(boolean rowcol, int iterator, int staticField);
    byte getByteKey(boolean key, int index);
    int arrSum(boolean rowcol, int index);

    int arrIntAccess(boolean rowcol, int iterator, int staticField);
    int getIntKey(boolean key, int index);
    int[] arrSumRGB(boolean rowcol, int index);

}
