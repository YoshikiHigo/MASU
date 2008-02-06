package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * �����̗v�f�̒�`����\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class DefinitionToken extends AstTokenAdapter {

    /**
     * �N���X��`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken CLASS_DEFINITION = new DefinitionToken("CLASS_DEFINITION") {
        @Override
        public boolean isClassDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * �R���X�g���N�^��`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken CONSTRUCTOR_DEFINITION = new DefinitionToken(
            "CONSTRUCTOR_DEFINITION") {
        @Override
        public boolean isMethodDefinition() {
            return true;
        }

        @Override
        public boolean isConstructorDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * �t�B�[���h��`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken FIELD_DEFINITION = new DefinitionToken("FIELD_DEFINITION") {
        @Override
        public boolean isFieldDefinition() {
            return true;
        }
    };

    /**
     * ���[�J���p�����[�^�ifor����catch�߂̍ŏ��ɐ錾�����ϐ��j�̒�`����\���萔�C���X�^���X
     */
    public static final DefinitionToken LOCAL_PARAMETER_DEFINITION = new DefinitionToken(
            "LOCAL_PARAMETER_DEFINITION") {
        @Override
        public boolean isLocalParameterDefinition() {
            return true;
        }
    };

    /**
     * ���[�J���ϐ���`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken LOCALVARIABLE_DEFINITION = new DefinitionToken(
            "LOCALVARIABLE_DEFINITION") {
        @Override
        public boolean isLocalVariableDefinition() {
            return true;
        }
    };

    /**
     * ���\�b�h��`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken METHOD_DEFINITION = new DefinitionToken("METHOD_DEFINITION") {
        @Override
        public boolean isMethodDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * ���\�b�h�p�����[�^��`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken METHOD_PARAMETER_DEFINITION = new DefinitionToken(
            "METHOD_PARAMETER_DEFINITION") {
        @Override
        public boolean isMethodParameterDefinition() {
            return true;
        }
    };

    /**
     * ���O��Ԃ̒�`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken NAMESPACE_DEFINITION = new DefinitionToken(
            "NAMESPACE_DEFINITION") {
        @Override
        public boolean isNameSpaceDefinition() {
            return true;
        }
    };
    
    /**
     * �^�p�����[�^�̒�`����\���萔�C���X�^���X.
     */
    public static final DefinitionToken TYPE_PARAMETER_DEFINITION = new DefinitionToken("TYPE_PARAMETER_DEFINITION"){
        @Override
        public boolean isTypeParameterDefinition(){
            return true;
        }
    };

    /**
     * �w�肳�ꂽ������ŏ���������R���X�g���N�^.
     * @param text ���̃g�[�N����\��������.
     */
    public DefinitionToken(final String text) {
        super(text);
    }
}
