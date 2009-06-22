package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * import����\���N���X
 * 
 * @author higo
 *
 */
public class ClassImportStatementInfo extends ImportStatementInfo<ClassInfo> {

    /**
     * �I�u�W�F�N�g��������
     * @param importedClasses
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ClassImportStatementInfo(final Set<ClassInfo> importedClasses, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(importedClasses, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * �C���|�[�g���ꂽ�N���X��SortedSet��Ԃ�
     * 
     * @return�@�C���|�[�g���ꂽ�N���X��SortedSet
     */
    public Set<ClassInfo> getImportedClasses() {
        return this.getImportedUnits();
    }
}
