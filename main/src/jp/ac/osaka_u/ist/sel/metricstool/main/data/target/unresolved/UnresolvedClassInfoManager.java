package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * UnresolvedClassInfoManager ���Ǘ�����N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassInfoManager {

    /**
     * �N���X����ǉ�����
     * 
     * @param classInfo �N���X���
     */
    public void addClass(final UnresolvedClassInfo classInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
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
    public Collection<UnresolvedClassInfo> getClassInfos() {
        return Collections.unmodifiableCollection(this.classInfos.values());
    }

    /**
     * �N���X���� Map �ɕۑ����邽�߂̃L�[
     * 
     * @author higo
     * 
     */
    class ClassKey implements Comparable<ClassKey> {

        /**
         * �R���X�g���N�^�D�N���X�̊��S�C������^����
         * 
         * @param fullQualifiedName �N���X�̊��S�C����
         */
        ClassKey(final String[] fullQualifiedName) {

            // �s���ȌĂяo���łȂ������`�F�b�N
            MetricsToolSecurityManager.getInstance().checkAccess();
            if (null == fullQualifiedName) {
                throw new NullPointerException();
            }

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

            if (null == classKey) {
                throw new NullPointerException();
            }

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
         * 
         * @param o ��r�ΏۃI�u�W�F�N�g
         * @return �������ꍇ�� true�C�������Ȃ��ꍇ�� false
         */
        @Override
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            String[] fullQualifiedName = this.getFullQualifiedName();
            String[] correspondFullQualifiedName = ((ClassKey) o).getFullQualifiedName();

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
         * 
         * @return ���̃N���X�̃n�b�V���R�[�h
         */
        @Override
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
    public UnresolvedClassInfoManager() {
        this.classInfos = new HashMap<ClassKey, UnresolvedClassInfo>();
    }

    /**
     * UnresolvedClassInfo ��ۑ����邽�߂̃Z�b�g
     */
    private final Map<ClassKey, UnresolvedClassInfo> classInfos;
}
