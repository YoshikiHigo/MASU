package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


/**
 * �O���N���X�ɒ�`����Ă���R���X�g���N�^����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * �O���N���X�ɒ�`����Ă���R���X�g���N�^�I�u�W�F�N�g������������
     * 
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalConstructorInfo(final Set<ModifierInfo> modifiers,
            final ExternalClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible) {
        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, 0, 0, 0, 0);
    }

    /**
     * �O���N���X�ɒ�`����Ă���R���X�g���N�^�I�u�W�F�N�g������������
     * 
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalConstructorInfo(final ExternalClassInfo ownerClass) {
        super(new HashSet<ModifierInfo>(), ownerClass, false, true, true, true, 0, 0, 0, 0);
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
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
