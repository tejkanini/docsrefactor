set ACTIVE_PORT=8080
set CUR_DIR=%CD%

docker run -itd --name ai-server -p %ACTIVE_PORT%:8080 -v "%CUR_DIR%/semosshome":/opt/semosshome docker.cfg.deloitte.com/genai/genai-server:4.3.0-SNAPSHOT-2023-10-19
TIMEOUT 10
ECHO Opening browser to http://localhost:%ACTIVE_PORT%/SemossWeb/packages/client/dist/#/ to access SEMOSS...
ECHO To stop the server, please execute stopAIServer.bat
ECHO OFF
IF EXIST "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" (  START "Chrome" chrome --new-window "http://localhost:%ACTIVE_PORT%/SemossWeb/packages/client/dist/#/"			) ELSE (  START "" "http://localhost:%ACTIVE_PORT%/SemossWeb/packages/client/dist/#/")
docker logs -f ai-server