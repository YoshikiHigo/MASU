package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ������̗v�f���L�q����Ă���ӏ���\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class DescriptionToken extends AstTokenAdapter {

    /**
     * ���L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken EXPRESSION = new DescriptionToken("EXPRESSION") {
        @Override
        public boolean isExpression() {
            return true;
        }
    };

    /**
     * �e�N���X�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken INHERITANCE = new DescriptionToken("INHERITANCE") {
        @Override
        public boolean isInheritanceDescription() {
            return true;
        }
    };

    /**
     * �C���q�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken MODIFIER = new DescriptionToken("MODIFIER") {
        @Override
        public boolean isModifiersDefinition() {
            return true;
        }
    };

    /**
     * ���O�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken NAME = new DescriptionToken("NAME") {
        @Override
        public boolean isNameDescription() {
            return true;
        }
    };

    /**
     * �^�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken TYPE = new DescriptionToken("TYPE") {
        @Override
        public boolean isTypeDescription() {
            return true;
        }
    };

    /**
     * ���O��ԗ��p�錾�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken USING = new DescriptionToken("USING") {
        @Override
        public boolean isNameUsingDefinition() {
            return true;
        }
    };

    /**
     * �w�肳�ꂽ������ŏ���������R���X�g���N�^.
     * @param text ���̃g�[�N����\��������.
     */
    public DescriptionToken(final String text) {
        super(text);
    }
}
