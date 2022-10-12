package com.todolist.utill;

public class ExceptionUtils {

    public static String getStackTrace(Throwable e){

        StringBuilder builder = new StringBuilder();
        builder.append(" Exception Name : " + e + "\n");

        StackTraceElement[] stackTraceElements = e.getStackTrace();
        if(stackTraceElements.length != 0) {
            builder.append("=======================STACK TRACE=======================\n");
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                builder.append("Class : " + stackTraceElement.getClassName() + " / ");
                builder.append("Line : [" + stackTraceElement.getLineNumber() + "]\n");
            }
        }

        return builder.toString();
    }
}
