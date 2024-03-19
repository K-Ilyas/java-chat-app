

javac SocketServer.java
javac SocketClient.java


@echo off
start cmd.exe /k java SocketServer
set /p num=Enter the number of clinet to lunch: 
for /l %%i in (1,1,%num%) do (
    start cmd.exe /k java SocketClient
)
pause
