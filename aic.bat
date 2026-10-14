@echo off
chcp 65001 > nul
java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Dapp.dir="%~dp0." "%~dp0AiCommit.java" %*