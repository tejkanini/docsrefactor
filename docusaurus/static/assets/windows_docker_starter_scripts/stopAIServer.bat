ECHO Stopping server, please wait...
ECHO OFF
docker stop ai-server
docker rm ai-server
ECHO Server stopped.  To restart, please execute startAIServer.bat
ECHO OFF