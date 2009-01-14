package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


/**
 * �O���N���X�ɒ�`����Ă��郁�\�b�h����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass) {

        super(new HashSet<ModifierInfo>(), methodName, ownerClass, false, true, true, true, 0, 0,
                0, 0);

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������D
     * ��`���Ă���N���X���s���ȏꍇ�ɗp����R���X�g���N�^
     * 
     * @param methodName ���\�b�h��
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, ExternalClassInfo.UNKNOWN, false, true,
                true, true, 0, 0, 0, 0);
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isInheritanceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isNamespaceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPrivateVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPublicVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public TypeParameterInfo getTypeParameter(int index) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public void addTypeParameter(TypeParameterInfo typeParameter) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        throw new CannotUseException();
    }
}
