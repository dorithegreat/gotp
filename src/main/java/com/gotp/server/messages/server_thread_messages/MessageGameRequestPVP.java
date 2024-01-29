package com.gotp.server.messages;

public class MessageGameRequestPVP implements Message {
    //I changed these only because the program wouldn't compile otherwise
    //it's garbage and it's not meant to stay like this
    public MessageFunction getFunction(){
        return MessageFunction.ERROR;
    }
    public MessageType getType(){
        return MessageType.DEBUG;
    }
}
