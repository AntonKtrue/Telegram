package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Anton on 15.05.2016.
 */
public class FrameDragger implements MouseListener, MouseMotionListener {
    private JFrame frameToDrag;

    private Point lastDragPosition;

    public FrameDragger(JFrame frameToDrag) {
        this.frameToDrag = frameToDrag;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currentDragPosition = e.getLocationOnScreen();
        int deltaX = currentDragPosition.x - lastDragPosition.x;
        int deltaY = currentDragPosition.y - lastDragPosition.y;
        if (deltaX != 0 || deltaY != 0) {
            int x = frameToDrag.getLocation().x + deltaX;
            int y = frameToDrag.getLocation().y + deltaY;
            frameToDrag.setLocation(x, y);
            lastDragPosition = currentDragPosition;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastDragPosition = e.getLocationOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
