README
===========================


****
###　　　　　　　　　　　　Author:Lacelove
###　　　　　　　　　 E-mail:lacelove@163.com


===========================
1.	Setup and Configure JDK
1.1.	Download JDK
1.2.	Setup JDK
1.3.	Configure JDK
	Set System Variable, e.g.,
	JAVA_HOME=C:\Program Files\Java\jdk1.8.0_45
	CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
	PATH=;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
2.	Setup Tomcat
3.	Setup and Configure MySQL
3.1.	Download MySQL
3.2.	Setup MySQL
3.3.	Configure MySQL
	Set System Variable
	PATH= C:\MYSQL\mysql-5.6.26-winx64\mysql-5.6.26-winx64\bin;
	Install and start
	C:\>mysqld install
	C:\>net start mysql
4.	GitHub
4.1.	Setup Git Windows Client Tools
	Setup msysgit, e.g., Git-1.9.5-preview20141217.exe
	Setup TortoiseGit
4.2.	Clone
	New a directory, e.g., c:\mygit
	Right click the directory
	[Git Clone…]-[URL: https://github.com/lacelove/MyCRUD]
5.	Run Application
	Setup IntellijIDEA
	Open Project in IntellijIDEA: [File]-[Open]-[MyCRUD.iml]
	Set Tomcat home directory in IntellijIDEA: [Run]-[Run…]-[Edit Configuration…]-[Tomcat Server]-[Tomcat]-[Application server]-[Configure…]-[Tomcate Home: C:\Program Files\Apache Software Foundation\Tomcat 8.0]
	Set Java SDK in IntellijIDEA: [File]-[Project Structure]-[Project Setting]-[Modules]-[New…]-[JDK]-[select java jdk home directory, e.g., C:\Program Files\Java\jdk1.8.0_45]
	Create database in MySQL:
	C:\>mysql –uroot –p
	mysql>source MyCRUD.sql 
	the MyCRUD.sql exists in the source file: \src\test\resources\MyCRUD.sql
	Run: [Run]-[Run ‘Tomcat’]
6.	Functional
6.1.	User login
	http://localhost:8081/
	Username: admin
	Password: 123456
6.2.	User forget password (TODO)
6.3.	User register
6.4.	User logout
6.5.	Admin login
6.5.1.	Admin login workflow
	http://localhost:8081/rest/admin/adminlogin
	Username: admin
	Password: 123456
6.6.	Admin add user
6.7.	Admin manager user
6.7.1.	User list
6.7.2.	Edit User
6.7.3.	Delete User
6.8.	Admin logout
