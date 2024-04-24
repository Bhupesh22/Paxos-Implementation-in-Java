@echo off

:: Start server instances
start "rmiregistry" cmd.exe /k start rmiregistry
start "Server1" cmd.exe /k java Server/Server1
start "Server2" cmd.exe /k java Server/Server2
start "Server3" cmd.exe /k java Server/Server3
start "Server4" cmd.exe /k java Server/Server4
start "Server5" cmd.exe /k java Server/Server5

:: Start the client
start "Client" cmd.exe /k java Client/Client Server1

echo All servers and client have been started.