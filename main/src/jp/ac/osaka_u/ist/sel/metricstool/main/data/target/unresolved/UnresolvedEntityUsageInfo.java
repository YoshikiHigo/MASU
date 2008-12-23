package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������̃N���X�Q�ƁC���\�b�h�Ăяo���C�t�B�[���h�g�p�Ȃǂ������N���X�̋��ʂ̊��N���X
 * 
 * @author higo
 * @param <T> �����ς݂̌^
 */
public abstract class UnresolvedEntityUsageInfo<T extends EntityUsageInfo> implements
        UnresolvedExpressionInfo<T> {

    protected UnresolvedEntityUsageInfo() {
        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;

        this.resolvedInfo = null;
        this.ownerExecutableElement = null;
    }

    @Override
    public final int compareTo(UnresolvedExecutableElementInfo<?> o) {

        if (null == o) {
            throw new IllegalArgumentException();
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
     * �I�[�i�[�G�������g���Z�b�g����
     * 
     * @param ownerExecutableElement �I�[�i�[�G�������g
     */
    public final void setOwnerExecutableElementInfo(
            final UnresolvedExecutableElementInfo<T> ownerExecutableElement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerExecutableElement) {
            throw new IllegalArgumentException();
        }

        this.ownerExecutableElement = ownerExecutableElement;
    }

    /**
     * �I�[�i�[�G�������g��Ԃ�
     * 
     * @return�@�I�[�i�[�G�������g
     */
    public final UnresolvedExecutableElementInfo<T> getOwnerExecutableElement() {

        if (null == this.ownerExecutableElement) {
            throw new NullPointerException();
        }

        return this.ownerExecutableElement;
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

    private UnresolvedExecutableElementInfo<T> ownerExecutableElement;

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
