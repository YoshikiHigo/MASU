package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * UnresolvedClassInfoManager �𑀍삷��N���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedClassInfoManager {

    /**
     * �P��I�u�W�F�N�g��Ԃ�
     */
    public static UnresolvedClassInfoManager getInstance() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return SINGLETON;
    }

    /**
     * �N���X����ǉ�����
     * 
     * @param classInfo �N���X���
     */
    public void addClass(final UnresolvedClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        ClassKey classKey = new ClassKey(classInfo.getFullQualifiedName());
        this.classInfos.put(classKey, classInfo);
    }

    /**
     * �N���X���̃Z�b�g��Ԃ�
     * 
     * @return �N���X���̃Z�b�g
     */
    public Collection<UnresolvedClassInfo> getUnresolvedClassInfos() {
        return Collections.unmodifiableCollection(this.classInfos.values());
    }

    /**
     * �N���X���� Map �ɕۑ����邽�߂̃L�[
     * 
     * @author y-higo
     * 
     */
    class ClassKey implements Comparable<ClassKey> {

        /**
         * �R���X�g���N�^�D�N���X�̊��S�C������^����
         * 
         * @param fullQualifiedName �N���X�̊��S�C����
         */
        ClassKey(final String[] fullQualifiedName) {
            this.fullQualifiedName = fullQualifiedName;
        }

        /**
         * �L�[��Ԃ�
         * 
         * @return �L�[�D �N���X�̊��S�C����
         */
        public String[] getFullQualifiedName() {
            return this.fullQualifiedName;
        }

        /**
         * �L�[�̏������`����
         */
        public int compareTo(final ClassKey classKey) {
            String[] fullQualifiedName = this.getFullQualifiedName();
            String[] correspondFullQualifiedName = classKey.getFullQualifiedName();

            if (fullQualifiedName.length > correspondFullQualifiedName.length) {
                return 1;
            } else if (fullQualifiedName.length < correspondFullQualifiedName.length) {
                return -1;
            } else {
                for (int i = 0; i < fullQualifiedName.length; i++) {
                    int order = fullQualifiedName[i].compareTo(correspondFullQualifiedName[i]);
                    if (order != 0) {
                        return order;
                    }
                }

                return 0;
            }
        }

        /**
         * ���̃N���X�ƑΏۃN���X�����������ǂ����𔻒肷��
         */
        public boolean equals(Object o) {

            String[] fullQualifiedName = this.getFullQualifiedName();
            String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o).getFullQualifiedName();

            if (fullQualifiedName.length != correspondFullQualifiedName.length) {
                return false;
            }

            for (int i = 0; i < fullQualifiedName.length; i++) {
                if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                    return false;
                }
            }

            return true;
        }

        /**
         * ���̃N���X�̃n�b�V���R�[�h��Ԃ�
         */
        public int hashCode() {

            StringBuffer buffer = new StringBuffer();
            String[] fullQualifiedName = this.getFullQualifiedName();
            for (int i = 0; i < fullQualifiedName.length; i++) {
                buffer.append(fullQualifiedName[i]);
            }

            return buffer.toString().hashCode();
        }

        private final String[] fullQualifiedName;
    }

    /**
     * �����Ȃ��R���X�g���N�^
     * 
     */
    private UnresolvedClassInfoManager() {
        this.classInfos = new HashMap<ClassKey, UnresolvedClassInfo>();
    }

    /**
     * �P��I�u�W�F�N�g��ۑ����邽�߂̕ϐ�
     */
    private final static UnresolvedClassInfoManager SINGLETON = new UnresolvedClassInfoManager();

    /**
     * UnresolvedClassInfo ��ۑ����邽�߂̃Z�b�g
     */
    private final Map<ClassKey, UnresolvedClassInfo> classInfos;
}
