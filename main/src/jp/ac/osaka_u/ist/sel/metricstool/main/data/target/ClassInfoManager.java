package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�����Ǘ�����N���X�D
 * 
 * @author y-higo
 * 
 */
public final class ClassInfoManager {

    /**
     * �N���X�����Ǘ����Ă���C���X�^���X��Ԃ��D �V���O���g���p�^�[���������Ă���D
     * 
     * @return �N���X�����Ǘ����Ă���C���X�^���X
     */
    public static ClassInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * �ΏۃN���X��ǉ�����
     * 
     * @param classInfo �ǉ�����N���X���
     */
    public void add(final TargetClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.targetClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);
    }

    /**
     * �O���N���X��ǉ�����
     * 
     * @param classInfo �ǉ�����N���X���
     */
    public void add(final ExternalClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.externalClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);
    }

    /**
     * �ΏۃN���X��SortedSet��Ԃ�
     * 
     * @return �ΏۃN���X��SortedSet
     */
    public SortedSet<TargetClassInfo> getTargetClassInfos() {
        return Collections.unmodifiableSortedSet(this.targetClassInfos);
    }

    /**
     * �O���N���X��SortedSet��Ԃ�
     * 
     * @return �O���N���X��SortedSet
     */
    public SortedSet<ExternalClassInfo> getExternalClassInfos() {
        return Collections.unmodifiableSortedSet(this.externalClassInfos);
    }

    /**
     * �ΏۃN���X�̐���Ԃ�
     * 
     * @return �ΏۃN���X�̐�
     */
    public int getTargetClassCount() {
        return this.targetClassInfos.size();
    }

    /**
     * �O���N���X�̐���Ԃ�
     * 
     * @return �O���N���X�̐�
     */
    public int getExternalClassCount() {
        return this.targetClassInfos.size();
    }

    /**
     * �����Ŏw�肵�����S���薼�����N���X����Ԃ�
     * 
     * @param fullQualifiedName ���S���薼
     * @return �N���X���
     */
    public ClassInfo getClassInfo(final String[] fullQualifiedName) {
        
        if (null == fullQualifiedName){
            throw new NullPointerException();
        }
        
        return this.packageInfo.getClassInfo(fullQualifiedName);
    }

    public Collection<ClassInfo> getClassInfos(final String[] namespace) {
        
        if (null == namespace){
            throw new NullPointerException();
        }
        
        return this.packageInfo.getClassInfos(namespace);
    }
    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    private ClassInfoManager() {
        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();
        this.packageInfo = new PackageInfo("DEFAULT", 0);
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static final ClassInfoManager SINGLETON = new ClassInfoManager();

    /**
     * 
     * �ΏۃN���X���(TargetClassInfo)���i�[����ϐ��D
     */
    private final SortedSet<TargetClassInfo> targetClassInfos;

    /**
     * �O���N���X���iExternalClassInfo�j���i�[����ϐ�
     */
    private final SortedSet<ExternalClassInfo> externalClassInfos;

    /**
     * �N���X�����K�w�\���ŕۂ��߂̕ϐ�
     */
    private final PackageInfo packageInfo;

    /**
     * �N���X�ꗗ���K�w�I���O��ԁi�p�b�P�[�W�K�w�j�Ŏ��f�[�^�N���X
     * 
     * @author y-higo
     */
    class PackageInfo {

        /**
         * ���O��Ԃ��������D���O��Ԗ��Ɛ[����^����D�f�t�H���g�p�b�P�[�W��0�D
         * 
         * @param packageName ���O��Ԗ�
         */
        PackageInfo(final String packageName, final int depth) {
            
            if(null == packageName){
                throw new NullPointerException();
            }
            if(depth < 0){
                throw new IllegalArgumentException("Depth must be 0 or more!");
            }
            
            this.packageName = packageName;
            this.depth = depth;
            this.classInfos = new TreeMap<String, ClassInfo>();
            this.subPackages = new TreeMap<String, PackageInfo>();
        }

        /**
         * �����Ŏw�肳�ꂽ�N���X����ǉ�����
         * 
         * @param classInfo �ǉ�����N���X���
         */
        void add(final ClassInfo classInfo) {

            if (null == classInfo) {
                throw new NullPointerException();
            }

            String[] packageNames = classInfo.getNamespace().getName();

            // �ǉ�����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�����[���ꍇ�́C�Y������T�u���O��Ԃ��Ăяo��
            if (this.getDepth() < packageNames.length) {
                PackageInfo subPackage = this.subPackages.get(packageNames[this.getDepth()]);
                if (null == subPackage) {
                    subPackage = new PackageInfo(packageNames[this.getDepth()], this.getDepth() + 1);
                    this.subPackages.put(subPackage.getPackageName(), subPackage);
                }
                subPackage.add(classInfo);

                // �ǉ�����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�Ɠ����ꍇ�́C���̖��O��ԂɃN���X����ǉ�����
            } else if (this.getDepth() == packageNames.length) {
                this.classInfos.put(classInfo.getName(), classInfo);

                // �ǉ�����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�����󂢏ꍇ�́C�G���[
            } else {
                throw new IllegalArgumentException("Illegal class Info: " + classInfo.toString());
            }
        }

        /**
         * ���̖��O��Ԃ̐[����Ԃ�
         * 
         * @return ���̖��O��Ԃ̐[��
         */
        int getDepth() {
            return this.depth;
        }

        /**
         * ���̖��O��Ԗ���Ԃ�
         * 
         * @return ���̖��O��Ԗ�
         */
        String getPackageName() {
            return this.packageName;
        }

        /**
         * �����ŗ^����ꂽ Full Qualified Name �����N���X��Ԃ�
         * 
         * @param fullQualifiedName �ق����N���X���� Full Qualified Name
         * @return �N���X���
         */
        ClassInfo getClassInfo(final String[] fullQualifiedName) {

            if (null == fullQualifiedName){
                throw new NullPointerException();
            }
            
            int namespaceLength = fullQualifiedName.length - 1;

            // �ق����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�����[���ꍇ�́C�Y������T�u���O��Ԃ��Ăяo��
            if (this.getDepth() < namespaceLength) {
                PackageInfo subPackage = this.subPackages.get(fullQualifiedName[this.getDepth()]);
                if (null == subPackage) {
                    return null;
                } else {
                    return subPackage.getClassInfo(fullQualifiedName);
                }

                // �ق����N���X���̖��O��ԑw���C���̖��O��ԊK�w�Ɠ����ꍇ�́C�N���X����Ԃ�
            } else if (this.getDepth() == namespaceLength) {
                ClassInfo classInfo = this.classInfos.get(fullQualifiedName[namespaceLength]);
                return classInfo;

                // �ق����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�����󂢏ꍇ�́C�G���[
            } else {
                throw new IllegalArgumentException("Illegal full qualified name: "
                        + fullQualifiedName.toString());
            }
        }
        
        Collection<ClassInfo> getClassInfos(final String[] namespace){
            
            if (null == namespace){
                throw new NullPointerException();
            }

            //�ق����N���X���̖��O��ԑw���C���̖��O��ԑw�����[���ꍇ�́C�Y������T�u���O��Ԗ����Ăяo��
            if (this.getDepth() < namespace.length){
                PackageInfo subPackage = this.subPackages.get(namespace[this.getDepth()]);
                if (null == subPackage) {
                    return Collections.unmodifiableCollection(new HashSet<ClassInfo>());
                }else{
                    return subPackage.getClassInfos(namespace);
                }
            
                // �ق����N���X���̖��O��ԑw���C���̖��O��ԑw�Ɠ����ꍇ�́C�N���X����Ԃ�
            }else if (this.getDepth() == namespace.length){
                
                return Collections.unmodifiableCollection(this.classInfos.values());
                //�ق����N���X���̖��O��ԊK�w���C���̖��O��ԊK�w�����󂢏ꍇ�́C�G���[
            }else{
                throw new IllegalArgumentException("Illegal namepace: "
                        + namespace.toString());
            }
        }

        /**
         * ���̃p�b�P�[�W�̖��O��ۑ�����
         */
        private final String packageName;

        /**
         * ���̃p�b�P�[�W�̐[���D �f�t�H���g�p�b�P�[�W��0�ł���D
         */
        private final int depth;

        /**
         * ���̃p�b�P�[�W�ɂ���N���X�ꗗ��ۑ�����
         */
        private final SortedMap<String, ClassInfo> classInfos;

        /**
         * ���̃p�b�P�[�W�̃T�u�p�b�P�[�W�ꗗ��ۑ�����
         */
        private final SortedMap<String, PackageInfo> subPackages;
    }
}
