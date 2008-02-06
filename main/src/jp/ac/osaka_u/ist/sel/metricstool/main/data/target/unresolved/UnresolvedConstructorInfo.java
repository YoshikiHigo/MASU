package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedConstructorInfo extends
        UnresolvedCallableUnitInfo<TargetConstructorInfo> {

    public UnresolvedConstructorInfo(final UnresolvedClassInfo ownerClass) {

        super(ownerClass);
    }

    @Override
    public boolean alreadyResolved() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TargetConstructorInfo getResolvedUnit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TargetConstructorInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // �C���q�C���O�C�Ԃ�l�C�s���C�������擾
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();

        final int constructorFromLine = this.getFromLine();
        final int constructorFromColumn = this.getFromColumn();
        final int constructorToLine = this.getToLine();
        final int constructorToColumn = this.getToColumn();

        // MethodInfo �I�u�W�F�N�g�𐶐�����D
        this.resolvedInfo = new TargetConstructorInfo(methodModifiers, usingClass, privateVisible,
                namespaceVisible, inheritanceVisible, publicVisible, constructorFromLine,
                constructorFromColumn, constructorToLine, constructorToColumn);

        // �^�p�����[�^���������C�����ς݃R���X�g���N�^���ɒǉ�����
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = (TypeParameterInfo) unresolvedTypeParameter
                    .resolveType(usingClass, this.resolvedInfo, classInfoManager, null, null);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        // �������������C�����ς݃R���X�g���N�^���ɒǉ�����
        for (final UnresolvedParameterInfo unresolvedParameterInfo : this.getParameters()) {

            final TargetParameterInfo parameterInfo = unresolvedParameterInfo.resolveUnit(
                    usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addParameter(parameterInfo);
        }

        //�@�����u���b�N�����������C�����ς�case�G���g���I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedBlockInfo<?> unresolvedInnerBlock : this.getInnerBlocks()) {
            final BlockInfo innerBlock = unresolvedInnerBlock.resolveUnit(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInnerBlock(innerBlock);
        }

        // ���\�b�h���Œ�`����Ă���e���������[�J���ϐ��ɑ΂���
        for (final UnresolvedLocalVariableInfo unresolvedLocalVariable : this.getLocalVariables()) {

            final LocalVariableInfo localVariable = unresolvedLocalVariable.resolveUnit(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(localVariable);
        }

        return this.resolvedInfo;
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    public boolean isStaticMember() {
        return false;
    }

    @Override
    public void setInstanceMember(boolean instance) {

    }

    /**
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    private TargetConstructorInfo resolvedInfo;
}