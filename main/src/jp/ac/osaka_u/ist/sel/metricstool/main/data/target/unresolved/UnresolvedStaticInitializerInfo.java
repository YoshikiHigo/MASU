package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ static initializer ��\���N���X
 * 
 * @author t-miyake, higo
 */
public class UnresolvedStaticInitializerInfo extends
        UnresolvedCallableUnitInfo<StaticInitializerInfo> {

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * ���O�������s��
     */
    @Override
    public StaticInitializerInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���L�N���X���擾
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new StaticInitializerInfo(ownerClass, this.getFromLine(), this
                .getFromColumn(), this.getToLine(), this.getToColumn());
        return this.resolvedInfo;
    }

    @Override
    public boolean isStaticMember() {
        return true;
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    /**
     * �Ȃɂ����Ȃ�
     */
    @Override
    public void setInstanceMember(boolean instance) {
    }
}
