package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * AST�p�[�X�Ŏ擾�����t�B�[���h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo {

    /**
     * Unresolved�t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type,
            final UnresolvedClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.modifiers = new HashSet<ModifierInfo>();

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
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public void addModifiar(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X����Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X�����Z�b�g����
     * 
     * @param ownerClass ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * ���̃t�B�[���h�̏C���q��ۑ����邽�߂̕ϐ�
     */
    private Set<ModifierInfo> modifiers;

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedClassInfo ownerClass;
}
