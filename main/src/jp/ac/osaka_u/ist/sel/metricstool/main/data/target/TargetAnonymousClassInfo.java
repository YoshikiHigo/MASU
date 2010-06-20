package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;


/**
 * �����C���i�[�N���X�������N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TargetAnonymousClassInfo extends TargetInnerClassInfo implements
        AnonymousClassInfo {

    /**
     * �����C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param namespace ���O���
     * @param className �N���X��
     * @param outerClass �O���̃N���X
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetAnonymousClassInfo(final NamespaceInfo namespace, final String className,
            final TargetClassInfo outerClass, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), namespace, className, outerClass, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * �����C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param fullQualifiedName ���S���薼
     * @param outerClass �O���̃N���X
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetAnonymousClassInfo(final String[] fullQualifiedName,
            final TargetClassInfo outerClass, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, outerClass, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }

    public void setOuterCallableUnit(final CallableUnitInfo outerCallableUnit) {
        if (null == outerCallableUnit) {
            throw new IllegalArgumentException();
        }
        this.outerCallableUnit = outerCallableUnit;
    }

    public CallableUnitInfo getOuterCallableUnit() {
        return this.outerCallableUnit;
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    @Override
    public UnitInfo getOuterUnit() {
        return this.getOuterCallableUnit();
    }

    private CallableUnitInfo outerCallableUnit;
}
