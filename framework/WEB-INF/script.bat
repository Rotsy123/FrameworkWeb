cd etu2009.framework.servlet/servlet
javac -d . ModelView.java 
javac -d . FileUpload.java
javac -d . GetUrl.java
move etu2009 ../model
cd ../model
javac -d . *.java
move etu2009 ../servlet
cd ../servlet
javac -cp "C:/Program Files/Apache Software Foundation/Tomcat 10.0/lib/servlet-api.jar;C:/Program Files/Apache Software Foundation/Tomcat 10.0/lib/commons-io-2.7.jar" -d . *.java
cd ../../classes
rmdir /s etu2009
cd ../etu2009.framework.servlet/servlet
move etu2009 ../../classes
cd ../../
jar -cf framework.jar classes/etu2009/framework/servlet
move framework.jar ../../testframe/WEB-INF/lib