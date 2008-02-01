package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * ���������\�b�h�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodCallInfo extends UnresolvedMemberCallInfo {

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^�C���\�b�h����^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUsage ���\�b�h�Ăяo�������s�����ϐ��̌^
     * @param methodName ���\�b�h��
     */
    public UnresolvedMethodCallInfo(final UnresolvedEntityUsageInfo ownerUsage,
            final String methodName) {

        super();
        
        if ((null == ownerUsage) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerUsage = ownerUsage;
        this.memberName = methodName;
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

        // ���\�b�h�̃V�O�l�`�����擾
        final String name = this.getName();
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // �e�̌^������
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerClassType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";
        if (ownerUsage instanceof UnknownEntityUsageInfo) {
            if (unresolvedOwnerUsage instanceof UnresolvedClassReferenceInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final ExternalClassInfo externalClassInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedOwnerUsage);
                classInfoManager.add(externalClassInfo);
                final ReferenceTypeInfo referenceType = new ReferenceTypeInfo(externalClassInfo);
                ownerUsage = new ClassReferenceInfo(referenceType);
            }
        }

        // -----��������e�̌^�ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(this);

            this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            return this.resolvedInfo;

            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
        } else if (ownerUsage.getType() instanceof TargetClassInfo) {

            // �܂��͗��p�\�ȃ��\�b�h���猟��
            {
                // ���p�\�ȃ��\�b�h�ꗗ���擾
                final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        (TargetClassInfo) ownerUsage.getType(), usingClass);

                // ���p�\�ȃ��\�b�h����C���������\�b�h�ƈ�v������̂�����
                // ���\�b�h���C�����̌^�̃��X�g��p���āC���̃��\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                for (final TargetMethodInfo availableMethod : availableMethods) {

                    // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                    if (availableMethod.canCalledWith(name, actualParameters)) {
                        this.resolvedInfo = new MethodCallInfo(availableMethod);
                        return this.resolvedInfo;
                    }
                }
            }

            // ���p�\�ȃ��\�b�h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
            // ���̃N���X�̃��\�b�h���g�p���Ă���Ƃ݂Ȃ�
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) ownerUsage.getType());
                if (null != externalSuperClass) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(memberName,
                            externalSuperClass, false);
                    final List<ParameterInfo> dummyParameters = NameResolver
                            .createParameters(actualParameters);
                    methodInfo.addParameters(dummyParameters);
                    methodInfoManager.add(methodInfo);

                    // �O���N���X�ɐV�K�ŊO�����\�b�h�ϐ��iExternalMethodInfo�j��ǉ������̂Ō^�͕s��
                    this.resolvedInfo = new MethodCallInfo(methodInfo);
                    return this.resolvedInfo;
                }

                assert false : "Here shouldn't be reached!";
            }

            // ������Ȃ������������s��
            {
                err.println("Can't resolve method Call : " + this.getName());

                usingMethod.addUnresolvedUsage(this);

                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }

            // �e���O���N���X�iExternalClassInfo�j�������ꍇ
        } else if (ownerUsage.getType() instanceof ExternalClassInfo) {

            final ExternalMethodInfo methodInfo = new ExternalMethodInfo(memberName,
                    (ExternalClassInfo) ownerUsage.getType(), false);
            final List<ParameterInfo> parameters = NameResolver.createParameters(actualParameters);
            methodInfo.addParameters(parameters);
            methodInfoManager.add(methodInfo);

            // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
            this.resolvedInfo = new MethodCallInfo(methodInfo);
            return this.resolvedInfo;

            // �e���z�񂾂����ꍇ
        } else if (ownerUsage.getType() instanceof ArrayTypeInfo) {

            // XXX Java����ł���΁C java.lang.Object �ɑ΂���Ăяo��
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(memberName,
                        ownerClass, false);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h��ǉ������̂Ō^�͕s��
                this.resolvedInfo = new MethodCallInfo(methodInfo);
                return this.resolvedInfo;
            }

            // �e���v���~�e�B�u�^�������ꍇ
        } else if (ownerUsage.getType() instanceof PrimitiveTypeInfo) {

            switch (Settings.getLanguage()) {
            // Java �̏ꍇ�̓I�[�g�{�N�V���O�ł̃��\�b�h�Ăяo�����\
            // TODO �����I�ɂ͂��� switch���͂Ƃ�D�Ȃ��Ȃ� TypeConverter.getTypeConverter(LANGUAGE)�����邩��D
            case JAVA:
                final ExternalClassInfo wrapperClass = TypeConverter.getTypeConverter(
                        Settings.getLanguage()).getWrapperClass(
                        (PrimitiveTypeInfo) ownerUsage.getType());
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(memberName,
                        wrapperClass, false);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = new MethodCallInfo(methodInfo);
                return this.resolvedInfo;

            default:
                assert false : "Here shouldn't be reached!";
                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
        return this.resolvedInfo;
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^��Ԃ�
     * 
     * @return ���\�b�h�Ăяo�������s�����ϐ��̌^
     */
    public UnresolvedEntityUsageInfo getOwnerClassType() {
        return this.ownerUsage;
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̎Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedEntityUsageInfo ownerUsage;

}
