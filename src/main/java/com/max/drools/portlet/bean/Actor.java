package com.max.drools.portlet.bean;

/**
 * Created with IntelliJ IDEA.
 * User: USER
 * Date: 16.07.12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class Actor {

    private boolean logic = true;

    public void printToConsole(String string) {
        System.out.println("The result is:" + string);
    }

    public boolean isLogic() {
        return logic;
    }

    public void setLogic(boolean logic) {
        this.logic = logic;
    }
}
