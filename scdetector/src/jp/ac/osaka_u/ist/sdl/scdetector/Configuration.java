package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.c = 50;
        this.d = null;
        this.l = null;
        this.o = null;
        this.r = true;
        this.pv = 1;
        this.pm = 2;
        this.pc = 2;
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

    public int getPM() {
        return this.pm;
    }

    public void setPM(final int pm) {

        if ((pm < 0) || (3 < pm)) {
            throw new RuntimeException("\"pm\" option must be between 0 and 3.");
        }
        this.pm = pm;
    }

    public int getPC() {
        return this.pc;
    }

    public void setPC(final int pc) {

        if ((pc < 0) || (3 < pc)) {
            throw new RuntimeException("\"pc\" option must be between 0 and 3.");
        }
        this.pc = pc;
    }

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
     */
    private int pv;

    /**
     * ���\�b�h�Ăяo���̐��K�����x�����w�肷�邽�߂̃I�v�V����
     */
    private int pm;

    /**
     * �R���X�g���N�^�Ăяo���̐��K�����x�����w�肷�邽�߂̃I�v�V����
     */
    private int pc;

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
