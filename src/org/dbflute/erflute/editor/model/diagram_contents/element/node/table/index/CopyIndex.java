package org.dbflute.erflute.editor.model.diagram_contents.element.node.table.index;

import java.util.ArrayList;
import java.util.List;

import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.ERTable;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.column.CopyColumn;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.column.ERColumn;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.column.NormalColumn;

public class CopyIndex extends ERIndex {

    private static final long serialVersionUID = -7896024413398953097L;

    private ERIndex originalIndex;

    public CopyIndex(ERTable copyTable, ERIndex originalIndex, List<ERColumn> copyColumns) {
        super(copyTable, originalIndex.getName(), originalIndex.isNonUnique(), originalIndex.getType(), originalIndex.getDescription());

        this.originalIndex = originalIndex;

        List<Boolean> descs = originalIndex.getDescs();

        int i = 0;

        for (NormalColumn originalIndexColumn : originalIndex.getColumns()) {
            Boolean desc = Boolean.FALSE;

            if (descs.size() > i) {
                desc = descs.get(i);
            }

            if (copyColumns != null) {

                boolean isGroupColumn = true;

                for (ERColumn column : copyColumns) {
                    if (column instanceof CopyColumn) {
                        CopyColumn copyColumn = (CopyColumn) column;

                        if (copyColumn.getOriginalColumn().equals(originalIndexColumn)) {
                            this.addColumn(copyColumn, desc);
                            isGroupColumn = false;
                            break;
                        }
                    }
                }

                if (isGroupColumn) {
                    this.addColumn(originalIndexColumn, desc);
                }
            } else {
                this.addColumn(originalIndexColumn, desc);
            }

            i++;
        }
    }

    public ERIndex getRestructuredIndex(ERTable originalTable) {
        if (this.originalIndex == null) {
            this.originalIndex = new ERIndex(originalTable, this.getName(), this.isNonUnique(), this.getType(), this.getDescription());
        }

        copyData(this, this.originalIndex);

        List<NormalColumn> indexColumns = new ArrayList<NormalColumn>();

        for (NormalColumn column : this.originalIndex.getColumns()) {
            if (column instanceof CopyColumn) {
                CopyColumn copyColumn = (CopyColumn) column;
                column = copyColumn.getOriginalColumn();
            }
            indexColumns.add(column);
        }

        this.originalIndex.setColumns(indexColumns);
        this.originalIndex.setTable(originalTable);

        return this.originalIndex;
    }

    public static void copyData(ERIndex from, ERIndex to) {
        to.setName(from.getName());
        to.setNonUnique(from.isNonUnique());
        to.setFullText(from.isFullText());
        to.setType(from.getType());
        to.setDescription(from.getDescription());

        to.getColumns().clear();
        to.getDescs().clear();

        List<Boolean> descs = from.getDescs();
        int i = 0;

        for (NormalColumn column : from.getColumns()) {
            Boolean desc = Boolean.FALSE;

            if (descs.size() > i) {
                desc = descs.get(i);
            }
            to.addColumn(column, desc);
            i++;
        }
    }
}
