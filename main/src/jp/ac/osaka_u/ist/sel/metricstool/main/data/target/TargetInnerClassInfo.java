package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �C���i�[�N���X��\���N���X�D
 * 
 * @author y-higo
 */
public final class TargetInnerClassInfo extends TargetClassInfo {

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param namespace ���O���
     * @param className �N���X��
     * @param outerClass �O���̃N���X
     * @param loc �s��
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final TargetClassInfo outerClass, final int loc) {

        super(modifiers, namespace, className, loc);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param fullQualifiedName ���S���薼
     * @param outerClass �O���̃N���X
     * @param loc �s��
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final TargetClassInfo outerClass, final int loc) {

        super(modifiers, fullQualifiedName, loc);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * �O���̃N���X�̃I�u�W�F�N�g��Ԃ�
     * 
     * @return
     */
    public TargetClassInfo getOuterClass() {
        return this.outerClass;
    }

    /**
     * �O���̃N���X�̃I�u�W�F�N�g��ۑ�����ϐ�
     */
    private final TargetClassInfo outerClass;
}
