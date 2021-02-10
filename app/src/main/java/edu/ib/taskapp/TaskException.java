package edu.ib.taskapp;

/**
 *  Task exception throw while creating new task
 */
public class TaskException extends Exception{
    /**
     * Constructor with message of Exception
     * @param message Message of exception
     */
    public TaskException(String message){
        super(message);
    }

}
