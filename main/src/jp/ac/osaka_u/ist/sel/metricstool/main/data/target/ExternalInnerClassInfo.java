package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public class ExternalInnerClassInfo extends ExternalClassInfo implements
        InnerClassInfo<ExternalClassInfo> {

    public ExternalInnerClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit) {
        super(fullQualifiedName);

        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    @Override
    public final UnitInfo getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * �O���̃N���X��Ԃ�.
     * �܂�CgetOuterUnit �̕Ԃ�l��TargetClassInfo�ł���ꍇ�́C���̃I�u�W�F�N�g��Ԃ��C
     * �Ԃ�l���CTargetMethodInfo�ł���ꍇ�́C���̃I�u�W�F�N�g�� ownerClass ��Ԃ��D
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final ExternalClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // �O���̃��j�b�g���N���X�ł���΂��̂܂ܕԂ�
        if (unitInfo instanceof ExternalClassInfo) {
            return (ExternalClassInfo) unitInfo;

            // �O���̃��j�b�g�����\�b�h�ł���΁C���̏��L�N���X��Ԃ�
        } else if (unitInfo instanceof ExternalMethodInfo) {

            final ClassInfo<?, ?, ?, ?> ownerClass = ((TargetMethodInfo) unitInfo).getOwnerClass();
            return (ExternalClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    /**
     * �O���̃��j�b�g�̃I�u�W�F�N�g��ۑ�����ϐ�
     */
    private final UnitInfo outerUnit;

}
