package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


@SuppressWarnings("serial")
public class ExternalInnerClassInfo extends ExternalClassInfo implements InnerClassInfo {

    public ExternalInnerClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit) {
        super(fullQualifiedName);
        this.outerUnit = outerUnit;
    }

    public ExternalInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface);
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
     * �O���̃��j�b�g��ݒ肷��
     * 
     * @param outerUnit �O���̃��j�b�g
     */
    public void setOuterUnit(final UnitInfo outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * �O���̃N���X��Ԃ�.
     * �܂�CgetOuterUnit �̕Ԃ�l��TargetClassInfo�ł���ꍇ�́C���̃I�u�W�F�N�g��Ԃ��C
     * �Ԃ�l���CTargetMethodInfo�ł���ꍇ�́C���̃I�u�W�F�N�g�� ownerClass ��Ԃ��D
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final ClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // �O���̃��j�b�g���N���X�ł���΂��̂܂ܕԂ�
        if (unitInfo instanceof ExternalClassInfo) {
            return (ExternalClassInfo) unitInfo;

            // �O���̃��j�b�g�����\�b�h�ł���΁C���̏��L�N���X��Ԃ�
        } else if (unitInfo instanceof ExternalMethodInfo) {

            final ClassInfo ownerClass = ((ExternalMethodInfo) unitInfo).getOwnerClass();
            return (ExternalClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return (TypeParameterizable) this.getOuterUnit();
    }

    /**
     * �O���̃��j�b�g�̃I�u�W�F�N�g��ۑ�����ϐ�
     */
    private UnitInfo outerUnit;

}
