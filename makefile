# Makefile for student submission for CS352 Semester project
# Declaration of USERNAME below must be customized to actual user

USERNAME = aardvar

all: sqlj javac bind

sqlj:
	@echo Ignore warning about undefined parameter type Fine
	cd semesterproject; sqlj -compile=false Database.sqlj

javac:
	javac -classpath project.jar:$$CLASSPATH semesterproject/Database.java
	
bind:
	@echo Please enter password when prompted for it
	cd semesterproject; /gc/cps352/bind \
		-rootpkgname $(USERNAME) \
		-url jdbc:db2:project \
		-user $(USERNAME) \
		Database_SJProfile0.ser
	
	
	