package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

/**
 * AST�p�[�X�Ŏ擾�����t�B�[���h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author y-higo
 * 
 */
public class UnresolvedFieldInfo extends UnresolvedVariableInfo{

    /**
     * Unresolved�t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type, final UnresolvedClassInfo ownerClass){
        
        super(name, type);
        
        if (null == ownerClass){
            throw new NullPointerException();
        }
        
        this.ownerClass = ownerClass;
    }

    /**
     * ���̃t�B�[���h���`���Ă���Unresolved �N���X����Ԃ�
     *
     * @return ���̃t�B�[���h���`���Ă���Unresolved �N���X���
     */
    public UnresolvedClassInfo getOwnerClass(){
        return this.ownerClass;
    }
    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedClassInfo ownerClass; 
}
