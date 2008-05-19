package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


/**
 * �������������t���u���b�N����\���N���X
 * 
 * @author t-miyake, higo
 * @param <T> �����ς݃u���b�N�̌^
 *
 */
public abstract class UnresolvedConditionalBlockInfo<T extends ConditionalBlockInfo> extends
        UnresolvedBlockInfo<T> {

    /**
     * �O���̃u���b�N����^���āC�I�u�W�F�N�g��������
     * 
     * @param outerSpace �O���̃u���b�N���
     */
    public UnresolvedConditionalBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
        this.conditionalClause = new UnresolvedConditionalClauseInfo(this);
    }

    /**
     * ��������������Ԃ�
     * @return ������������
     */
    public UnresolvedConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    /**
     * ��������������ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedConditionalClauseInfo conditionalClause;
}
