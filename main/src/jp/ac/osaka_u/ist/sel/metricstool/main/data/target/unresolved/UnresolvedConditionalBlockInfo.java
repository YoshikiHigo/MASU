package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
    }

    /**
     * ��������������Ԃ�
     * @return ������������
     */
    public final UnresolvedConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    /**
     * ��������������ݒ肷��
     * @param conditionalClause ������������
     */
    public final void setConditionalClause(
            final UnresolvedConditionalClauseInfo conditionalClause) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if(null == conditionalClause) {
            throw new IllegalArgumentException("conditionalClause is null");
        }
        
        this.conditionalClause = conditionalClause;
    }

    /**
     * ��������������ۑ����邽�߂̕ϐ�
     */
    private UnresolvedConditionalClauseInfo conditionalClause;
}
