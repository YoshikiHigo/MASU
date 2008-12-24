package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ϐ��̎g�p�⃁�\�b�h�̌Ăяo���ȂǁC�v���O�����v�f�̎g�p��\���N���X
 * 
 * @author higo
 *
 */
public abstract class ExpressionInfo implements ConditionInfo {

    /**
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ExpressionInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.ownerExecutableElement = null;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    abstract public String getText();

    /**
     * �G���e�B�e�B�g�p�̌^��Ԃ��D
     * 
     * @return �G���e�B�e�B�g�p�̌^
     */
    public abstract TypeInfo getType();

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int compareTo(ExecutableElementInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
    }

    public final ExecutableElementInfo getOwnerExecutableElement() {

        if (null == this.ownerExecutableElement) {
            throw new IllegalStateException();
        }

        return this.ownerExecutableElement;
    }

    public final StatementInfo getOwnerStatement() {

        final ExecutableElementInfo ownerExecutableElement = this.getOwnerExecutableElement();
        if (ownerExecutableElement instanceof StatementInfo) {
            return (StatementInfo) ownerExecutableElement;
        }

        if (ownerExecutableElement instanceof ExpressionInfo) {
            return ((ExpressionInfo) ownerExecutableElement).getOwnerStatement();
        }

        // ownerExecutableElement �� StatementInfo �ł� ExpressionInfo�@�ł��Ȃ��Ƃ���IllegalStateException
        throw new IllegalStateException(
                "ownerExecutableElement must be StatementInfo or ExpressionInfo.");
    }

    /**
     * ���ڂ̃I�[�i�[�ł���ExecutableElement���Z�b�g����
     * 
     * @param ownerExecutableElement ���ڂ̃I�[�i�[�ł���ExecutableElement
     */
    public final void setOwnerExecutableElement(final ExecutableElementInfo ownerExecutableElement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerExecutableElement) {
            throw new IllegalArgumentException();
        }

        this.ownerExecutableElement = ownerExecutableElement;
    }

    private ExecutableElementInfo ownerExecutableElement;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private final int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private final int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int toColumn;

}
