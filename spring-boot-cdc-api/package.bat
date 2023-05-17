
@ECHO OFF

SET TARGET_FOLDER="%~dp0\target"

IF EXIST %TARGET_FOLDER% (
    ECHO Starting to delete old target folder!!
    RD /s /q "%TARGET_FOLDER%\"
)

ECHO package staring!!

CALL mvn package

ECHO package finished!!

pause