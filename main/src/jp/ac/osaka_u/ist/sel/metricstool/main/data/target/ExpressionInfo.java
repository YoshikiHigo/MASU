package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

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
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ExpressionInfo(final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.ownerExecutableElement = null;
        this.ownerMethod = ownerMethod;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    abstract public String getText();

    /**
     * �����Œ�`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return �����Œ�`���ꂽ�ϐ���Set 
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

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

    /**
     * �O����ExecutableElement��Ԃ�
     * 
     * @return �O����ExecutableElement
     */
    public final ExecutableElementInfo getOwnerExecutableElement() {

        if (null == this.ownerExecutableElement) {
            throw new IllegalStateException();
        }

        return this.ownerExecutableElement;
    }

    /**
     * ���̎���������Ԃ�
     * 
     * @return ���̎�������
     */
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
        //return null;
    }

    /**
     * ���ڂ̃I�[�i�[�ł���ExecutableElement���Z�b�g����
     * 
     * @param ownerExecutableElement ���ڂ̃I�[�i�[�ł���ExecutableElement
     */
    public void setOwnerExecutableElement(final ExecutableElementInfo ownerExecutableElement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerExecutableElement) {
            throw new IllegalArgumentException();
        }

        this.ownerExecutableElement = ownerExecutableElement;
    }

    /**
     * �I�[�i�[���\�b�h��Ԃ�
     * 
     * @return �I�[�i�[���\�b�h
     */
    public final CallableUnitInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    private ExecutableElementInfo ownerExecutableElement;

    /**
     * �I�[�i�[���\�b�h��ۑ����邽�߂̕ϐ�
     */
    private final CallableUnitInfo ownerMethod;

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
