package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���ϐ���\�����߂̃N���X�D �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�������^��</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedLocalVariableInfo extends UnresolvedVariableInfo<LocalVariableInfo> {

    /**
     * ���[�J���ϐ��u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �������^��
     */
    public UnresolvedLocalVariableInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }

    /**
     * ���������[�J���ϐ������������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass ���������[�J���ϐ��̒�`���s���Ă���N���X
     * @param usingMethod ���������[�J���ϐ��̒�`���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃��[�J���ϐ����
     */
    @Override
    public LocalVariableInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // �C���q�C�ϐ����C�^���擾
        final Set<ModifierInfo> localModifiers = this.getModifiers();
        final String variableName = this.getName();
        final UnresolvedTypeInfo unresolvedVariableType = this.getType();
        TypeInfo variableType = unresolvedVariableType.resolveType(usingClass, usingMethod,
                classInfoManager, null, null);
        assert variableType != null : "resolveTypeInfo returned null!";
        if (variableType instanceof UnknownTypeInfo) {
            if (unresolvedVariableType instanceof UnresolvedClassReferenceInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final ExternalClassInfo externalClass = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedVariableType);
                variableType = new ClassTypeInfo(externalClass);
                classInfoManager.add(externalClass);

            } else if (unresolvedVariableType instanceof UnresolvedArrayTypeInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                variableType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method local variable type : "
                        + unresolvedVariableType.toString();
            }
        }
        final int localFromLine = this.getFromLine();
        final int localFromColumn = this.getFromColumn();
        final int localToLine = this.getToLine();
        final int localToColumn = this.getToColumn();

        // ���[�J���ϐ��I�u�W�F�N�g�𐶐����CMethodInfo�ɒǉ�
        this.resolvedInfo = new LocalVariableInfo(localModifiers, variableName, variableType,
                localFromLine, localFromColumn, localToLine, localToColumn);
        return this.resolvedInfo;
    }

}
