package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * �N���[���y�A��\���N���X
 * 
 * @author higo
 *
 */
public class ClonePairInfo implements Cloneable, Comparable<ClonePairInfo> {

    /**
     * �R���X�g���N�^
     */
    public ClonePairInfo() {

        this.codeFragmentA = new CodeCloneInfo();
        this.codeFragmentB = new CodeCloneInfo();

        this.id = number++;
    }

    /**
     * �R���X�g���N�^
     * 
     * @param elementA �����v�fA
     * @param elementB �����v�fB
     */
    public ClonePairInfo(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {

        this();

        this.add(elementA, elementB);
    }

    /**
     * �N���[���y�A�ɗv�f��ǉ�����
     * 
     * @param elementA �ǉ��v�fA
     * @param elementB �ǉ��v�fB
     */
    public void add(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {
        this.codeFragmentA.add(elementA);
        this.codeFragmentB.add(elementB);
    }

    /**
     * �N���[���y�A�ɗv�f�̏W����ǉ�����
     * 
     * @param elementsA �v�f�QA
     * @param elementsB �v�f�QB
     */
    public void addAll(final SortedSet<ExecutableElementInfo> elementsA,
            final SortedSet<ExecutableElementInfo> elementsB) {
        this.codeFragmentA.addAll(elementsA);
        this.codeFragmentB.addAll(elementsB);
    }

    /**
     *�@�N���[���y�A�̒�����Ԃ�
     *
     * @return�@�N���[���y�A�̒���
     */
    public int length() {
        return (this.codeFragmentA.length() + this.codeFragmentB.length()) / 2;
    }

    /**
     * �R�[�h�N���[��A��Ԃ�
     * 
     * @return�@�R�[�h�N���[��A
     */
    public CodeCloneInfo getCodeFragmentA() {
        return this.codeFragmentA;
    }

    /**
     * �R�[�h�N���[��B��Ԃ�
     * 
     * @return�@�R�[�h�N���[��B
     */
    public CodeCloneInfo getCodeFragmentB() {
        return this.codeFragmentB;
    }

    /**
     * �����ŗ^����ꂽ�N���[���y�A�ɁC���̃N���[���y�A����܂���Ă��邩���肷��
     * 
     * @param clonePair �ΏۃN���[���y�A
     * @return ��܂����ꍇ��true, �����łȂ��ꍇ��false
     */
    public boolean subsumedBy(final ClonePairInfo clonePair) {

        return (this.getCodeFragmentA().subsumedBy(clonePair.getCodeFragmentA()) && this
                .getCodeFragmentB().subsumedBy(clonePair.getCodeFragmentB()))
                || (this.getCodeFragmentB().subsumedBy(clonePair.getCodeFragmentA()) && this
                        .getCodeFragmentA().subsumedBy(clonePair.getCodeFragmentB()));
    }

    /**
     * �N���[���y�A��ID��Ԃ�
     * 
     * @return �N���[���y�A��ID
     */
    public int getID() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.getCodeFragmentA().hashCode() + this.getCodeFragmentB().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof ClonePairInfo)) {
            return false;
        }

        final ClonePairInfo target = (ClonePairInfo) o;
        return (this.getCodeFragmentA().equals(target.getCodeFragmentA()) && this
                .getCodeFragmentB().equals(target.getCodeFragmentB()))
                || (this.getCodeFragmentA().equals(target.getCodeFragmentB()) && this
                        .getCodeFragmentB().equals(target.getCodeFragmentA()));
    }

    @Override
    public ClonePairInfo clone() {

        final ClonePairInfo clonePair = new ClonePairInfo();
        final CodeCloneInfo cloneA = this.getCodeFragmentA();
        final CodeCloneInfo cloneB = this.getCodeFragmentB();
        clonePair.addAll(cloneA.getElements(), cloneB.getElements());

        return clonePair;
    }

    @Override
    public int compareTo(final ClonePairInfo clonePair) {

        if (null == clonePair) {
            throw new IllegalArgumentException();
        }

        final CodeCloneInfo thisCodeA = this.getCodeFragmentA();
        final CodeCloneInfo thisCodeB = this.getCodeFragmentB();
        final CodeCloneInfo targetCodeA = clonePair.getCodeFragmentA();
        final CodeCloneInfo targetCodeB = clonePair.getCodeFragmentB();

        // �R�[�h�N���[�����\������v�f���Ŕ�r
        if (thisCodeA.length() > targetCodeA.length()) {
            return 1;
        } else if (thisCodeA.length() < targetCodeA.length()) {
            return -1;
        } else if (thisCodeB.length() > targetCodeB.length()) {
            return 1;
        } else if (thisCodeB.length() < targetCodeB.length()) {
            return -1;
        }

        // �R�[�h�N���[��A�̈ʒu���Ŕ�r
        {
            Iterator<ExecutableElementInfo> thisIterator = thisCodeA.getElements().iterator();
            Iterator<ExecutableElementInfo> targetIterator = targetCodeA.getElements().iterator();
            while (thisIterator.hasNext() && targetIterator.hasNext()) {
                int order = thisIterator.next().compareTo(targetIterator.next());
                if (0 != order) {
                    return order;
                }
            }
        }

        // �R�[�h�N���[��B�̈ʒu���Ŕ�r
        {
            Iterator<ExecutableElementInfo> thisIterator = thisCodeB.getElements().iterator();
            Iterator<ExecutableElementInfo> targetIterator = targetCodeB.getElements().iterator();
            while (thisIterator.hasNext() && targetIterator.hasNext()) {
                int order = thisIterator.next().compareTo(targetIterator.next());
                if (0 != order) {
                    return order;
                }
            }
        }

        return 0;
    }

    final private CodeCloneInfo codeFragmentA;

    final private CodeCloneInfo codeFragmentB;

    final private int id;

    private static int number = 0;
}
