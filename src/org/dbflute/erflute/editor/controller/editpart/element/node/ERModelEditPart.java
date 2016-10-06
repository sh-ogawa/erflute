package org.dbflute.erflute.editor.controller.editpart.element.node;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbflute.erflute.core.DesignResources;
import org.dbflute.erflute.editor.controller.editpolicy.ERDiagramLayoutEditPolicy;
import org.dbflute.erflute.editor.model.ViewableModel;
import org.dbflute.erflute.editor.model.diagram_contents.element.connection.ConnectionElement;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.NodeElement;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel.ERModel;
import org.dbflute.erflute.editor.view.drag_drop.ERDiagramTransferDragSourceListener;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Color;

public class ERModelEditPart extends NodeElementEditPart {

    @Override
    public void doPropertyChange(PropertyChangeEvent event) {

        if (event.getPropertyName().equals(ERModel.PROPERTY_CHANGE_VTABLES)) {
            // �e�[�u���z�u�ύX
            this.refreshChildren();
            this.refresh();
        } else if (event.getPropertyName().equals(ConnectionElement.PROPERTY_CHANGE_CONNECTION)) {
            System.out.println("aa");
        } else if (event.getPropertyName().equals(ViewableModel.PROPERTY_CHANGE_COLOR)) {
            this.refreshVisuals();
        }

    }

    @Override
    public void refresh() {
        super.refresh();

        // �����̃R�����g�A�E�g�O����StackOverflow�ɂȂ�B�ʂ̂Ƃ���Ń��t���b�V������K�v����
        //		Map<NodeElement, EditPart> part = getModelToEditPart();
        //		for (Entry<NodeElement, EditPart> entry : part.entrySet()) {
        //			if (entry.getKey() instanceof ERVirtualTable) {
        //				entry.getValue().addNotify();
        //			}
        //		}

    }

    @Override
    protected List getModelChildren() {
        final List<Object> modelChildren = new ArrayList<Object>();
        final ERModel model = (ERModel) this.getModel();
        modelChildren.addAll(model.getGroups());
        modelChildren.addAll(model.getTables());
        modelChildren.addAll(model.getNotes());
        return modelChildren;
    }

    //	@Override
    //	protected Rectangle getRectangle() {
    //		// TODO Auto-generated method stub
    //		return super.getRectangle();
    //	}

    @Override
    protected IFigure createFigure() {
        //		ERModel ermodel = (ERModel) this.getModel();
        //		ERModelFigure figure = new ERModelFigure(ermodel.getName());
        //		return figure;

        final FreeformLayer layer = new FreeformLayer();
        layer.setLayoutManager(new FreeformLayout());
        return layer;
    }

    @Override
    protected void createEditPolicies() {
        this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new ERDiagramLayoutEditPolicy());
    }

    @Override
    public void refreshVisuals() {
        final ERModel element = (ERModel) this.getModel();

        final int[] color = element.getColor();

        if (color != null) {
            final Color bgColor = DesignResources.getColor(color);
            this.getViewer().getControl().setBackground(bgColor);
        }

        for (final Object child : this.getChildren()) {
            if (child instanceof NodeElementEditPart) {
                final NodeElementEditPart part = (NodeElementEditPart) child;
                part.refreshVisuals();
            }
        }
    }

    //	private void internalRefreshTable(ERTable table) {
    //		for (Entry<NodeElement, EditPart> entry : getModelToEditPart().entrySet()) {
    //			if (entry.getKey().equals(table)) {
    //				// �e�[�u���̍X�V
    //				entry.getValue().refresh();
    //			}
    //		}
    //		
    //		
    //	}

    public void refreshRelations() {
        for (final Object child : this.getChildren()) {
            if (child instanceof NodeElementEditPart) {
                final NodeElementEditPart part = (NodeElementEditPart) child;
                part.refreshConnections();
            }
        }
    }

    private Map<NodeElement, EditPart> getModelToEditPart() {
        final Map<NodeElement, EditPart> modelToEditPart = new HashMap<NodeElement, EditPart>();
        final List children = getChildren();

        for (int i = 0; i < children.size(); i++) {
            final EditPart editPart = (EditPart) children.get(i);
            modelToEditPart.put((NodeElement) editPart.getModel(), editPart);
        }

        return modelToEditPart;
    }

    @Override
    protected void performRequestOpen() {
        // TODO Auto-generated method stub

    }

    @Override
    public EditPart getTargetEditPart(Request request) {
        if (ERDiagramTransferDragSourceListener.REQUEST_TYPE_PLACE_TABLE.equals(request.getType())) {
            return this;
        }
        return super.getTargetEditPart(request);
    }

}