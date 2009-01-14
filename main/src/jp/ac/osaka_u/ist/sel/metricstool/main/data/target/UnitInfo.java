package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �v���O�������j�b�g��\���N���X
 * 
 * @author higo
 */
public abstract class UnitInfo implements Position, Serializable {

    /**
     * �K�v�ȏ���^���ăI�u�W�F�N�g��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    UnitInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * ���̃��j�b�g���ɂ�����ϐ��g�p��Set��Ԃ�
     * 
     * @return ���̃��j�b�g���ɂ�����ϐ��g�p��Set
     */
    public abstract Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * ���̃��j�b�g���Œ�`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ���̃��j�b�g���Œ�`���ꂽ�ϐ���Set
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * ���̃��j�b�g���ɂ�����Ăяo����Set��Ԃ�
     * 
     * @return ���̃��j�b�g���ɂ�����Ăяo����Set
     */
    public abstract Set<CallInfo<? extends CallableUnitInfo>> getCalls();

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * ���̃��j�b�g�̍s����Ԃ�
     * 
     * @return�@���̃��j�b�g�̍s��
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    /**
     * ���̃��j�b�g�̃V�O�l�`����Ԃ�
     * 
     * @return ���̃��j�b�g�̃V�O�l�`��
     */
    //public abstract String getSignature();
    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public int getToColumn() {
        return this.toColumn;
    }

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
