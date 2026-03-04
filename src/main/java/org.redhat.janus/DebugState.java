package org.redhat.janus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DebugState {

    private boolean debug = false;
    private boolean trigger = false;
    private String port = "80/443";

    public void setDebugState(boolean newState) {
        debug = newState;
    }

    public boolean getDebugState() {
        return debug;
    }

    public void setTrigger(boolean triggering) {
        trigger = triggering;
    }

    public boolean getTrigger() {
        return trigger;
    }   
    
    public void setPort(String portnum) {
        port = portnum;
    }

    public String getPort() {
        return port;
    }     
}    
