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
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final TargetClassInfo outerClass, final int loc,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(modifiers, namespace, className, loc, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance);

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
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final TargetClassInfo outerClass, final int loc,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(modifiers, fullQualifiedName, loc, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance);

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
