package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �N���X�����Ǘ�����N���X�D
 * 
 * @author higo
 * 
 */
public final class ClassInfoManager {

    /**
     * �ΏۃN���X��ǉ�����
     * 
     * @param classInfo �ǉ�����N���X���
     * @return �����N���X��ǉ������ꍇ�� true,���Ȃ������ꍇ��false
     */
    public boolean add(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        // ��d�o�^�`�F�b�N
        if (this.targetClassInfos.contains(classInfo)) {
            err.println(classInfo.getFullQualifiedName(".") + " is already registered!");
            return false;
        } else if (this.externalClassInfos.contains(classInfo)) {
            // �O���N���X�Əd�����Ă���ꍇ�̓G���[�o�͂��Ȃ�
            return false;
        }

        // �N���X�ꗗ�̃Z�b�g�ɓo�^
        if (classInfo instanceof TargetClassInfo) {
            this.targetClassInfos.add((TargetClassInfo) classInfo);
        } else if (classInfo instanceof ExternalClassInfo) {
            this.externalClassInfos.add((ExternalClassInfo) classInfo);
        } else {
            assert false : "Here shouldn't be reached!";
        }

        // �N���X������N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v�ɒǉ�
        {
            final String name = classInfo.getClassName();
            SortedSet<ClassInfo> classInfos = this.classNameMap.get(name);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.classNameMap.put(name, classInfos);
            }
            classInfos.add(classInfo);
        }

        //�@���O��Ԃ���N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v�ɒǉ�
        {
            final NamespaceInfo namespace = classInfo.getNamespace();
            SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.namespaceMap.put(namespace, classInfos);
            }
            classInfos.add(classInfo);
        }

        return true;
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

        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }

        final int namespaceLength = fullQualifiedName.length - 1;
        final String[] namespace = Arrays.<String> copyOf(fullQualifiedName,
                fullQualifiedName.length - 1);
        final String className = fullQualifiedName[namespaceLength];

        // �����N���X�������N���X�ꗗ���擾
        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        if (null != classInfos) {
            // ���O��Ԃ��������N���X��Ԃ�
            for (final ClassInfo classInfo : classInfos) {
                if (classInfo.getNamespace().equals(namespace)) {
                    return classInfo;
                }
            }

            // �����ɗ���͓̂o�^����Ă��Ȃ��N���X�̊��S���薼���w�肳�ꂽ�Ƃ�
            // �O���N���X�Ƃ��ăI�u�W�F�N�g�𐶐����C�o�^����
            final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
            this.add(classInfo);
            return classInfo;

        } else {

            // �����ɗ���͓̂o�^����Ă��Ȃ��N���X�̊��S���薼���w�肳�ꂽ�Ƃ�
            // �O���N���X�Ƃ��ăI�u�W�F�N�g�𐶐����C�o�^����
            final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
            this.add(classInfo);
            return classInfo;
        }
    }

    /**
     * �����Ŏw�肵�����O��Ԃ����N���X���� Collection ��Ԃ�
     * 
     * @param namespace ���O���
     * @return �����Ŏw�肵�����O��Ԃ����N���X���� Collection
     */
    public Collection<ClassInfo> getClassInfos(final String[] namespace) {

        if (null == namespace) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.namespaceMap.get(new NamespaceInfo(namespace));
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    /**
     * �����Ŏw�肵���N���X�������N���X���� Collection ��Ԃ�
     * 
     * @param className �N���X��
     * @return �����Ŏw�肵���N���X�������N���X���� Collection
     */
    public Collection<ClassInfo> getClassInfos(final String className) {

        if (null == className) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * 
     * �R���X�g���N�^�D 
     */
    public ClassInfoManager() {

        this.classNameMap = new HashMap<String, SortedSet<ClassInfo>>();
        this.namespaceMap = new HashMap<NamespaceInfo, SortedSet<ClassInfo>>();

        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();

        // java����̏ꍇ�́C�ÖقɃC���|�[�g�����N���X��ǉ����Ă���
        if (Settings.getLanguage().equals(LANGUAGE.JAVA15)
                || Settings.getLanguage().equals(LANGUAGE.JAVA14)
                || Settings.getLanguage().equals(LANGUAGE.JAVA13)) {
            for (int i = 0; i < ExternalClassInfo.JAVA_PREIMPORTED_CLASSES.length; i++) {
                this.add(ExternalClassInfo.JAVA_PREIMPORTED_CLASSES[i]);
            }
        }
    }

    /**
     * �N���X������C�N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v
     */
    private final Map<String, SortedSet<ClassInfo>> classNameMap;

    /**
     * ���O��Ԗ�����C�N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v
     */
    private final Map<NamespaceInfo, SortedSet<ClassInfo>> namespaceMap;

    /**
     * �ΏۃN���X�ꗗ��ۑ����邽�߂̃Z�b�g
     */
    private final SortedSet<TargetClassInfo> targetClassInfos;

    /**
     * �O���N���X�ꗗ��ۑ����邽�߂̃Z�b�g
     */
    private final SortedSet<ExternalClassInfo> externalClassInfos;
}
