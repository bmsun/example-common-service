######JDK1.8版本
######解决AES256加密出现的java.security.InvalidKeyException: Illegal key size 异常
用local_policy.jar、US_export_policy.jar覆盖%JDK_HOME%\jre\lib\security\policy下的文件