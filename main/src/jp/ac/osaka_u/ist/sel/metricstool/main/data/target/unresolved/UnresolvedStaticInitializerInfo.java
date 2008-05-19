package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
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
        UnresolvedLocalSpaceInfo<StaticInitializerInfo> {

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super();

        if (null == ownerClass) {
            throw new IllegalArgumentException("ownerClass is null");
        }

        this.ownerClass = ownerClass;
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

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ���L�N���X���擾
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final ClassInfo ownerClass = unresolvedOwnerClass.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new StaticInitializerInfo(ownerClass, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���̃X�^�e�B�b�N�C�j�V�����C�U�[���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃X�^�e�B�b�N�C�j�V�����C�U�[���`���Ă���N���X
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    private final UnresolvedClassInfo ownerClass;

}
