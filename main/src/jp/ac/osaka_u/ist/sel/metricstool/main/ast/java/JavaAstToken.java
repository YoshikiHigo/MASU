package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;


/**
 * Java���L�̗v�f��\���g�[�N�����`����N���X
 * 
 * @author kou-tngt
 *
 */
public class JavaAstToken extends AstTokenAdapter {

    /**
     * import�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken CLASS_IMPORT = new JavaAstToken("CLASS_IMPORT"){
        @Override
        public boolean isClassImport(){
            return true;
        }        
    };

    /**
     * import�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken MEMBER_IMPORT = new JavaAstToken("MEMBER_IMPORT"){
        @Override
        public boolean isMemberImport(){
            return true;
        } 
    };
    
    /**
     * throw�L�q����\���萔�C���X�^���X
     */
    //TODO: �ꏊ�����ł����̂���?
    public static final JavaAstToken THROWS = new JavaAstToken("THROWS") {
        @Override
        public boolean isThrows(){
            return true;
        }
    };
    
    /**
     * enum�v�f�̋L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken ENUM_CONSTANT = new JavaAstToken("ENUM_CONST"){
        @Override
        public boolean isEnumConstant() {
            return true;
        }
    };

    /**
     * super�L�q����\���萔�C���X�^���X
     */
    public static final JavaAstToken SUPER = new JavaAstToken("SUPER");

    /**
     * �z�񏉊�������\���萔�C���X�^���X
     */
    public static final JavaAstToken ARRAY_INIT = new JavaAstToken("ARRAY_INIT") {
        @Override
        public boolean isArrayInitilizer() {
            return true;
        }
    };
    
    /**
     * new���ɂ��z��^�w��i[]�j��\���萔�C���X�^���X
     */
    public static final JavaAstToken ARRAY_INSTANTIATION = new JavaAstToken("ARRAY_INSTANTIATION");

    /**
     * this()�̂悤�Ȏ��R���X�g���N�^�̌Ăяo����\���萔�C���X�^���X
     */
    public static final JavaAstToken CONSTRUCTOR_CALL = new JavaAstToken("CONSTRUCTOR_CALL") {
        @Override
        public boolean isThisConstructorCall(){
            return true;
        }
    };

    /**
     * super()�̂悤�Ȑe�R���X�g���N�^�̌Ăяo����\���萔�C���X�^���X
     */
    public static final JavaAstToken SUPER_CONSTRUCTOR_CALL = new JavaAstToken(
            "SUPER_CONSTRUCTOR_CALL"){
        @Override
        public boolean isSuperConstructorCall(){
            return true;
        }
    };

    /**
     * .class���L�q��\���萔�C���X�^���X
     */
    public static final JavaAstToken CLASS = new JavaAstToken("CLASS");

    /**
     * �C���^�t�F�[�X��`����\���萔�C���X�^���X
     */
    public static final JavaAstToken INTERFACE_DEFINITION = new JavaAstToken("INTERFACE_DEFINITION") {
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
     * �w�肳�ꂽ�������\���g�[�N�����쐬����.
     * @param text �g�[�N���̕�����
     */
    public JavaAstToken(final String text) {
        super(text);
    }

}
