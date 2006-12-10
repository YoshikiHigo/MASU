package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���O�����ł��Ȃ��^��\���N���X�D
 * 
 * @author y-higo
 * 
 */
public class UnknownTypeInfo implements TypeInfo {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static UnknownTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * ���O�����ł��Ȃ��^�̖��O��Ԃ��D
     */
    public String getName() {
        return UNKNOWN_STRING;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo){
        
        if (null == typeInfo){
            throw new NullPointerException();
        }
        
        if (typeInfo instanceof UnknownTypeInfo){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * void �^�̌^����\���萔
     */
    public static final String UNKNOWN_STRING = new String("unknown");

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final UnknownTypeInfo SINGLETON = new UnknownTypeInfo();
}