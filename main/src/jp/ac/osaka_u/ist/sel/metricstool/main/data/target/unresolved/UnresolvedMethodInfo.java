package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ��x�ڂ�AST�p�[�X�Ŏ擾�������\�b�h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodInfo extends UnresolvedCallableUnitInfo<TargetMethodInfo>
        implements StaticOrInstanceSetting {

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     * @param ownerClass ���̃��\�b�h��錾���Ă���N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedMethodInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.methodName = null;
        this.returnType = null;
        this.instance = true;
        this.resolvedInfo = null;
    }

    /**
     * ���������\�b�h�����������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass ���������\�b�h���̒�`������N���X
     * @param usingMethod ���������\�b�h���̒�`�����郁�\�b�h�i���̃��\�b�h���Ă΂��ꍇ�͒ʏ� null ���Z�b�g����Ă���͂��j
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃��\�b�h���
     */
    @Override
    public TargetMethodInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �C���q�C���O�C�s���C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final String methodName = this.getMethodName();

        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // MethodInfo �I�u�W�F�N�g�𐶐�����D
        this.resolvedInfo = new TargetMethodInfo(methodModifiers, methodName, privateVisible,
                namespaceVisible, inheritanceVisible, publicVisible, instance, fromLine,
                fromColumn, toLine, toColumn);

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(ownerClass);

        // �^�p�����[�^���������C�����ς݃��\�b�h���ɒǉ�����
        // �^�p�����[�^��extends�߂͂����ł͉������Ȃ�
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(ownerClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        return this.resolvedInfo;
    }

    /**
     * �������Ԃ�l������������
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public final TargetMethodInfo resolveReturnType(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetMethodInfo resolved = this.getResolved();
        final TargetClassInfo owernClass = this.getOwnerClass().getResolved();

        final UnresolvedTypeInfo<? extends TypeInfo> unresolvedType = this.getReturnType();
        final TypeInfo type = unresolvedType.resolve(owernClass, resolved, classInfoManager, null,
                null);
        resolved.setReturnType(type);

        return resolved;

    }

    /**
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * ���\�b�h�����Z�b�g����
     * 
     * @param methodName ���\�b�h��
     */
    public void setMethodName(final String methodName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * ���\�b�h�̕Ԃ�l�̌^��Ԃ�
     * 
     * @return ���\�b�h�̕Ԃ�l�̌^
     */
    public UnresolvedTypeInfo<?> getReturnType() {
        return this.returnType;
    }

    /**
     * ���\�b�h�̕Ԃ�l���Z�b�g����
     * 
     * @param returnType ���\�b�h�̕Ԃ�l
     */
    public void setReturnType(final UnresolvedTypeInfo<?> returnType) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * �C���X�^���X�����o�[���ǂ������Z�b�g����
     * 
     * @param instance �C���X�^���X�����o�[�̏ꍇ�� true�C �X�^�e�B�b�N�����o�[�̏ꍇ�� false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private String methodName;

    /**
     * ���\�b�h�̕Ԃ�l��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedTypeInfo<?> returnType;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private boolean instance;

}
