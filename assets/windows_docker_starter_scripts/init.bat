set CUR_DIR=%CD%
set ACTIVE_PORT=8080
set SEMOSS_BASH_INIT="export ACTIVE_PORT=%ACTIVE_PORT%; cd /opt/semosshome; semoss_init.sh"

docker run -itd --name semosstmp -p %ACTIVE_PORT%:8080 docker.cfg.deloitte.com/genai/genai-server:4.3.0-SNAPSHOT-2023-10-19
docker cp semoss_init.sh semosstmp:/opt/semosshome
docker exec -itd semosstmp bash -c %SEMOSS_BASH_INIT%

TIMEOUT 10
IF EXIST "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" (  START "Chrome" chrome --new-window "http://localhost:%ACTIVE_PORT%/SemossWeb/packages/client/dist"			) ELSE (  START "" "http://localhost:%ACTIVE_PORT%/SemossWeb/packages/client/dist")
IF EXIST "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" (  START "Chrome" chrome --new-tab "http://localhost:%ACTIVE_PORT%/Monolith/setAdmin/"			) ELSE (  START "" "http://localhost:%ACTIVE_PORT%/Monolith/setAdmin/")
