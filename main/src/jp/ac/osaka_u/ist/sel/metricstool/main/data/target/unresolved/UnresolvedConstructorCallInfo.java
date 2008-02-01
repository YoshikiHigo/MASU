package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������R���X�g���N�^�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedMemberCallInfo {

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^�Ɩ��O��^���ăI�u�W�F�N�g��������
     * 
     * @param unresolvedClassType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo unresolvedClassType) {

        super();

        if (null == unresolvedClassType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedClassType = unresolvedClassType;
        this.memberName = unresolvedClassType.getTypeName();
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // �R���X�g���N�^�̃V�O�l�`�����擾
        final String name = this.getName();
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�R���X�g���N�^�̌^������
        final TypeInfo classType = this.getClassType().resolveType(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(classType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        final ClassInfo referencedClass = ((ReferenceTypeInfo) classType).getReferencedClass();
        if (referencedClass instanceof TargetClassInfo) {

        } else if (referencedClass instanceof ExternalClassInfo) {

        }

        // TODO 
        return null;
    }

    public UnresolvedReferenceTypeInfo getClassType() {
        return this.unresolvedClassType;
    }

    private final UnresolvedReferenceTypeInfo unresolvedClassType;

}
