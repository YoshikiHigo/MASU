package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * import����\���N���X
 * 
 * @author higo
 *
 */
public class ImportStatementInfo extends UnitInfo {

    
    /**
     * 
     */
    private static final long serialVersionUID = -855270889117061510L;

    /**
     * �I�u�W�F�N�g��������
     * @param importedClasses
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ImportStatementInfo(final SortedSet<ClassInfo> importedClasses, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.importedClasses = new TreeSet<ClassInfo>();
        this.importedClasses.addAll(importedClasses);
    }

    /**
     * �C���|�[�g���ꂽ�N���X��SortedSet��Ԃ�
     * 
     * @return�@�C���|�[�g���ꂽ�N���X��SortedSet
     */
    public SortedSet<ClassInfo> getImportClasses() {
        return Collections.unmodifiableSortedSet(this.importedClasses);
    }

    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        // TODO Auto-generated method stub
        return null;
    }

    private final SortedSet<ClassInfo> importedClasses;
}
