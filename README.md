# Mail Parser

Simple parser to produce a csv from a freetext file.

## Useage
```
java -jar MailParser-assembly-1.0.jar <inputfile>
```

This might appear in a windows batch file as follows:
```
@ECHO OFF
set JAVA_HOME=c:\path\to\jdk
%JAVA_HOME%\bin\java -jar MailParser-assembly-1.0.jar %1
```

If you save the above text in a file called ```parser.bat``` then it can be run as follows:
```
parser.bat <inputfile>
```

This suggestion is untested and may require a little rework to get it working.

## Known Issues
The MailParser-assembly-1.0.jar file must be located in the same directory as the input file.

