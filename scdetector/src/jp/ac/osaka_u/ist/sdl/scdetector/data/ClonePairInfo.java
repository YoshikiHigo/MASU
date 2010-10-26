package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

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

		this.codecloneA = new CodeCloneInfo();
		this.codecloneB = new CodeCloneInfo();

		this.id = number++;
	}

	public ClonePairInfo(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

		this();

		this.add(nodeA, nodeB);
	}

	public void add(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {
		this.codecloneA.add(nodeA);
		this.codecloneB.add(nodeB);
	}

	/**
	 *�@�N���[���y�A�̒�����Ԃ�
	 * 
	 * @return�@�N���[���y�A�̒���
	 */
	public int length() {
		return (this.codecloneA.length() + this.codecloneB.length()) / 2;
	}

	/**
	 * �����ŗ^����ꂽ�N���[���y�A�ɁC���̃N���[���y�A����܂���Ă��邩���肷��
	 * 
	 * @param clonePair
	 *            �ΏۃN���[���y�A
	 * @return ��܂����ꍇ��true, �����łȂ��ꍇ��false
	 */
	public boolean subsumedBy(final ClonePairInfo clonePair) {

		return (this.codecloneA.subsumedBy(clonePair.codecloneA) && this.codecloneB
				.subsumedBy(clonePair.codecloneB))
				|| (this.codecloneB.subsumedBy(clonePair.codecloneA) && this.codecloneA
						.subsumedBy(clonePair.codecloneB));
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
		return this.codecloneA.hashCode() + this.codecloneB.hashCode();
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
		return (this.codecloneA.equals(target.codecloneA) && this.codecloneB
				.equals(target.codecloneB))
				|| (this.codecloneA.equals(target.codecloneB) && this.codecloneB
						.equals(target.codecloneA));
	}

	@Override
	public ClonePairInfo clone() {

		final ClonePairInfo clonePair = new ClonePairInfo();
		final CodeCloneInfo cloneA = this.codecloneA;
		final CodeCloneInfo cloneB = this.codecloneB;
		clonePair.codecloneA.addElements(cloneA);
		clonePair.codecloneB.addElements(cloneB);

		return clonePair;
	}

	@Override
	public int compareTo(final ClonePairInfo clonePair) {

		if (null == clonePair) {
			throw new IllegalArgumentException();
		}

		final CodeCloneInfo thisCodeA = this.codecloneA;
		final CodeCloneInfo thisCodeB = this.codecloneB;
		final CodeCloneInfo targetCodeA = clonePair.codecloneA;
		final CodeCloneInfo targetCodeB = clonePair.codecloneB;

		/*
		 * // �R�[�h�N���[�����\������v�f���Ŕ�r if (thisCodeA.length() > targetCodeA.length())
		 * { return 1; } else if (thisCodeA.length() < targetCodeA.length()) {
		 * return -1; } else if (thisCodeB.length() > targetCodeB.length()) {
		 * return 1; } else if (thisCodeB.length() < targetCodeB.length()) {
		 * return -1; }
		 */

		// �R�[�h�N���[��A�̈ʒu���Ŕ�r
		{
			Iterator<ExecutableElementInfo> thisIterator = thisCodeA
					.getElements().iterator();
			Iterator<ExecutableElementInfo> targetIterator = targetCodeA
					.getElements().iterator();
			while (thisIterator.hasNext() && targetIterator.hasNext()) {
				int order = thisIterator.next()
						.compareTo(targetIterator.next());
				if (0 != order) {
					return order;
				}
			}
		}

		// �R�[�h�N���[��B�̈ʒu���Ŕ�r
		{
			Iterator<ExecutableElementInfo> thisIterator = thisCodeB
					.getElements().iterator();
			Iterator<ExecutableElementInfo> targetIterator = targetCodeB
					.getElements().iterator();
			while (thisIterator.hasNext() && targetIterator.hasNext()) {
				int order = thisIterator.next()
						.compareTo(targetIterator.next());
				if (0 != order) {
					return order;
				}
			}
		}

		return 0;
	}

	final public CodeCloneInfo codecloneA;

	final public CodeCloneInfo codecloneB;

	final private int id;

	private static int number = 0;
}
