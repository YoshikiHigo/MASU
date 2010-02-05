package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


@SuppressWarnings("serial")
public class MemberImportStatementInfo extends ImportStatementInfo<Member> {

    /**
     * �I�u�W�F�N�g��������
     * @param importedMembers
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public MemberImportStatementInfo(final Set<Member> importedMembers, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(importedMembers, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * �C���|�[�g���ꂽ�N���X��SortedSet��Ԃ�
     * 
     * @return�@�C���|�[�g���ꂽ�N���X��SortedSet
     */
    public Set<Member> getImportedMembers() {
        return this.getImportedUnits();
    }
}
