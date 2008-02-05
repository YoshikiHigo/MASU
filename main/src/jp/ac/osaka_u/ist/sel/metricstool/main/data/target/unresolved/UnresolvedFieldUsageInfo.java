package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayLengthUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �������t�B�[���h�g�p��ۑ����邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldUsageInfo extends UnresolvedVariableUsageInfo {

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^���ƕϐ����C���p�\�Ȗ��O��Ԃ�^���ăI�u�W�F�N�g��������
     * 
     * @param availableNamespaces ���p�\�Ȗ��O���
     * @param ownerClassType �t�B�[���h�g�p�����s�����ϐ��̌^��
     * @param fieldName �ϐ���
     * @param reference �t�B�[���h�g�p���Q�Ƃł���ꍇ�� true�C����ł���ꍇ�� false ���w��
     */
    public UnresolvedFieldUsageInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final UnresolvedEntityUsageInfo ownerClassType, final String fieldName,
            final boolean reference) {

        super(reference);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
        this.reference = reference;

        this.resolvedInfo = null;
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
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
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
            return this.getResolvedEntityUsage();
        }

        // �t�B�[���h���C�Q�ƁE������擾
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �e�̌^������
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerClassType();
        final EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // -----��������e�̌^ �ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(this);

            this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
            return this.resolvedInfo;

            //�e���N���X�^�̏ꍇ
        } else if (ownerUsage.getType() instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) ownerUsage.getType())
                    .getReferencedClass();
            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
            if (ownerClass instanceof TargetClassInfo) {

                // �܂��͗��p�\�ȃt�B�[���h���猟��
                {
                    // ���p�\�ȃt�B�[���h�ꗗ���擾
                    final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                            (TargetClassInfo) ownerUsage.getType(), usingClass);

                    // ���p�\�ȃt�B�[���h���C�������t�B�[���h���Ō���
                    for (final TargetFieldInfo availableField : availableFields) {

                        // ��v����t�B�[���h�������������ꍇ
                        if (fieldName.equals(availableField.getName())) {

                            this.resolvedInfo = new FieldUsageInfo(availableField, reference,
                                    fromLine, fromColumn, toLine, toColumn);
                            return this.resolvedInfo;
                        }
                    }
                }

                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂�
                // ���̃N���X�̕ϐ����g�p���Ă���Ƃ݂Ȃ�
                {
                    for (TargetClassInfo classInfo = (TargetClassInfo) ownerUsage.getType(); true; classInfo = ((TargetInnerClassInfo) classInfo)
                            .getOuterClass()) {

                        final ExternalClassInfo externalSuperClass = NameResolver
                                .getExternalSuperClass(classInfo);
                        if (null != externalSuperClass) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                    externalSuperClass);
                            fieldInfoManager.add(fieldInfo);

                            // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                            this.resolvedInfo = new FieldUsageInfo(fieldInfo, reference, fromLine,
                                    fromColumn, toLine, toColumn);
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

                    usingMethod.addUnresolvedUsage(this);

                    this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }

                // �e���O���N���X�iExternalClassInfo�j�������ꍇ
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName, ownerClass);
                fieldInfoManager.add(fieldInfo);

                // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                this.resolvedInfo = new FieldUsageInfo(fieldInfo, reference, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

        } else if (ownerUsage.getType() instanceof ArrayTypeInfo) {

            // TODO �����͌���ˑ��ɂ��邵���Ȃ��̂��H �z��.length �Ȃ�

            // Java ����� �t�B�[���h���� length �������ꍇ�� int �^��Ԃ�
            if (Settings.getLanguage().equals(LANGUAGE.JAVA) && fieldName.equals("length")) {
                this.resolvedInfo = new ArrayLengthUsageInfo(ownerUsage, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�Ԃ�
     * 
     * @return �g�p�\�Ȗ��O��Ԃ�Ԃ�
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����Ԃ�
     * 
     * @return �t�B�[���h�g�p�����s�����ϐ��̖������^��
     */
    public UnresolvedEntityUsageInfo getOwnerClassType() {
        return this.ownerClassType;
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
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedEntityUsageInfo ownerClassType;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;

    /**
     * �����ς݃t�B�[���h�g�p��ۑ����邽�߂̕ϐ�
     */
    private EntityUsageInfo resolvedInfo;
}
