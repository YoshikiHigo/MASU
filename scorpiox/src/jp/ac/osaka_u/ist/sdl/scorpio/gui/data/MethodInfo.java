package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

public class MethodInfo implements Comparable<MethodInfo> {

	public MethodInfo(final String name, final int id, final int fromLine,
			final int toLine) {
		this.name = name;
		this.id = id;
		this.fromLine = fromLine;
		this.toLine = toLine;
	}

	public MethodInfo() {
	}

	/**
	 * ���\�b�h����Ԃ�
	 * 
	 * @return ���\�b�h��
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���\�b�h����ݒ肷��
	 * 
	 * @param name
	 *            �ݒ肷�郁�\�b�h��
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * ���\�b�h��ID��Ԃ�
	 * 
	 * @return�@���\�b�h��ID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * ���\�b�h��ID��ݒ肷��
	 * 
	 * @param id
	 *            �ݒ肷��ID
	 */
	public void setID(final int id) {
		this.id = id;
	}

	/**
	 * ���\�b�h�̊J�n�s��Ԃ�
	 * 
	 * @return�@���\�b�h�̊J�n�s
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��ݒ肷��
	 * 
	 * @param fromLine
	 *            �ݒ肷��s��
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��Ԃ�
	 * 
	 * @return�@���\�b�h�̊J�n�s
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��ݒ肷��
	 * 
	 * @param fromLine
	 *            �ݒ肷��s��
	 */
	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	@Override
	public int compareTo(MethodInfo o) {
		if (this.id < o.id) {
			return -1;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return 0;
		}
	}

	private String name;
	private int id;
	private int fromLine;
	private int toLine;
}
