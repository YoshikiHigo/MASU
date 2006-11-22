package jp.ac.osaka_u.ist.sel.metricstool.main.security;


import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collections;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * ���g���N�X�c�[���̃A�N�Z�X������X���b�h�P�ʂœ��I�ɍs���Z�L�����e�B�}�l�[�W��
 * <p>
 * �ŏ��� {@link #getInstance()} ���Ă񂾃X���b�h�ɑS�Ẵp�[�~�b�V�����������������^����D
 * ���̌�C�����������Ă���X���b�h�� {@link #addPrivilegeThread(Thread)} ���\�b�h��ʂ��ēo�^�����X���b�h�ɂ����l�̓�����^����D
 * ������^����ꂽ�X���b�h�̓����͍폜����Ȃ��D
 * <p>
 * ���̃N���X�̗��p�҂͓����������Ȃ��X���b�h����̃A�N�Z�X��r���������ꍇ�� {@link #checkAccess()} ���\�b�h���ĂԁD
 * �Ăяo�����X���b�h�������������Ȃ��ꍇ�́C {@link AccessControlException}�@��O���X���[�����D
 * ���������X���b�h�ł������ꍇ�́C���������ɏ�����Ԃ��D
 * <p>
 * �܂��C�����Ƃ͕ʂ̃A�N�Z�X�R���g���[���Ƃ��ăO���[�o���p�[�~�b�V�����Ƃ����T�O�������D
 * �O���[�o���p�[�~�b�V�����Ƃ̓v���O�C����GUI���܂�VM��̑S�ẴN���X�ɋ������p�[�~�b�V�����̂��ƂŁC
 * {@link #addGlobalPermission(Permission)}�ɂ���ēo�^���ꂽ�p�[�~�b�V�����́C
 * �S�ẴX���b�h�C�S�ẴR���e�L�X�g�C�S�ẴR�[�h�\�[�X�ɋ������D
 * �������C�O���[�o���p�[�~�b�V�����̒ǉ��͓����X���b�h�݂̂���\�ł���D
 * <p>
 * �����������Ȃ��X���b�h�ŁC�O���[�o���p�[�~�b�V�����ɓo�^����Ă��Ȃ����̂ɂ��ẮC
 * �ʏ�� {@link SecurityManager} �Ɠ����̋@�\��K�p����D
 * <p>
 * �V���O���g���N���X�ł��邽�߁C�R���X�g���N�^�� private �ł���C���̃N���X���p�����邱�Ƃ͂ł��Ȃ����C
 * ����𖾎��I�ɐ錾���邽�߂� final �C���q�����Ă���D
 * 
 * @author kou-tngt
 *
 */
public final class MetricsToolSecurityManager extends SecurityManager {

    /**
     * �V���O���g���C���X�^���X���擾����
     * @return �V���O���g���C���X�^���X
     */
    public static MetricsToolSecurityManager getInstance() {
        if (null == SINGLETON) {
            synchronized (MetricsToolSecurityManager.class) {
                if (null == SINGLETON) {
                    SINGLETON = new MetricsToolSecurityManager();
                }
            }
        }
        return SINGLETON;
    }

    /**
     * ���g���N�X�c�[�������s���Ă���VM��̑S�̂ŋ�����p�[�~�b�V������ǉ�����D
     * ���M���O�Ƃ���肽���ꍇ�́C������g���ēo�^����D
     * �o�^����ɂ͌Ăяo���X���b�h�ɓ������K�v
     * @param permission ���������p�[�~�b�V�����C���X�^���X
     * @throws AccessControlException �X���b�h�ɓ��ʌ������Ȃ��ꍇ
     * @throws NullPointerException permission��null�̏ꍇ
     */
    public final void addGlobalPermission(final Permission permission) {
        this.checkAccess();
        if (null == permission){
            throw new NullPointerException("permission is null.");
        }
        
        this.globalPermissions.add(permission);
    }

    /**
     * ���� thread �ŗ^����ꂽ�X���b�h�ɓ�����t�^����
     * @param thread ������t�^�������X���b�h
     * @throws AccessControlException �Ăяo�����̃X���b�h�������������Ă��Ȃ������ꍇ
     * @throws NullPointerException thread��null�������ꍇ
     */
    public final void addPrivilegeThread(final Thread thread) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("Added thread is null.");
        }
        
        this.privilegeThreads.add(thread);
    }

    /**
     * �����X���b�h����̌Ăяo�����ǂ����𔻒肷�郁�\�b�h�D
     * �����X���b�h�ȊO����Ăяo�����ƁC {@link AccessControlException}�@���X���[����D
     * @throws AccessControlException �����X���b�h�ȊO����Ăяo���ꂽ�ꍇ
     */
    public final void checkAccess() {
        //�J�����g�X���b�h���擾
        final Thread currentThread = Thread.currentThread();
        if (!this.isPrivilegeThread(currentThread)) {
            //�o�^����Ă��Ȃ�����

            //�G���[�\���p�ɃX�^�b�N�ƃ��[�X�̎擾
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //���̃��\�b�h�̌Ăяo�����̃��\�b�h���擾
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission)} ���\�b�h���I�[�o�[���C�h���C
     * �����X���b�h����̌Ăяo���C���邢�̓O���[�o���p�[�~�b�V�����Ƃ��ēo�^�ς݂ł���΁C
     * �p�[�~�b�V�����`�F�b�N�������ɏI������D
     * �����łȂ��Ȃ�C�e�N���X�̃��\�b�h���ĂсC�p�[�~�b�V�����̃`�F�b�N���s���D
     * @param perm �`�F�b�N����p�[�~�b�V����
     * @throws NullPointerException ����perm��null�̏ꍇ
     * @throws SecurityException �����X���b�h�ł��O���[�o���p�[�~�b�V�����ł��Ȃ��ꍇ�ɁC�p�[�~�b�V������������Ă��Ȃ��ꍇ
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    @Override
    public final void checkPermission(final Permission perm) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.isPrivilegeThread()) {
            return;
        } else if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm);
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission, Object)} ���\�b�h���I�[�o�[���C�h���C
     * �O���[�o���p�[�~�b�V�����Ƃ��ēo�^�ς݂ł���΃p�[�~�b�V�����`�F�b�N�������ɏI������D
     * �����łȂ��Ȃ�C�e�N���X�̃��\�b�h���ĂсC�p�[�~�b�V�����̃`�F�b�N���s���D
     * @param perm �`�F�b�N����p�[�~�b�V����
     * @throws NullPointerException ����perm��null�̏ꍇ
     * @throws SecurityException perm���O���[�o���p�[�~�b�V�����łȂ��ꍇ�ɁC�p�[�~�b�V������������Ă��Ȃ��ꍇ
     * @see java.lang.SecurityManager#checkPermission(Permission, Object)
     */
    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm, context);
        }
    }

    /**
     * �J�����g�X���b�h�������������Ă��邩��Ԃ�
     * @return �����������Ă����true
     */
    public final boolean isPrivilegeThread() {
        return this.isPrivilegeThread(Thread.currentThread());
    }

    /**
     * ���� thread �ŗ^����ꂽ�X���b�h�������������Ă��邩��Ԃ�
     * @param thread �����������Ă��邩�𒲂ׂ����X���b�h
     * @return ���� thread �ŗ^����ꂽ�X���b�h�������������Ă����true
     */
    public final boolean isPrivilegeThread(final Thread thread) {
        return this.privilegeThreads.contains(thread);
    }

    /**
     * �V���O���g���pprivate�R���X�g���N�^�D
     * �������Ăяo�����X���b�h�����������N���X�Ƃ��ēo�^����D
     */
    private MetricsToolSecurityManager() {
        final Thread thread = Thread.currentThread();
        assert (null != thread) : "Illegal state : current thread is null.";
        this.privilegeThreads.add(thread);
    }

    /**
     * �����X���b�h�̃Z�b�g�D
     * ���̑S�Ă̎Q�Ƃ��؂ꂽ������X���b�h�������Ă��Ă��Ӗ����Ȃ��̂ŁC��Q�Ƃɂ��邽�߂� {@link WeakHashSet} ��p����D
     * �܂��C�}���`�X���b�h���œK�؂ɓ��삳���邽�߂� {@link Collections#synchronizedSet(Set)} ���g���ē���������D
     */
    private final Set<Thread> privilegeThreads = Collections
            .synchronizedSet((new WeakHashSet<Thread>()));

    /**
     * �V���O���g���C���X�^���X�D
     */
    private static MetricsToolSecurityManager SINGLETON;

    /**
     * VM�S�̂ŋ�����p�[�~�b�V�����̃R���N�V����
     */
    private final Permissions globalPermissions = new Permissions();
}
