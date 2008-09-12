package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
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
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getConditionalExpression() {
        return this.conditionalExpression;
    }

    /**
     * ��������������ݒ肷��
     * @param conditionalExpression ������������
     */
    public final void setConditionalExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> conditionalExpression) {
        if(null == conditionalExpression) {
            throw new IllegalArgumentException("conditionalExpression is null");
        }
        MetricsToolSecurityManager.getInstance().checkAccess();
        
        this.conditionalExpression = conditionalExpression;
    }

    /**
     * ��������������ۑ����邽�߂̕ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> conditionalExpression;
}
