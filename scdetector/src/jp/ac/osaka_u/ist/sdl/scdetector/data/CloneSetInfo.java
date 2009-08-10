package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �N���[���Z�b�g��\���N���X
 * 
 * @author higo
 *
 */
public class CloneSetInfo implements Comparable<CloneSetInfo> {

    /**
     * �R���X�g���N�^
     */
    public CloneSetInfo() {
        this.codeclones = new HashSet<CodeCloneInfo>();
        this.id = number++;
    }

    /**
     * �R�[�h�N���[����ǉ�����
     * 
     * @param codeclone �ǉ�����R�[�h�N���[��
     * @return �ǉ������ꍇ��true,�@���łɊ܂܂�Ă���ǉ����Ȃ������ꍇ��false
     */
    public boolean add(final CodeCloneInfo codeclone) {
        return this.codeclones.add(codeclone);
    }

    /**
     * �R�[�h�N���[���Q��ǉ�����
     * 
     * @param codeclones �ǉ�����R�[�h�N���[���Q
     */
    public void addAll(final Collection<CodeCloneInfo> codeclones) {

        for (final CodeCloneInfo codeFragment : codeclones) {
            this.add(codeFragment);
        }
    }

    /**
     * �N���[���Z�b�g���\������R�[�h�N���[���Q��Ԃ�
     * 
     * @return�@�N���[���Z�b�g���\������R�[�h�N���[���Q
     */
    public Set<CodeCloneInfo> getCodeClones() {
        return Collections.unmodifiableSet(this.codeclones);
    }

    /**
     * �N���[���Z�b�g��ID��Ԃ�
     * 
     * @return�@�N���[���Z�b�g��ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * �N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̐���Ԃ�
     * 
     * @return�@�N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̐�
     */
    public int getNumberOfCodeclones() {
        return this.codeclones.size();
    }

    /**
     * �N���[���Z�b�g�Ɋ܂܂��M���b�v�̐���Ԃ�
     * 
     * @return�@�N���[���Z�b�g�Ɋ܂܂��M���b�v�̐�
     */
    public int getGapsNumber() {

        int gap = 0;

        for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
            gap += codeFragment.getGapsNumber();
        }

        return gap;
    }

    /**
     * �N���[���Z�b�g�̒����i�܂܂��R�[�h�N���[���̑傫���j��Ԃ�
     * 
     * @return�@�N���[���Z�b�g�̒����i�܂܂��R�[�h�N���[���̑傫���j
     */
    public int getLength() {
        int total = 0;
        for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
            total += codeFragment.length();
        }

        return total / this.getNumberOfCodeclones();
    }

    @Override
    public int compareTo(CloneSetInfo o) {

        if (this.getID() < o.getID()) {
            return -1;
        } else if (this.getID() > o.getID()) {
            return 1;
        } else {
            return 0;
        }
    }

    final private Set<CodeCloneInfo> codeclones;

    final private int id;

    private static int number = 0;
}
