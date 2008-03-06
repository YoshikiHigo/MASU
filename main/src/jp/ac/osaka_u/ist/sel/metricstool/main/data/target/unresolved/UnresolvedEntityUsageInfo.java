package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;


/**
 * �������̃N���X�Q�ƁC���\�b�h�Ăяo���C�t�B�[���h�g�p�Ȃǂ������N���X�̋��ʂ̊��N���X
 * 
 * @author higo
 */
public abstract class UnresolvedEntityUsageInfo<T extends EntityUsageInfo> implements
        Resolvable<T>, PositionSetting {

    protected UnresolvedEntityUsageInfo() {
        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;
        
        this.resolvedInfo = null;
    }

    /**
     * ���ɉ����ς݂��ǂ�����Ԃ��D
     * 
     * @return �����ς݂ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃N���X�Q�Ƃ�Ԃ�
     * 
     * @return �����ς݃N���X�Q��
     * @throws NotResolvedException ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    @Override
    public final T getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public final void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public final void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public final void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public final void setToColumn(final int toColumn) {
        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

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

    /**
     * �����ςݏ���ۑ����邽�߂̕ϐ�
     */
    protected T resolvedInfo;
    
    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;
}
