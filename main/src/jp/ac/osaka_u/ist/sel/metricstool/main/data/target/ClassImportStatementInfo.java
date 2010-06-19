package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


/**
 * import����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
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

        // �C���|�[�g����Ă���N���X��External�N���X�ł���ꍇ�͂���ɑΉ�����^�[�Q�b�g�N���X�����邩�𒲂ׂ�
        final Set<ClassInfo> refreshedImportedClasses = new HashSet<ClassInfo>();
        for (final ClassInfo importedClass : this.getImportedUnits()) {
            if (importedClass instanceof ExternalClassInfo) {
                final String[] importedFQName = importedClass.getFullQualifiedName();
                final ClassInfo refreshedImportedClass = DataManager.getInstance()
                        .getClassInfoManager().getClassInfo(importedFQName);
                refreshedImportedClasses.add(refreshedImportedClass);
            } else {
                refreshedImportedClasses.add(importedClass);
            }
        }
        return refreshedImportedClasses;
    }
}
