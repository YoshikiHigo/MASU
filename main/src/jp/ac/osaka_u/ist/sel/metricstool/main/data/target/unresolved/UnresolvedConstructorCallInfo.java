package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
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
     * @param unresolvedReferenceType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo unresolvedReferenceType) {

        super();

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
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

        
        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �R���X�g���N�^�̃V�O�l�`�����擾
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�R���X�g���N�^�̌^������
        final TypeInfo referenceType = this.getReferenceType().resolveType(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(referenceType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        // �N���X�^�̃R���X�g���N�^�Ăяo���̏ꍇ
        if (referenceType instanceof ClassTypeInfo) {

            final ClassInfo referencedClass = ((ClassTypeInfo) referenceType).getReferencedClass();
            if (referencedClass instanceof TargetClassInfo) {

                // �܂��͗��p�\�ȃ��\�b�h���猟��
                {
                    // ���p�\�ȃ��\�b�h�ꗗ���擾
                    final List<TargetMethodInfo> availableMethods = NameResolver
                            .getAvailableMethods((TargetClassInfo) referencedClass, usingClass);

                    // ���p�\�ȃ��\�b�h(�R���X�g���N�^)����C���������\�b�h�ƈ�v������̂�����
                    // �����̌^�̃��X�g��p���āC���̃R���X�g���N�^���\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                    for (final TargetMethodInfo availableMethod : availableMethods) {

                        // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                        if (availableMethod.canCalledWith(actualParameters)) {
                            this.resolvedInfo = new ConstructorCallInfo(availableMethod, fromLine, fromColumn, toLine, toColumn);
                            return this.resolvedInfo;
                        }
                    }
                }

                // ���p�\�ȃ��\�b�h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                // ���̃N���X�̃��\�b�h���g�p���Ă���Ƃ݂Ȃ�
                {
                    final ExternalClassInfo externalSuperClass = NameResolver
                            .getExternalSuperClass((TargetClassInfo) referencedClass);
                    if (null != externalSuperClass) {

                        final ExternalMethodInfo methodInfo = new ExternalMethodInfo(
                                externalSuperClass.getClassName(), externalSuperClass, true);
                        final List<ParameterInfo> dummyParameters = NameResolver
                                .createParameters(actualParameters);
                        methodInfo.addParameters(dummyParameters);
                        methodInfoManager.add(methodInfo);

                        // �O���N���X�ɐV�K�ŊO�����\�b�h�ϐ��iExternalMethodInfo�j��ǉ������̂Ō^�͕s��
                        this.resolvedInfo = new ConstructorCallInfo(methodInfo, fromLine, fromColumn, toLine, toColumn);
                        return this.resolvedInfo;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                // ������Ȃ������������s��
                {
                    err.println("Can't resolve method Call : " + this.toString());

                    usingMethod.addUnresolvedUsage(this);

                    this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }

            } else if (referencedClass instanceof ExternalClassInfo) {

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(referencedClass
                        .getClassName(), referencedClass, true);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = new ConstructorCallInfo(methodInfo, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            //�@�z��^�̃R���X�g���N�^�Ăяo���̏ꍇ
        } else if (referenceType instanceof ArrayTypeInfo) {
            // TODO �z��̃R���X�g���N�^��\���N���X���K�v
        }

        // TODO 
        return null;
    }

    public UnresolvedReferenceTypeInfo getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final UnresolvedReferenceTypeInfo unresolvedReferenceType;

}
