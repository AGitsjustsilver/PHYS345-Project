import ij.process.*;

public class ByteRubiks extends Rubiks {
    public ByteRubiks(ImageProcessor ip, VectorKeys keys){
        super(ip, keys);
    }

    @Override
    public void encrypt() {
//         Algorithm Steps
//        * 1. Generate 2 different size arrays R and C
//        * 2. Determine the max number of Iterations //
        int iter = 0;

        while (checkIteratorMax(iter)) {
//        * 3. Increment iterator by 1
            iter++;
            this.rowShift();
            this.colShift();
            this.rowXOR();
            this.colXOR();

//        * 8. if iter and iterMax are equal then its done else go again
        }

        // update ImageProcessor
        this.ip.setIntArray(this.imgArr);
    }

    @Override
    public void decrypt() {
        int iter = 0;

        while(checkIteratorMax(iter)){
            iter++;
            this.colXOR();
            this.rowXOR();
            this.colShift();
            this.rowShift();
        }

        this.ip.setIntArray(this.imgArr);
    }

    @Override
    protected void shiftElements(Direction d, int index, boolean key) {
        // col = UP/DOWN = true
        // row = LEFT/RIGHT = false;
        boolean direction = (d == Direction.UP)||(d == Direction.DOWN);
        if(direction) {
            this.colSwitch();
            d = (d == Direction.UP)? Direction.LEFT: Direction.RIGHT;
        }
        int bound = this.imgArr[index].length-1;
        for (int i = 0; i < getKey(key, index); i++) {
            if (d == Direction.RIGHT){
                // shift ->
                for (int row = 0; row < bound; row++){
                    swap(this.imgArr[index], row, row+1);
                }
                swap(this.imgArr[index], bound-1, bound);
            }else {
                // shift <-
                for (int row = bound; row > 0; row--){
                    swap(this.imgArr[index], row-1, row);
                }
                swap(this.imgArr[index], 1, 0);
            }
        }
        if(direction)
            this.colSwitch();
    }

    @Override
    protected void rowShift(){
        //        * 4. For each row of image:
        for(int row=0; row < width; row++) {
//        *   a. calculate sum all values in row
//        *   b. take modulo 2
            // shift multiple times by the vector value at the index
            if(arrSum(this.imgArr[row])%2==0) {
//        *   c. if 0 -> right circular shift
                shiftElements(Direction.RIGHT, row, true);
            }else {
//        *      else -> left circular shift
                shiftElements(Direction.LEFT, row, true);
            }
        }
    }

    @Override
    protected void colShift(){
        //        * 5. For each column of image:
        for(int col=0; col < height; col++) {
//        *   a. calculate the sum of all column values
//        *   b. compute modulo 2
            // shift multiple times by the vector value at the index
            if(arrSum(this.imgArr[col]) % 2 == 0) {
//        *   c. if 0 -> up circular shift
                shiftElements(Direction.UP,col, false);
            }else {
//        *      else ->down circular shift
                shiftElements(Direction.DOWN,col, false);
            }
        }
    }

    @Override
    protected void rowXOR(){
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
                    rotXorShift(i,j,false);
                }
            }
        }
    }

    @Override
    protected void colXOR(){
        //        * 7. Using Vector Key R apply XOR to columns
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(j%2!=0) {
                    // a. Image[i][2j-1] (odd columns) get regular XOR of R[j]
                    xorShift(i,j,true);
                }else {
                    // b. Image[i][2j] (even columns) get left bit shifted R[j]
                    rotXorShift(i,j,true);
                }
            }
        }
    }

    @Override
    protected void xorShift(int i, int j, boolean key){
        this.imgArr[i][j] = this.imgArr[i][j] ^ getKey(key, j);
    }

    @Override
    protected void rotXorShift(int i, int j, boolean key){
        this.imgArr[i][j] = this.imgArr[i][j] ^ (getKey(key, j) >> 1);
    }

    // object

    @Override
    public String toString() {
        return super.toString();
    }
}
