@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "__MVNW_ARG0_NAME__=%~n0")
@SET MAVEN_WRAPPER_JAR="%~dp0.mvn\wrapper\maven-wrapper.jar"
@SET MAVEN_WRAPPER_PROPERTIES="%~dp0.mvn\wrapper\maven-wrapper.properties"

@FOR /F "usebackq tokens=1,2 delims==" %%a IN (%MAVEN_WRAPPER_PROPERTIES%) DO (
  @IF "%%a"=="distributionUrl" SET DISTRIBUTION_URL=%%b
)

@SETLOCAL
@SET JAVA_HOME_CANDIDATE=%JAVA_HOME%
@IF NOT "%JAVA_HOME_CANDIDATE%"=="" (
  SET JAVA_EXE="%JAVA_HOME_CANDIDATE%\bin\java.exe"
) ELSE (
  SET JAVA_EXE=java
)

%JAVA_EXE% -jar %MAVEN_WRAPPER_JAR% %*

@ENDLOCAL
