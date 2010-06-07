package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �C���i�[�N���X��\���N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class TargetInnerClassInfo extends TargetClassInfo implements InnerClassInfo {

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param namespace ���O���
     * @param className �N���X��
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, fileInfo, fromLine,
                fromColumn, toLine, toColumn);
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param fullQualifiedName ���S���薼
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface, fileInfo, fromLine, fromColumn, toLine,
                toColumn);
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
    public final ClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // �O���̃��j�b�g���N���X�ł���΂��̂܂ܕԂ�
        if (unitInfo instanceof TargetClassInfo) {
            return (TargetClassInfo) unitInfo;

            // �O���̃��j�b�g��Target��CallableUnitInfo�ł���΁C���̏��L�N���X��Ԃ�
        } else if (unitInfo instanceof TargetMethodInfo
                || unitInfo instanceof TargetConstructorInfo || unitInfo instanceof InitializerInfo) {

            final ClassInfo ownerClass = ((CallableUnitInfo) unitInfo).getOwnerClass();
            return (TargetClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        final UnitInfo unit = this.getOuterUnit();
        if (unit instanceof TypeParameterizable) {
            return (TypeParameterizable) unit;
        }

        assert false : "outerUnit must be TypeParameterizable!";
        return null;
    }

    /**
     * �O���̃��j�b�g�̃I�u�W�F�N�g��ۑ�����ϐ�
     * TargetClassInfo�@�������� TargetMethodInfo �łȂ���΂Ȃ�Ȃ�
     */
    private UnitInfo outerUnit;
}
