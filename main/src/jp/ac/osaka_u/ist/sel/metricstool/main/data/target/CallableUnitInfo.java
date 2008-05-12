package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Ăяo���\�ȒP��(���\�b�h��R���X�g���N�^)��\���N���X
 * 
 * @author higo
 */

public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q��Set
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    CallableUnitInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.unresolvedUsage = new HashSet<UnresolvedEntityUsageInfo<?>>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param entityUsage ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedEntityUsageInfo<?> entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set ��Ԃ��D
     * 
     * @return ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedEntityUsageInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean publicVisible;

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final Set<UnresolvedEntityUsageInfo<?>> unresolvedUsage;
}
