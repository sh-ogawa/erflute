package org.dbflute.erflute.editor.controller.editpart.element;

import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.settings.PageSettings;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.swt.graphics.Color;

public class PagableFreeformRootEditPart extends ScalableFreeformRootEditPart {

    private ERDiagram diagram;

    public PagableFreeformRootEditPart(ERDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    protected GridLayer createGridLayer() {
        return new PagableGridLayer();
    }

    private class PagableGridLayer extends GridLayer {

        private int i = 0;

        @Override
        protected void paintGrid(Graphics g) {
            super.paintGrid(g);

            Rectangle clip = g.getClip(Rectangle.SINGLETON);

            PageSettings pageSetting = diagram.getPageSetting();

            int width = pageSetting.getWidth();
            int height = pageSetting.getHeight();

            Rectangle rect = clip;

            Color color = g.getForegroundColor();
            g.setForegroundColor(ColorConstants.lightGray);

            int startX = rect.x;
            if (startX > 0) {
                startX = 0;
            }
            int startY = rect.y;
            if (startY > 0) {
                startY = 0;
            }

            for (int i = startX; i < rect.x + rect.width; i += width) {
                g.drawLine(i, rect.y, i, rect.y + rect.height);
            }

            for (int i = startY; i < rect.y + rect.height; i += height) {
                g.drawLine(rect.x, i, rect.x + rect.width, i);
            }

            g.setForegroundColor(color);

            i++;
            if (i > 0) {
                i = -1;
                repaint();
            }
        }
    }
}
