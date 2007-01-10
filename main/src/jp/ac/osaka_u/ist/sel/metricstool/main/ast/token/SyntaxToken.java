package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ���@����\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class SyntaxToken extends AstTokenAdapter {

    /**
     * ���O��؂��\���萔�C���X�^���X
     */
    public static final SyntaxToken NAME_SEPARATOR = new SyntaxToken("NAME_SEPARATOR") {
        @Override
        public boolean isNameSeparator() {
            return true;
        }
    };

    /**
     * �N���X�u���b�N�ȊO�̃u���b�N�̊J�n��\���萔�C���X�^���X
     */
    public static final SyntaxToken BLOCK_START = new SyntaxToken("BLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }
    };

    /**
     * �N���X�u���b�N�̊J�n��\���萔�C���X�^���X
     */
    public static final SyntaxToken CLASSBLOCK_START = new SyntaxToken("CLASSBLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }

        @Override
        public boolean isClassBlock() {
            return true;
        }
    };

    /**
     * ���\�b�h�Ăяo������\���萔�C���X�^���X
     */
    public static final SyntaxToken METHOD_CALL = new SyntaxToken("METHOD_CALL") {
        @Override
        public boolean isMethodCall() {
            return true;
        }
    };

    /**
     * new����\���萔�C���X�^���X
     */
    public static final SyntaxToken NEW = new SyntaxToken("NEW") {
        @Override
        public boolean isInstantiation() {
            return true;
        }
    };

    /**
     * �z��錾��\���萔�C���X�^���X
     */
    public static final SyntaxToken ARRAY = new SyntaxToken("ARRAY") {
        @Override
        public boolean isArrayDeclarator() {
            return true;
        }
    };

    /**
     * �w�肳�ꂽ������ŕ\�����g�[�N�����쐬����R���X�g���N�^
     * @param text �g�[�N����\��������
     */
    public SyntaxToken(final String text) {
        super(text);
    }
}
