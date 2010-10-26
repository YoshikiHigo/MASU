package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.PDGMergedNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldInNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterOutNode;

/**
 * �R�[�h�N���[����\���N���X
 * 
 * @author higo
 * 
 */
public class CodeCloneInfo implements Comparable<CodeCloneInfo> {

	final private SortedSet<ExecutableElementInfo> elements;

	/**
	 * �R���X�g���N�^
	 */
	public CodeCloneInfo() {
		this.elements = new TreeSet<ExecutableElementInfo>();
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param element
	 *            �����v�f
	 */
	public CodeCloneInfo(final PDGNode<?> node) {
		this();
		this.add(node);
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param elements
	 *            �����v�f�Q
	 */
	public CodeCloneInfo(final Collection<PDGNode<?>> nodes) {
		this();
		this.addAll(nodes);
	}

	public void addElements(final CodeCloneInfo codeclone) {
		this.elements.addAll(codeclone.getElements());
	}

	public void add(final PDGNode<?> node) {

		// ���\�b�h�G���^�[�m�[�h�͒ǉ����Ȃ�
		if (node instanceof PDGMethodEnterNode) {

		}

		// �f�[�^�m�[�h�i�p�����[�^��t�B�[���h�p�b�V���O�̂��߂́j�͒ǉ����Ȃ�
		else if (node instanceof PDGParameterOutNode
				|| node instanceof PDGFieldInNode
				|| node instanceof PDGFieldOutNode) {
		}

		// �W��m�[�h�̂́C�S�ẴR�A��ǉ�
		else if (node instanceof PDGMergedNode) {
			final PDGMergedNode mergedNode = (PDGMergedNode) node;
			this.elements.addAll(mergedNode.getCores());
		}

		// ����ȊO�̎��̓R�A��ǉ�
		else {
			this.elements.add(node.getCore());
		}
	}

	public void addAll(final Collection<PDGNode<?>> nodes) {
		for (final PDGNode<?> node : nodes) {
			this.add(node);
		}
	}

	/**
	 * �����ŗ^����ꂽ�v�f���C���̃R�[�h�N���[���Ɋ܂܂�邩���肷��
	 * 
	 * @param element
	 *            �Ώۗv�f
	 * @return�@�܂܂��ꍇ��true,�@�����łȂ��ꍇ��false
	 */
	public boolean contain(final ExecutableElementInfo element) {
		return this.elements.contains(element);
	}

	public FileInfo getOwnerFile() {
		return ((TargetClassInfo) this.elements.first().getOwnerMethod()
				.getOwnerClass()).getOwnerFile();
	}

	/**
	 * �R�[�h�N���[���̒�����Ԃ�
	 * 
	 * @return�@�R�[�h�N���[���̒���
	 */
	public int length() {
		return this.elements.size();
	}

	/**
	 * �����ŗ^����ꂽ�R�[�h�N���[���ɁC���̃R�[�h�N���[������܂���Ă��邩���肷��
	 * 
	 * @param codeclone
	 *            �ΏۃR�[�h�N���[��
	 * @return ��܂����ꍇ��true, �����łȂ��ꍇ��false
	 */
	public boolean subsumedBy(final CodeCloneInfo codeclone) {
		return codeclone.getElements().containsAll(this.getElements())
				&& (this.elements.size() < codeclone.getElements().size());
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		if (!(o instanceof CodeCloneInfo)) {
			return false;
		}

		final CodeCloneInfo target = (CodeCloneInfo) o;
		return this.getElements().containsAll(target.getElements())
				&& target.getElements().containsAll(this.getElements());
	}

	/**
	 * �R�[�h�N���[�����\������v�f�Q��Ԃ�
	 * 
	 * @return�@�R�[�h�N���[�����\������v�f�Q
	 */
	public SortedSet<ExecutableElementInfo> getElements() {
		return Collections.unmodifiableSortedSet(this.elements);
	}

	/**
	 * �R�[�h�N���[�����\������v�f���܂ރ��\�b�h�ꗗ��Ԃ�
	 * 
	 * @return
	 */
	public SortedSet<CallableUnitInfo> getOwnerCallableUnits() {
		final SortedSet<CallableUnitInfo> methods = new TreeSet<CallableUnitInfo>();
		for (final ExecutableElementInfo element : this.getElements()) {
			methods.add(element.getOwnerMethod());
		}
		return methods;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (final ExecutableElementInfo element : this.getElements()) {
			hash += element.hashCode();
		}
		return hash;
	}

	public float density() {
		final Set<Integer> duplication = new HashSet<Integer>();
		for (final ExecutableElementInfo element : this.getElements()) {
			for (int line = element.getFromLine(); line <= element
					.getToColumn(); line++) {
				duplication.add(new Integer(line));
			}
		}
		return (float) duplication.size()
				/ (float) (this.getElements().last().getToLine()
						- this.getElements().first().getFromLine() + 1);
	}

	@Override
	public int compareTo(final CodeCloneInfo o) {

		// �K�v�ȏ����C�����Ȃ����ƁI
		if (this.equals(o)) {
			return 0;
		}

		final Iterator<ExecutableElementInfo> thisIterator = this.getElements()
				.iterator();
		final Iterator<ExecutableElementInfo> targetIterator = o.getElements()
				.iterator();

		// �����̗v�f���������
		while (thisIterator.hasNext() && targetIterator.hasNext()) {

			final int elementOrder = thisIterator.next().compareTo(
					targetIterator.next());
			if (0 != elementOrder) {
				return elementOrder;
			}
		}

		if (!thisIterator.hasNext() && !targetIterator.hasNext()) {
			return 0;
		}

		if (!thisIterator.hasNext()) {
			return -1;
		}

		if (!targetIterator.hasNext()) {
			return 1;
		}

		assert false : "Here shouldn't be reached!";
		return 0;
	}

	/**
	 * ���̃R�[�h�N���[�����ɑ��݂���M���b�v�̐���Ԃ�
	 * 
	 * @return ���̃R�[�h�N���[�����ɑ��݂���M���b�v�̐�
	 */
	public int getGapsNumber() {
		int gap = 0;

		final CallableUnitInfo ownerMethod = this.getElements().first()
				.getOwnerMethod();
		final SortedSet<ExecutableElementInfo> elements = getAllExecutableElements(ownerMethod);
		final ExecutableElementInfo[] elementArray = elements
				.toArray(new ExecutableElementInfo[0]);

		final ExecutableElementInfo[] clonedElementArray = this.getElements()
				.toArray(new ExecutableElementInfo[0]);
		CLONE: for (int i = 0; i < clonedElementArray.length - 1; i++) {
			for (int j = 0; j < elementArray.length - 1; j++) {

				// i�Ԗڂ�j�Ԗڂ̗v�f�������������`�F�b�N
				if (equals(clonedElementArray[i], elementArray[j])) {

					// i+1�Ԗڂ�j+1�Ԗڂ��������Ȃ��̂ł���΁C�M���b�v����
					if (!equals(clonedElementArray[i + 1], elementArray[j + 1])) {
						gap++;
					}

					continue CLONE;
				}
			}
		}

		return gap;
	}

	private static SortedSet<ExecutableElementInfo> getAllExecutableElements(
			final LocalSpaceInfo localSpace) {

		if (null == localSpace) {
			throw new IllegalArgumentException("localSpace is null.");
		}

		if (localSpace instanceof ExternalMethodInfo
				|| localSpace instanceof ExternalConstructorInfo) {
			throw new IllegalArgumentException(
					"localSpace is an external local space.");
		}

		final SortedSet<ExecutableElementInfo> allElements = new TreeSet<ExecutableElementInfo>();
		for (final StatementInfo innerStatement : localSpace.getStatements()) {

			// �P���̏ꍇ
			if (innerStatement instanceof SingleStatementInfo
					|| innerStatement instanceof CaseEntryInfo
					|| innerStatement instanceof LabelInfo) {
				allElements.add(innerStatement);
			}

			// �u���b�N���̏ꍇ
			else if (innerStatement instanceof BlockInfo) {

				if (innerStatement instanceof ConditionalBlockInfo) {
					final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
							.getConditionalClause().getCondition();
					allElements.add(condition);

					if (innerStatement instanceof ForBlockInfo) {
						allElements.addAll(((ForBlockInfo) innerStatement)
								.getInitializerExpressions());
						allElements.addAll(((ForBlockInfo) innerStatement)
								.getIteratorExpressions());
					}
				}

				allElements
						.addAll(getAllExecutableElements((BlockInfo) innerStatement));
			}

			else {
				assert false : "Here shouldn't be reached!";
			}
		}

		return allElements;
	}

	private static boolean equals(final ExecutableElementInfo element1,
			final ExecutableElementInfo element2) {

		return (element1.getFromLine() == element2.getFromLine())
				&& (element1.getFromColumn() == element2.getFromColumn())
				&& (element1.getToLine() == element2.getToLine())
				&& (element1.getToColumn() == element2.getToColumn());
	}
}
