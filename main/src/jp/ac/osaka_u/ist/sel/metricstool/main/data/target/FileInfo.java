package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author y-higo
 * 
 * �t�@�C���̏���\���N���X�D
 */
public final class FileInfo {

    /**
     * �w�肳�ꂽ�t�@�C�����̃I�u�W�F�N�g������������D
     * 
     * @param name �t�@�C����
     */
    public FileInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.definedClasses = new TreeSet<ClassInfo>();
    }

    /**
     * ���̃t�@�C���ɒ�`����Ă���N���X��ǉ�����D
     * 
     * @param definedClass ��`���ꂽ�N���X�D
     */
    public void addDefinedClass(final ClassInfo definedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedClass) {
            throw new NullPointerException();
        }

        this.definedClasses.add(definedClass);
    }

    /**
     * ���̃t�@�C���ɒ�`����Ă���N���X�̃C�e���[�^��Ԃ�
     * 
     * @return ���̃t�@�C���ɒ�`����Ă���N���X�̃C�e���[�^
     */
    public Iterator<ClassInfo> definedClassIterator() {
        SortedSet<ClassInfo> unmodifiableDefinedClasses = Collections
                .unmodifiableSortedSet(this.definedClasses);
        return unmodifiableDefinedClasses.iterator();
    }

    /**
     * �����Ƃ��̃t�@�C�������������𔻒肷��D����ɂ́C�ϐ�name��p����D
     * 
     * @param o ��r�Ώۃt�@�C��
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        String thisName = this.getName();
        String correspondName = ((FileInfo) o).getName();
        return thisName.equals(correspondName);
    }

    /**
     * �s����Ԃ��D
     * 
     * @return �s��
     */
    public int getLOC() {
        return loc;
    }

    /**
     * �t�@�C������Ԃ��D ���݃t���p�X�ŕԂ����C�f�B���N�g���ƃt�@�C�����𕪂��������ǂ������D
     * 
     * @return �t�@�C����
     */
    public String getName() {
        return name;
    }

    /**
     * �t�@�C���̃n�b�V���R�[�h��Ԃ��D�n�b�V���R�[�h�̓t�@�C�����i�t���p�X�j��p���Čv�Z�����
     * 
     * @return ���̃t�@�C���̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * �ϐ� loc �� setter�D�s�������Z�b�g����D
     * 
     * @param loc �s��
     */
    public void setLOC(final int loc) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.loc = loc;
    }

    /**
     * �t�@�C���̍s����\���ϐ��D
     */
    private int loc;

    /**
     * �t�@�C������\���ϐ�. �n�b�V���R�[�h�̌v�Z�Ɏg���Ă���D
     */
    private final String name;

    private final SortedSet<ClassInfo> definedClasses;

    // TODO �錾����Ă���N���X�̏���ǉ�
    // TODO import���Ă���N���X�̏���ǉ�
    // TODO include���Ă���t�@�C���̏���ǉ�
}
