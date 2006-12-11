package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void �^��\���N���X�D
 * 
 * @author y-higo
 * 
 */
public final class VoidTypeInfo implements TypeInfo, UnresolvedTypeInfo {

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
    public String getTypeName() {
        return VOID_STRING;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo){
        
        if (null == typeInfo){
            throw new NullPointerException();
        }
        
        return typeInfo instanceof VoidTypeInfo;
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
