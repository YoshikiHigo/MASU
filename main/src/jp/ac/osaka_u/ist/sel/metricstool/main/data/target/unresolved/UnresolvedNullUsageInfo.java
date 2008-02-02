package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������null�g�p��\�����߂̃N���X�D
 * UnresolvedEntityUsageInfo��EntityUsageInfo���N���X�ł��邽�߁C
 * NullUsageInfo�ł���痼�����p�����邱�Ƃ��ł��Ȃ��D
 * ���̂��߂̑Ë��ĂƂ��č쐬�����N���X�D
 * 
 * @author higo
 *
 */
public final class UnresolvedNullUsageInfo implements UnresolvedEntityUsageInfo {

    public UnresolvedNullUsageInfo() {
        this.resolvedInfo = null;
    }

    /**
     * ���O��������Ă��邩�ǂ�����Ԃ��D
     * 
     * @return ���O��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���O�������ꂽ�g�p����Ԃ�
     * 
     * @return ���O�������ꂽ�g�p���
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * �g�p���̖��O��������
     * 
     * @return �����ς݂̎g�p���
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        this.resolvedInfo = new NullUsageInfo();
        return this.resolvedInfo;
    }

    private NullUsageInfo resolvedInfo;
}
