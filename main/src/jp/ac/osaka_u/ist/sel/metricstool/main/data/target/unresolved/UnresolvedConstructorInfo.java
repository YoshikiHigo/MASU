package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������R���X�g���N�^��\���N���X
 * 
 * @author higo
 *
 */
public final class UnresolvedConstructorInfo extends
        UnresolvedCallableUnitInfo<TargetConstructorInfo> {

    /**
     * �K�v�ȏ���^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedConstructorInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ���O�������s��
     */
    @Override
    public TargetConstructorInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
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

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new TargetConstructorInfo(methodModifiers, ownerClass, privateVisible,
                namespaceVisible, inheritanceVisible, publicVisible, constructorFromLine,
                constructorFromColumn, constructorToLine, constructorToColumn);
        this.resolvedInfo.setOuterUnit(ownerClass);

        // �^�p�����[�^���������C�����ς݃R���X�g���N�^���ɒǉ�����
        // �����ł�extends�߂͉������Ȃ�
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(ownerClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        return this.resolvedInfo;
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�Ȃ̂� true ��Ԃ�
     */
    @Override
    public boolean isInstanceMember() {
        return true;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�ł͂Ȃ��̂� false ��Ԃ�
     */
    @Override
    public boolean isStaticMember() {
        return false;
    }

    /**
     * �Ȃɂ����Ȃ�
     */
    @Override
    public void setInstanceMember(boolean instance) {
    }

}
