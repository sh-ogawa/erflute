package org.dbflute.erflute.editor.view.action.option.notation.level;

import org.dbflute.erflute.editor.MainDiagramEditor;
import org.dbflute.erflute.editor.model.settings.DiagramSettings;

public class ChangeNotationLevelToNameAndKeyAction extends AbstractChangeNotationLevelAction {

    public static final String ID = ChangeNotationLevelToNameAndKeyAction.class.getName();

    public ChangeNotationLevelToNameAndKeyAction(MainDiagramEditor editor) {
        super(ID, editor);
    }

    @Override
    protected int getLevel() {
        return DiagramSettings.NOTATION_LEVLE_NAME_AND_KEY;
    }
}
