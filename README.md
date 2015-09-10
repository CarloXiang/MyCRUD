README
===========================


****
###　　　　　　　　　　　　Author:Lacelove
###　　　　　　　　　 E-mail:lacelove@163.com


===========================
#Setup and Configure JDK
##Download JDK
##Setup JDK
##Configure JDK
* Set System Variable, e.g.,
   * JAVA_HOME=C:\Program Files\Java\jdk1.8.0_45
   * CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
   * PATH=;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;

#Setup Tomcat
#Setup and Configure MySQL
##Download MySQL
##Setup MySQL
##Configure MySQL
* Set System Variable
  * PATH= C:\MYSQL\mysql-5.6.26-winx64\mysql-5.6.26-winx64\bin;
  * install and start
    * C:\>mysqld install
    * C:\>net start mysql

#GitHub
##Setup Git Windows Client Tools
* Setup msysgit, e.g., Git-1.9.5-preview20141217.exe
* Setup TortoiseGit

#Clone
* New a directory, e.g., c:\mygit
* Night click the directory
* [Git Clone…]-[URL: https://github.com/lacelove/MyCRUD]

#Run Application
* **Setup IntellijIDEA**
* **Open Project in IntellijIDEA**: [File]-[Open]-[MyCRUD.iml]
* **Set Tomcat home directory in IntellijIDEA**: [Run]-[Run…]-[Edit Configuration…]-[Tomcat Server]-[Tomcat]-[Application server]-[Configure…]-[Tomcate Home: C:\Program Files\Apache Software Foundation\Tomcat 8.0]
* **Set Java SDK in IntellijIDEA**: [File]-[Project Structure]-[Project Setting]-[Modules]-[New…]-[JDK]-[select java jdk home directory, e.g., C:\Program Files\Java\jdk1.8.0_45]
* **Create database in MySQL**:
  * C:\>mysql –uroot –p
  * mysql>source MyCRUD.sql 
    * the MyCRUD.sql exists in the source file: \src\test\resources\MyCRUD.sql
* **Run**: [Run]-[Run ‘Tomcat’]

#Functional
##User login
* http://localhost:8081/
* Username: admin
* Password: 123456

##User forget password (TODO)
##User register
##User logout
##Admin login
* http://localhost:8081/rest/admin/adminlogin
* Username: admin
* Password: 123456

##Admin add user
##Admin manager user
* User list
* Edit User
* Delete User

##Admin logout
