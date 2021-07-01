@ECHO OFF
MKDIR "files"
START /MIN /D .\appmodule mvnw.cmd javafx:run
EXIT