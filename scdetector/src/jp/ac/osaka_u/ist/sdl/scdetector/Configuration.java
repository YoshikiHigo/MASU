package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.c = 1000000;
        this.d = null;
        this.l = null;
        this.o = null;
        this.r = true;
        this.pv = 1;
        this.pi = 1;
        this.pc = 1;
        this.po = 0;
        this.pl = 1;
        this.pr = false;
        this.fi = false;
        this.fj = false;
        this.fk = 100;
        this.fl = 2;
    }

    public int getC() {
        return this.c;
    }

    public void setC(final int c) {
        this.c = c;
    }

    public String getD() {
        return this.d;
    }

    public void setD(final String d) {
        this.d = d;
    }

    public String getL() {
        return this.l;
    }

    public void setL(final String l) {
        this.l = l;
    }

    public String getO() {
        return this.o;
    }

    public void setO(final String o) {
        this.o = o;
    }

    public boolean getR() {
        return this.r;
    }

    public void setR(final boolean r) {
        this.r = r;
    }

    public int getS() {
        return this.s;
    }

    public void setS(final int s) {
        this.s = s;
    }

    public int getPV() {
        return this.pv;
    }

    public void setPV(final int pv) {

        if ((pv < 0) || (2 < pv)) {
            throw new RuntimeException("\"pv\" option must be between 0 and 2.");
        }
        this.pv = pv;
    }

    public int getPI() {
        return this.pi;
    }

    public void setPI(final int pi) {

        if ((pi < 0) || (3 < pi)) {
            throw new RuntimeException("\"pi\" option must be between 0 and 3.");
        }
        this.pi = pi;
    }

    public int getPC() {
        return this.pc;
    }

    public void setPC(final int pc) {

        if ((pc < 0) || (2 < pc)) {
            throw new RuntimeException("\"pc\" option must be between 0 and 2.");
        }
        this.pc = pc;
    }

    public int getPO() {
        return this.po;
    }

    public void setPO(final int po) {

        if ((po < 0) || (2 < po)) {
            throw new RuntimeException("\"po\" option must be between 0 and 2.");
        }
        this.po = po;
    }

    public int getPL() {
        return this.pl;
    }

    public void setPL(final int pl) {

        if ((pl < 0) || (2 < pl)) {
            throw new RuntimeException("\"pl\" option must be between 0 and 2.");
        }
        this.pl = pl;
    }

    public boolean getPR() {
        return this.pr;
    }

    public void setPR(final boolean pr) {
        this.pr = pr;
    }

    // ���̃N���[���y�A�ɕ�܂���Ă���N���[���y�A����菜���I�v�V����
    public boolean getFI() {
        return this.fi;
    }

    public boolean setFI(final boolean fi) {
        return this.fi = fi;
    }

    public boolean getFJ() {
        return this.fj;
    }

    public void setFJ(final boolean fj) {
        this.fj = fj;
    }

    public int getFK() {
        return this.fk;
    }

    public void setFK(final int fk) {

        if ((fk < 1) || (100 < fk)) {
            throw new RuntimeException("\"fk\" option must be between 1 and 100.");
        }

        this.fk = fk;
    }

    public int getFL() {
        return this.fl;
    }

    public void setFL(final int fl) {

        if (fl < 0) {
            throw new RuntimeException("\"fl\" option must be 0 or more.");
        }

        this.fl = fl;
    }

    /**
     * �n�b�V���l����������臒l�ȏ�̌�����ꍇ�́C�X���C�X���_�Ƃ��Ȃ����߂̃I�v�V����
     */
    private int c;

    /**
     * ��͑Ώۃf�B���N�g�����w�肷�邽�߂̃I�v�V����
     */
    private String d;

    /**
     * ��͑Ώۃv���O���~���O������w�肷�邽�߂̃I�v�V����
     */
    private String l;

    /**
     * ��͌��ʂ��o�͂���t�@�C�����w�肷�邽�߂̃I�v�V����
     */
    private String o;

    /**
     * �ϐ��Q�Ƃ��o�b�N���[�h�X���C�X�̈ˑ��֌W�Ƃ��Ċ܂߂邩���w�肷�邽�߂̃I�v�V����
     */
    private boolean r;

    /**
     * �o�͂���R�[�h�N���[���̃T�C�Y�̉������w�肷�邽�߂̃I�v�V�����D
     * �傫���͕��̐���\���D
     */
    private int s;

    /**
     * �ϐ����p�̐��K�����x�����w�肷�邽�߂̃I�v�V����
     * 0: �ϐ��������̂܂܎g��
     * 1: �ϐ������^���ɐ��K������D
     * 2: �S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
     */
    private int pv;

    /**
     * (���\�b�h�܂��̓R���X�g���N�^)�Ăяo���̐��K�����x�����w�肷�邽�߂̃I�v�V����
     * 0: �Ăяo�����͂��̂܂܁C���������p����
     * 1: �Ăяo������Ԃ�l�̌^���ɕϊ�����C���������p����D
     * 2: �Ăяo������Ԃ�l�̌^���ɕϊ�����C�������͗p���Ȃ��D
     * 3: �S�Ă̌Ăяo���𓯈ꎚ��ɐ��K������D�������͗p���Ȃ�
     */
    private int pi;

    /**
     * �L���X�g�g�p�̐��K�����x�����w�肷�邽�߂̃I�v�V����
     * 0: �L���X�g�g�p�����̂܂ܗp����D
     * 1: �L���X�g�g�p���^�ɐ��K������D
     * 2: �S�ẴL���X�g�g�p�𓯈ꎚ��ɐ��K������D
     */
    private int pc;

    /**
     * 
     * �P�����Z�C�񍀉��Z�C�O�����Z�̐��K�����x�����w�肷�邽�߂̃I�v�V����
     * 0: ���Z�����̂܂ܗp����
     * 1: ���Z�����̌^�ɐ��K������
     * 2: �S�Ẳ��Z�𓯈�̎���ɐ��K������
     */
    private int po;

    /**
     *
     * ���e�����̐��K�����x�����w�肷�邽�߂̃I�v�V����
     * 0: ���e���������̂܂ܗp����
     * 1: ���e���������̌^�̐��K������
     * 2: �S�Ẵ��e�����𓯈�̎���ɐ��K������ 
     */
    private int pl;

    /**
     * 
     * �N���X�Q�Ɩ��𐳋K�����邽�߂̃I�v�V����
     * false: �N���X�Q�Ƃ͐��K�����Ȃ�
     * true: �S�ẴN���X�Q�Ƃ𓯈ꎚ��ɐ��K������
     */
    private boolean pr;

    /**
     * ���̃N���[���y�A�ɓ�����N���[���y�A���t�B���^�����O���邽�߂̃I�v�V����
     */
    private boolean fi;

    /**
     * �n�߂ƏI�肪��v���Ă���N���[���y�A���t�B���^�����O���邽�߂̃I�v�V����
     */
    private boolean fj;

    /**
     * 臒l�ȏ�I�[�o�[���b�v���Ă���N���[���y�A���t�B���^�����O���邽�߂̃I�v�V����
     */
    private int fk;

    /**
     * �R�[�h�T�C�Y��臒l�ȏ�قȂ�N���[���y�A���t�B���^�����O���邽�߂̃I�v�V����
     */
    private int fl;
}
