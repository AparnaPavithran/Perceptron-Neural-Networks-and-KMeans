Compile :
javac K_Means.java

Run :
java K_Means Penguins.jpg 4 pen 800
java K_Means Penguins.jpg 4 pen 1000

Assumptions :
1. First value in the input string is picture name(along with the path if not contained in the same folder in which the file is running).
2. Second value is the number of clusters.
3. Third value is the output picture name. (output is given in jpg format by default) 
4. Fourth value is the number of iterations are given to make sure that it is not running for a long time to converge if for any random initialization.
5. Program will not work if no input string is given.
