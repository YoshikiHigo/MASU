package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������S���薼�N���X�Q�Ƃ�\���N���X
 * 
 * @author higo, t-miyake
 *
 */
public final class UnresolvedFullQualifiedNameClassReferenceInfo extends
        UnresolvedClassReferenceInfo {

    /**
     * ���S���薼���킩���Ă���iUnresolvedClassInfo�̃I�u�W�F�N�g�����݂���j�N���X�̎Q�Ƃ�������
     * 
     * @param referencedClass �Q�Ƃ���Ă���N���X
     */
    public UnresolvedFullQualifiedNameClassReferenceInfo(final UnresolvedClassInfo referencedClass) {
        super(new HashSet<AvailableNamespaceInfo>(), referencedClass.getFullQualifiedName());
        this.referencedClass = referencedClass;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
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

        final String[] fullQualifiedReferenceName = this.getReferenceName();
        ClassInfo referencedClass = classInfoManager.getClassInfo(fullQualifiedReferenceName);

        // �Q�Ƃ��ꂽ�N���X���o�^����Ă��Ȃ��ꍇ�́C�����œo�^����
        if (null == referencedClass) {
            referencedClass = new ExternalClassInfo(fullQualifiedReferenceName);
            classInfoManager.add((ExternalClassInfo) referencedClass);
        }

        final ClassTypeInfo referenceType = new ClassTypeInfo(referencedClass);
        for (final UnresolvedTypeInfo unresolvedTypeArgument : this.getTypeArguments()) {
            final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            referenceType.addTypeArgument(typeArgument);
        }
        this.resolvedInfo = new ClassReferenceInfo(referenceType, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * �Q�Ƃ���Ă���N���X�̏���Ԃ�
     * 
     * @return �Q�Ƃ���Ă���N���X�̏��
     */
    public UnresolvedClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * �Q�Ƃ���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedClassInfo referencedClass;

}
