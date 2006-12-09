package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * void �^��\���N���X�D
 * 
 * @author y-higo
 * 
 */
public final class VoidTypeInfo implements TypeInfo {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static VoidTypeInfo getInstance(){
        return SINGLETON;
    }
    
    /**
     * void �^�̖��O��Ԃ��D
     */
    public String getName() {
        return VOID_STRING;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo){
        
        if (null == typeInfo){
            throw new NullPointerException();
        }
        
        if (typeInfo instanceof VoidTypeInfo){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * void �^�̌^����\���萔
     */
    public static final String VOID_STRING = new String("void");

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final VoidTypeInfo SINGLETON = new VoidTypeInfo();
}
