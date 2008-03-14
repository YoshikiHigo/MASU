package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �ϐ��錾���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
public class VariableDeclarationStatementInfo extends SingleStatementInfo {

    /**
     * �錾����Ă���ϐ��C���������C�ʒu����^���ď�����
     * �錾����Ă���ϐ�������������Ă���ꍇ�C���̃R���X�g���N�^���g�p����
     * 
     * @param declaredVariable �錾����Ă��郍�[�J���ϐ�
     * @param initializationExpression ��������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public VariableDeclarationStatementInfo(final LocalVariableInfo declaredVariable,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = initializationExpression;

    }

    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.declaredLocalVarialbe;
    }

    /**
     * �錾����Ă���ϐ��̏���������Ԃ�
     * 
     * @return �錾����Ă���ϐ��̏��������D����������Ă��ꍇ��null
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ�������������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �錾����Ă���ϐ�������������Ă����true
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ���\���t�B�[���h
     */
    private final LocalVariableInfo declaredLocalVarialbe;

    /**
     * �錾����Ă���ϐ��̏���������\���t�B�[���h
     */
    private final ExpressionInfo initializationExpression;

}
