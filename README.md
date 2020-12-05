# PHYS345-Project
Implementation and expansion on the rubik's cube based image encryption algorithm in this [paper](https://www.hindawi.com/journals/jece/2012/173931/). Credit for the algorithm and analysis goes to Khaled Loukhaoukha, Jean-Yves Chouinard, and Abdellah Berdai.

## Strucutre
* Rubiks_.java 
  - Contains the frontward facing interface for the plugging
* algorithm.Rubiks.java
  - abstract class 
    - encrypt
    - decrypt
  - contains helper code to assist extending classes
* algorithm.ByteRubiks.java
  - extends algorithm.Rubiks.java
  - performs encryption and decryption on n-bit grey images.
* algorithm.RGBRubiks.java
  - extends algorithm.ByteRubiks.java
  - treats RGB image as a composite of 3 ByteGrey Images and uses super class to perform encryption and decryption
  
