# PHYS345-Project
Implementation and expansion on the rubik's cube based image encryption algorithm in this [paper](https://www.hindawi.com/journals/jece/2012/173931/). Credit for the algorithm and analysis goes to Khaled Loukhaoukha, Jean-Yves Chouinard, and Abdellah Berdai.

## Strucutre
* Rubiks_.java 
  - Contains the frontward facing interface for the plugging
* Rubiks.java
  - abstract class 
    - encrypt
    - decrypt
  - contains helper code to assist extending classes
* ByteRubiks.java
  - extends Rubiks.java
  - performs encryption and decryption on n-bit grey images.
* RGBRubiks.java
  - extends ByteRubiks.java
  - treats RGB image as a composite of 3 ByteGrey Images and uses super class to perform encryption and decryption
  
