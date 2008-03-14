package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * throw���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw���ɂ���ē��������O��\�����ƈʒu����^���ď�����
     * 
     * @param thrownEpression throw���ɂ���ē��������O��\����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ThrowStatementInfo(ExpressionInfo thrownEpression, int fromLine, int fromColumn,
            int toLine, int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        
        if(null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;
    }

    /**
     * throw���ɂ���ē��������O��\������Ԃ�
     * 
     * @return throw���ɂ���ē��������O��\����
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }
    
    /**
     * throw���ɂ���ē��������O��\����
     */
    private final ExpressionInfo thrownEpression;

}
