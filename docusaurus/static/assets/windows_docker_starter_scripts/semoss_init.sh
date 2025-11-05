cd /opt/semosshome/;
sed -i "s|<NATIVE_ENABLE>|true|" social.properties;
sed -i "s|<NATIVE_REGISTRATION_ENABLE>|true|" social.properties;
sed -i "s|<REDIRECT>|http://localhost:$ACTIVE_PORT/SemossWeb/packages/client/dist|" social.properties;
$TOMCAT_HOME/bin/stop.sh;
$TOMCAT_HOME/bin/start.sh;
exit
