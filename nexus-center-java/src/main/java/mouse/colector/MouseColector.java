/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mouse.colector;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.POINT;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author thami
 */
public class MouseColector {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private POINT lastMousePosition;
    private long lastActivityTime;
    private long timeout;
    private String lastCurrrentTime;
    private Integer currentx;
    private Integer currenty;
    private Integer lastx;
    private Integer lasty;
    private Boolean movimento;

    public MouseColector(long timeout) {
        this.timeout = timeout;
//        lastMousePosition = getMousePosition();
        lastActivityTime = System.currentTimeMillis();
    }

    public MouseColector() {
    }

    public POINT getMousePosition() {
        POINT point = new POINT();


        User32.INSTANCE.GetCursorPos(point);
        return point;
    }

    public void showMouseposition() {
        POINT currentMousePosition = getMousePosition();
        currentx = currentMousePosition.x;
        currenty = currentMousePosition.y;
        if (currentx.equals(lastx) && currenty.equals(lasty)) {
            movimento = false;
        } else {
            movimento = true;
        }

        lastMousePosition = currentMousePosition;
        lastx = currentx;
        lasty = currenty;
        lastActivityTime = System.currentTimeMillis();
        lastCurrrentTime = LocalDateTime.now().format(formatter);

        System.out.println("Movimentos do Mouse \n coord x:" + currentx + "\n coord y:" + currenty + " \n Horário : " + lastCurrrentTime);
    }

    public String isPositionStoped() {
        if (movimento) {
            return "ativo";
        } else {
            return "desativado";
        }

    }

}
