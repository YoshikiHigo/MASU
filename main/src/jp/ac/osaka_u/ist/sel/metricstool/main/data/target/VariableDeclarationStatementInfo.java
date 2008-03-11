package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public class VariableDeclarationStatementInfo implements StatementInfo {

    /**
     * �錾����Ă���ϐ��C�ʒu����^���ď�����
     * �錾����Ă���ϐ�������������Ă��Ȃ��ꍇ�C���̃R���X�g���N�^���g�p����
     * 
     * @param declaredVariable �錾����Ă��郍�[�J���ϐ�
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public VariableDeclarationStatementInfo(final LocalVariableInfo declaredVariable,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = null;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

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
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = initializationExpression;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;

    }

    @Override
    public final int compareTo(StatementInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
    }

    @Override
    public final int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    @Override
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int getToLine() {
        return this.toLine;
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

    /**
     * �J�n�s��\���ϐ�
     */
    private final int fromLine;

    /**
     * �J�n���\���ϐ�
     */
    private final int fromColumn;

    /**
     * �I���s��\���ϐ�
     */
    private final int toLine;

    /**
     * �I�����\���ϐ�
     */
    private final int toColumn;
}
