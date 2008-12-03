package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;




/**
 * if�@�� while �ȂǁC�����߂��������u���b�N����\���N���X
 * 
 * @author higo
 *
 */
public abstract class ConditionalBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param ownerClass ���̃u���b�N�����L����N���X
     * @param ownerMethod ���̃u���b�N�����L���郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ConditionalBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);


    }

    /**
     * ���̏����t�u���b�N�̏�������Ԃ�
     * 
     * @return�@���̏����t�u���b�N�̏�����
     */
    public final ConditionInfo getCondition() {
        return this.condition;
    }

    private ConditionInfo condition;
}
