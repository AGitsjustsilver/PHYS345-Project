import ij.*;
/*
 * The back end methods for the plugin interface
 * */

abstract class Rubiks {

    // actions that each individual rubiks type has to do with
    public abstract void encrypt();
    public abstract void decrypt();
}

