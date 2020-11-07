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
* Grey_Rubiks.java
  - extends Rubiks.java
  - 
* RGB_Rubiks.java
  - sds
```
            ,---------------------------------------.                                              
            |RGB_Rubiks                             |                                              
            |---------------------------------------|                                              
            |---------------------------------------|                                              
            |+_Rubiks(ImageJ imp, ImageProcessor ip)|                                              
            |+void encrypt()                        |                                              
            |+void decrypt()                        |                                              
            `---------------------------------------'                                              
                                                                                                   
                                                                                                   
                                                  ,-----------------------------------------------.
,---------------------------------------.         |Runner                                         |
|Grey_Rubiks                            |         |-----------------------------------------------|
|---------------------------------------|         |-String command                                |
|---------------------------------------|         |-ImagePlus imp                                 |
|+_Rubiks(ImageJ imp, ImageProcessor ip)|         |-----------------------------------------------|
|+void encrypt()                        |         |~Runner(String command, ImagePlus imp)         |
|+void decrypt()                        |         |+void run()                                    |
`---------------------------------------'         |~void runCommand(String command, ImagePlus imp)|
                                                  `-----------------------------------------------'
                                                                          |                        
                         ,----------------------------------------.       |                        
                         |Rubiks_                                 |       |                        
                         |----------------------------------------|       |                        
     ,---------------.   |-Panel panel                            |       |                        
     |Rubiks         |   |-int previousID                         |       |                        
     |---------------|   |-Frame instance                         |   ,------.                     
     |---------------|   |~String[] choices                       |   |Thread|                     
     |+void encrypt()|   |----------------------------------------|   `------'                     
     |+void decrypt()|   |~void addButton(String label, int index)|                                
     `---------------'   |+void actionPerformed(ActionEvent e)    |                                
                         |+void processWindowEvent(WindowEvent e) |                                
                         `----------------------------------------'                                
                                                                                                   
                                                                                                   
                             ,--------------.   ,-----------.                                      
                             |ActionListener|   |PlugInFrame|                                      
                             `--------------'   `-----------'                                      
```