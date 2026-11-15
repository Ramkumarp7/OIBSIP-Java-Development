@echo off
cd /d "%~dp0"
mvn clean compile exec:java
if errorlevel 1 pause
