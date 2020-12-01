import ij.process.*;

public class ByteRubiks extends Rubiks {

    public ByteRubiks(){
        super();
    }

    public ByteRubiks(ImageProcessor ip){
        super(ip);
    }

    @Override
    public void encrypt() {
//         Algorithm Steps
//        * 1. Generate 2 different size arrays R and C
//        * 2. Determine the max number of Iterations //
        int iter = 0,
            width = this.ip.getWidth(),
            height = this.ip.getHeight();

        while (!checkIter(iter)) {
//        * 3. Increment iterator by 1
            iter++;
            this.rowShift(width);
            this.colShift(height);
            this.rowXOR(height, width);
            this.colXOR(height, width);

//        * 8. if iter and iterMax are equal then its done else go again
        }

        // update ImageProcessor
        this.ip.setIntArray(this.imgArr);
    }

    @Override
    public void decrypt() {
        int iter = 0,
                width = this.ip.getWidth(),
                height = this.ip.getHeight();

        while(!checkIter(iter)){
            iter++;
            this.colXOR(height, width);
            this.rowXOR(height, width);
            this.colShift(height);
            this.rowShift(width);
        }

        this.ip.setIntArray(this.imgArr);
    }

    private void rowShift(int width){
        //        * 4. For each row of image:
        for(int row=0; row < width; row++) {
//        *   a. calculate sum all values in row
//        *   b. take modulo 2
            // shift multiple times by the vector value at the index
            if(arrSum(this.imgArr[row])%2==0) {
//        *   c. if 0 -> right circular shift
                shift(Dir.RIGHT, row, true);
            }else {
//        *      else -> left circular shift
                shift(Dir.LEFT, row, true);
            }
        }
    }

    private void colShift(int height){
        //        * 5. For each column of image:
        for(int col=0; col < height; col++) {
//        *   a. calculate the sum of all column values
//        *   b. compute modulo 2
            // shift multiple times by the vector value at the index
            if(arrSum(this.imgArr[col]) % 2 == 0) {
//        *   c. if 0 -> up circular shift
                shift(Dir.UP,col, false);
            }else {
//        *      else ->down circular shift
                shift(Dir.DOWN,col, false);
            }
        }
    }

    private void rowXOR(int height, int width){
        //        * 6. Using Vector Key C apply XOR to rows
        for (int i = 0; i < width; i++) {
            if (i%2!=0) {
                // a. Image[2i-1][j] (odd rows) get regular XOR of C[j]
                for (int j = 0; j < height; j++) {
                    xorShift(i,j,false);
                }
            }else {
                // b. Image[2i][j] (even rows) get left bit shifted C[j]
                for (int j = 0; j < height; j++) {
                    xorRotShift(i,j,false);
                }
            }
        }
    }

    private void colXOR(int height, int width){
        //        * 7. Using Vector Key R apply XOR to columns
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(j%2!=0) {
                    // a. Image[i][2j-1] (odd columns) get regular XOR of R[j]
                    xorShift(i,j,true);
                }else {
                    // b. Image[i][2j] (even columns) get left bit shifted R[j]
                    xorRotShift(i,j,true);
                }
            }
        }
    }
}
