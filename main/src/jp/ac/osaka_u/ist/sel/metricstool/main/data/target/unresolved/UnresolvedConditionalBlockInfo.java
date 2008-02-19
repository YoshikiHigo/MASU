package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


/**
 * �������������t���u���b�N����\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedConditionalBlockInfo<T extends ConditionalBlockInfo> extends
        UnresolvedBlockInfo<T> {

    public UnresolvedConditionalBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        super(ownerSpace);
        this.conditionalClause = new UnresolvedConditionalClauseInfo();
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
