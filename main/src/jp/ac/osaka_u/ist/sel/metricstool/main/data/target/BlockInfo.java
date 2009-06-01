package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N�� for �u���b�N�Ȃ� ���\�b�h���̍\���I�Ȃ܂Ƃ܂�̒P�ʂ�\�����ۃN���X
 * 
 * @author higo
 */
public abstract class BlockInfo extends LocalSpaceInfo implements StatementInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param ownerClass ���̃u���b�N�����L����N���X
     * @param ownerSpace ���̃u���b�N�����L����u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    BlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == outerSpace)) {
            throw new IllegalArgumentException();
        }

        this.outerSpace = outerSpace;
    }

    /**
     * ���̃u���b�N�I�u�W�F�N�g�𑼂̃u���b�N�I�u�W�F�N�g�Ɣ�r����
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof BlockInfo)) {
            return false;
        }

        return 0 == this.compareTo((BlockInfo) o);
    }

    /**
     * ���̃u���b�N�����L�����Ԃ�
     * 
     * @return ���̃u���b�N�����L���郁�\�b�h
     */
    @Override
    public final CallableUnitInfo getOwnerMethod() {

        final LocalSpaceInfo outerSpace = this.getOwnerSpace();
        if (outerSpace instanceof CallableUnitInfo) {
            return (CallableUnitInfo) outerSpace;
        }

        if (outerSpace instanceof BlockInfo) {
            return ((BlockInfo) outerSpace).getOwnerMethod();
        }

        throw new IllegalStateException();
    }

    /**
     * ���̃u���b�N���J��Ԃ����ł��邩�ǂ����Ԃ�
     * @return �J��Ԃ����ł���Ȃ�true
     */
    public boolean isLoopStatement() {
        return false;
    }

    /**
     * ���̃u���b�N�𒼐ڏ��L���郍�[�J����Ԃ�Ԃ�
     * 
     * @return ���̃u���b�N�𒼐ڏ��L���郍�[�J�����
     */
    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.outerSpace;
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExpressions = new HashSet<ClassTypeInfo>();
        for (final StatementInfo innerStatement : this.getStatements()) {
            thrownExpressions.addAll(innerStatement.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    /**
     * ���̃u���b�N�𒼐ڏ��L���郍�[�J����Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final LocalSpaceInfo outerSpace;
}
