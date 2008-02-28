package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
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
public final class UnresolvedMethodCallInfo extends UnresolvedCallInfo {

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^�C���\�b�h����^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUsage ���\�b�h�Ăяo�������s�����ϐ��̌^
     * @param methodName ���\�b�h��
     */
    public UnresolvedMethodCallInfo(final UnresolvedEntityUsageInfo ownerUsage,
            final String methodName) {

        if ((null == ownerUsage) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerUsage = ownerUsage;
        this.methodName = methodName;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
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
            return this.getResolvedEntityUsage();
        }

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

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

                final ExternalClassInfo externalClassInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedOwnerUsage);
                classInfoManager.add(externalClassInfo);
                final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                for (final UnresolvedTypeInfo unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedOwnerUsage)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    referenceType.addTypeArgument(typeArgument);
                }
                ownerUsage = new ClassReferenceInfo(referenceType, fromLine, fromColumn, toLine,
                        toColumn);
            }
        }

        // -----��������e�̌^�ɉ����ď����𕪊�
        TypeInfo ownerType = ownerUsage.getType();

        // �^�p�����[�^�̏ꍇ�͂��̌p���^�����߂�
        if (ownerType instanceof TypeParameterInfo) {
            final TypeInfo extendsType = ((TypeParameterInfo) ownerType).getExtendsType();
            if (null != extendsType) {
                ownerType = extendsType;
            } else {
                assert false : "Here should not be reached";
                final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                        fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }
        }

        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (ownerType instanceof UnknownTypeInfo) {

            final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
            this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine, fromColumn,
                    toLine, toColumn);
            return this.resolvedInfo;

            // �e���N���X�^�������ꍇ
        } else if (ownerType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) ownerType).getReferencedClass();
            if (ownerClass instanceof TargetClassInfo) {

                // �܂��͗��p�\�ȃ��\�b�h���猟��
                {
                    // ���p�\�ȃ��\�b�h�ꗗ���擾
                    final List<TargetMethodInfo> availableMethods = NameResolver
                            .getAvailableMethods((TargetClassInfo) ownerClass, usingClass);

                    // ���p�\�ȃ��\�b�h����C���������\�b�h�ƈ�v������̂�����
                    // ���\�b�h���C�����̌^�̃��X�g��p���āC���̃��\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                    for (final TargetMethodInfo availableMethod : availableMethods) {

                        // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                        if (availableMethod.canCalledWith(name, actualParameters)) {
                            this.resolvedInfo = new MethodCallInfo(ownerType, availableMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                            return this.resolvedInfo;
                        }
                    }
                }

                // ���p�\�ȃ��\�b�h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                // ���̃N���X�̃��\�b�h���g�p���Ă���Ƃ݂Ȃ�
                {
                    final ExternalClassInfo externalSuperClass = NameResolver
                            .getExternalSuperClass((TargetClassInfo) ownerClass);
                    if (null != externalSuperClass) {

                        final ExternalMethodInfo methodInfo = new ExternalMethodInfo(
                                this.getName(), externalSuperClass);
                        final List<ParameterInfo> dummyParameters = NameResolver
                                .createParameters(actualParameters, methodInfo);
                        methodInfo.addParameters(dummyParameters);
                        methodInfoManager.add(methodInfo);

                        // �O���N���X�ɐV�K�ŊO�����\�b�h�ϐ��iExternalMethodInfo�j��ǉ������̂Ō^�͕s��
                        this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine,
                                fromColumn, toLine, toColumn);
                        ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                        return this.resolvedInfo;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                // ������Ȃ������������s��
                {
                    err.println("Can't resolve method Call : " + this.getName());

                    final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                    this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                            fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }

                // �e���O���N���X�iExternalClassInfo�j�������ꍇ
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        ownerClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;
            }

            // �e���z�񂾂����ꍇ
        } else if (ownerType instanceof ArrayTypeInfo) {

            // XXX Java����ł���΁C java.lang.Object �ɑ΂���Ăяo��
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        ownerClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h��ǉ������̂Ō^�͕s��
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;
            }

            // �e���v���~�e�B�u�^�������ꍇ
        } else if (ownerType instanceof PrimitiveTypeInfo) {

            switch (Settings.getLanguage()) {
            // Java �̏ꍇ�̓I�[�g�{�N�V���O�ł̃��\�b�h�Ăяo�����\
            // TODO �����I�ɂ͂��� switch���͂Ƃ�D�Ȃ��Ȃ� TypeConverter.getTypeConverter(LANGUAGE)�����邩��D
            case JAVA:
                final ExternalClassInfo wrapperClass = TypeConverter.getTypeConverter(
                        Settings.getLanguage()).getWrapperClass((PrimitiveTypeInfo) ownerType);
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        wrapperClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;

            default:
                assert false : "Here shouldn't be reached!";
                final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                        fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
        this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine, fromColumn,
                toLine, toColumn);
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
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public final String getName() {
        return this.methodName;
    }

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    protected String methodName;

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̎Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedEntityUsageInfo ownerUsage;

}
