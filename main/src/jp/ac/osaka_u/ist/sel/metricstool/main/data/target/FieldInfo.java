package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �t�B�[���h�̏��������N���X
 * 
 * @author y-higo
 * 
 */
public class FieldInfo implements Comparable<FieldInfo> {

    /**
     * �t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     */
    public FieldInfo(String name, TypeInfo type, ClassInfo ownerClass) {
        this.name = name;
        this.type = type;
        this.ownerClass = ownerClass;
    }

    /**
     * �t�B�[���h�I�u�W�F�N�g�̏������`���郁�\�b�h�D���̃t�B�[���h���`���Ă���N���X�̏����ɏ]���D�����N���X���ɒ�`����Ă���ꍇ�́C
     * 
     * @return �t�B�[���h�̏����֌W
     */
    public int compareTo(FieldInfo fieldInfo) {
        ClassInfo classInfo = this.getOwnerClass();
        ClassInfo correspondClassInfo = this.getOwnerClass();
        int classOrder = classInfo.compareTo(correspondClassInfo);
        if (classOrder != 0) {
            return classOrder;
        } else {
            String fieldName = this.getName();
            String correspondFieldName = this.getName();
            return fieldName.compareTo(correspondFieldName);
        }
    }

    /**
     * �t�B�[���h����Ԃ�
     * 
     * @return �t�B�[���h��
     */
    public String getName() {
        return this.name;
    }

    /**
     * �t�B�[���h�̌^��Ԃ�
     * 
     * @return �t�B�[���h�̌^
     */
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă���N���X
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * �t�B�[���h����\���ϐ�
     */
    private final String name;

    /**
     * �t�B�[���h�̌^��\���ϐ�
     */
    private final TypeInfo type;

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ�����ϐ�
     */
    private final ClassInfo ownerClass;

    /**
     * �t�B�[���h�̏C���q��\���ϐ�
     */
    // TODO �C���q��\���ϐ����`����D
}
