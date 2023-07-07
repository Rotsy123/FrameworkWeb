cd etu2009.framework.servlet/servlet
javac -parameters -d . ModelView.java 
javac -parameters -d . FileUpload.java
javac -parameters -d . GetUrl.java
javac -parameters -d . Scopeannotation.java
move etu2009 ../model
cd ../model
javac -parameters -d . *.java
javac -parameters -d . Employe.java
move etu2009 ../servlet
cd ../servlet
javac -parameters -source 17 -cp "C:/Program Files/Apache Software Foundation/Tomcat 10.0/lib/*" -d . *.java
cd ../../classes
rmdir /s etu2009
cd ../etu2009.framework.servlet/servlet
move etu2009 ../../classes
cd ../../
jar -cf framework.jar etu2009\framework\servlet
move framework.jar ../../testframe/WEB-INF/lib 
