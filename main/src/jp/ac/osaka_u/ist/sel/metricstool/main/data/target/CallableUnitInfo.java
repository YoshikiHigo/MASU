package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Ăяo���\�ȒP��(���\�b�h��R���X�g���N�^)��\���N���X
 * 
 * @author higo
 */

public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier,
        TypeParameterizable {

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

        this.parameters = new LinkedList<ParameterInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.typeParameterUsages = new HashMap<TypeParameterInfo, TypeInfo>();

        this.unresolvedUsage = new HashSet<UnresolvedExpressionInfo<?>>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * ���̃��\�b�h�̈����� List ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� List
     */
    public List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * �^�p�����[�^�̎g�p��ǉ�����
     * 
     * @param typeParameterInfo �^�p�����[�^ 
     * @param usedType �^�p�����[�^�ɑ������Ă���^
     */
    @Override
    public void addTypeParameterUsage(final TypeParameterInfo typeParameterInfo,
            final TypeInfo usedType) {

        if ((null == typeParameterInfo) || (null == usedType)) {
            throw new IllegalArgumentException();
        }

        this.typeParameterUsages.put(typeParameterInfo, usedType);
    }

    /**
     * �^�p�����[�^�g�p�̃}�b�v��Ԃ�
     * 
     * @return �^�p�����[�^�g�p�̃}�b�v
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        return Collections.unmodifiableMap(this.typeParameterUsages);
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    @Override
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * �w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^�̃C���f�b�N�X
     * @return�@�w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    @Override
    public TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param entityUsage ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedExpressionInfo<?> entityUsage) {

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
    public Set<UnresolvedExpressionInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
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
     * ���̃N���X�Ŏg�p����Ă���^�p�����[�^�Ǝ��ۂɌ^�p�����[�^�ɑ������Ă���^�̃y�A.
     * ���̃N���X�Œ�`����Ă���^�p�����[�^�ł͂Ȃ��D
     */
    private final Map<TypeParameterInfo, TypeInfo> typeParameterUsages;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    protected final List<ParameterInfo> parameters;

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final transient Set<UnresolvedExpressionInfo<?>> unresolvedUsage;
}
