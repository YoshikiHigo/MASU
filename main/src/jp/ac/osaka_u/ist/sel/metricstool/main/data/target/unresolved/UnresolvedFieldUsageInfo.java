package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayLengthUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �������t�B�[���h�g�p��ۑ����邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldUsageInfo extends UnresolvedVariableUsageInfo<FieldUsageInfo> {

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^���ƕϐ����C���p�\�Ȗ��O��Ԃ�^���ăI�u�W�F�N�g��������
     * 
     * @param availableNamespaces ���p�\�Ȗ��O���
     * @param qualifierUsage �t�B�[���h�g�p�����s�����e�G���e�B�e�B
     * @param fieldName �ϐ���
     * @param reference �t�B�[���h�g�p���Q�Ƃ�
     * @param assignment �t�B�[���h�g�p�������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedFieldUsageInfo(final List<UnresolvedImportStatementInfo> availableNamespaces,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage,
            final String fieldName, final boolean reference, final boolean assignment,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fieldName, reference, assignment, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == qualifierUsage) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.qualifierUsage = qualifierUsage;
        this.fieldName = fieldName;
    }

    @Override
    public void setOwnerExecutableElementInfo(
            UnresolvedExecutableElementInfo<? extends ExecutableElementInfo> ownerExecutableElement) {
        super.setOwnerExecutableElementInfo(ownerExecutableElement);

        this.qualifierUsage.setOwnerExecutableElementInfo(ownerExecutableElement);
    }

    /**
     * �������t�B�[���h�g�p���������C���̌^��Ԃ��D
     * 
     * @param usingClass �������t�B�[���h�g�p���s���Ă���N���X
     * @param usingMethod �������t�B�[���h�g�p���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃t�B�[���h�g�p
     */
    @Override
    public FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �t�B�[���h���C�Q�ƁE������擾
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();
        final boolean assingment = this.isAssignment();

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �t�B�[���h�g�p���������Ă���^("."�̑O�̂��)������
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifierUsage();
        final ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "resolveEntityUsage returned null!";

        // -----��������e�̌^�ɉ����ď����𕪊�
        TypeInfo ownerType = qualifierUsage.getType();

        // �^�p�����[�^�̏ꍇ�͂��̌p���^�����߂�
        if (ownerType instanceof TypeParameterInfo) {
            final TypeInfo extendsType = ((TypeParameterInfo) ownerType).getExtendsType();
            if (null != extendsType) {
                ownerType = extendsType;
            } else {
                assert false : "Here should not be reached";

                final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage, UnknownTypeInfo
                        .getInstance(), unknownField, reference, assingment, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                return this.resolvedInfo;
            }
        }

        // -----��������e�̌^ �ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (ownerType instanceof UnknownTypeInfo) {

            final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);

            this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage, UnknownTypeInfo
                    .getInstance(), unknownField, reference, assingment, usingMethod, fromLine,
                    fromColumn, toLine, toColumn);
            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
            return this.resolvedInfo;

            //�e���N���X�^�̏ꍇ
        } else if (ownerType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) qualifierUsage.getType())
                    .getReferencedClass();
            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
            if (ownerClass instanceof TargetClassInfo) {

                // �܂��͗��p�\�ȃt�B�[���h���猟��
                {
                    // ���p�\�ȃt�B�[���h�ꗗ���擾
                    final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                            (TargetClassInfo) ownerClass, usingClass);

                    // ���p�\�ȃt�B�[���h���C�������t�B�[���h���Ō���
                    for (final TargetFieldInfo availableField : availableFields) {

                        // ��v����t�B�[���h�������������ꍇ
                        if (fieldName.equals(availableField.getName())) {

                            this.resolvedInfo = FieldUsageInfo
                                    .getInstance(qualifierUsage, qualifierUsage.getType(),
                                            availableField, reference, assingment, usingMethod,
                                            fromLine, fromColumn, toLine, toColumn);
                            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                            return this.resolvedInfo;
                        }
                    }
                }

                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂�
                // ���̃N���X�̕ϐ����g�p���Ă���Ƃ݂Ȃ�
                {
                    for (TargetClassInfo classInfo = (TargetClassInfo) ownerClass; true; classInfo = ((TargetInnerClassInfo) classInfo)
                            .getOuterClass()) {

                        final ExternalClassInfo externalSuperClass = NameResolver
                                .getExternalSuperClass(classInfo);
                        if (null != externalSuperClass) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                    externalSuperClass);
                            fieldInfoManager.add(fieldInfo);

                            // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                            this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage,
                                    qualifierUsage.getType(), fieldInfo, reference, assingment,
                                    usingMethod, fromLine, fromColumn, toLine, toColumn);
                            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                            return this.resolvedInfo;
                        }

                        if (!(classInfo instanceof TargetInnerClassInfo)) {
                            break;
                        }
                    }
                }

                // ������Ȃ������������s��
                {
                    assert false : "Can't resolve field reference : " + this.getFieldName();

                    final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                    this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage, UnknownTypeInfo
                            .getInstance(), unknownField, reference, assingment, usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                    return this.resolvedInfo;
                }

                // �e���O���N���X�iExternalClassInfo�j�������ꍇ
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName, ownerClass);
                fieldInfoManager.add(fieldInfo);

                // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage, qualifierUsage
                        .getType(), fieldInfo, reference, assingment, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                return this.resolvedInfo;
            }

        } else if (ownerType instanceof ArrayTypeInfo) {

            // TODO �����͌���ˑ��ɂ��邵���Ȃ��̂��H �z��.length �Ȃ�

            // Java ����� �t�B�[���h���� length �������ꍇ�� int �^��Ԃ�
            // TODO�@�����Ƃ����Ȃ����Ȃ��Ƃ����Ȃ�
            final Settings settings = Settings.getInstance();
            if ((settings.getLanguage().equals(LANGUAGE.JAVA15)
                    || settings.getLanguage().equals(LANGUAGE.JAVA14) || settings.getLanguage()
                    .equals(LANGUAGE.JAVA13))
                    && fieldName.equals("length")) {

                this.resolvedInfo = new ArrayLengthUsageInfo(qualifierUsage,
                        (ArrayTypeInfo) ownerType, usingMethod, fromLine, fromColumn, toLine,
                        toColumn);
                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);

        this.resolvedInfo = FieldUsageInfo.getInstance(qualifierUsage, UnknownTypeInfo
                .getInstance(), unknownField, reference, assingment, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�Ԃ�
     * 
     * @return �g�p�\�Ȗ��O��Ԃ�Ԃ�
     */
    public List<UnresolvedImportStatementInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����Ԃ�
     * 
     * @return �t�B�[���h�g�p�����s�����ϐ��̖������^��
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * �t�B�[���h����Ԃ�
     * 
     * @return �t�B�[���h��
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedImportStatementInfo> availableNamespaces;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage;
}
