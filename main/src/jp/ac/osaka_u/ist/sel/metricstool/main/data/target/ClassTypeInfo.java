package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Q�ƌ^��\���N���X
 * 
 * @author higo
 * 
 */
public final class ClassTypeInfo implements ReferenceTypeInfo {

    /**
     * �Q�ƌ^��List���N���X��List�ɕϊ�����
     * 
     * @param references �Q�ƌ^��List
     * @return �N���X��List
     */
    public static List<ClassInfo> convert(final List<ClassTypeInfo> references) {

        final List<ClassInfo> classInfos = new LinkedList<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableList(classInfos);
    }

    /**
     * �Q�ƌ^��SortedSet���N���X��SortedSet�ɕϊ�����
     * 
     * @param references �Q�ƌ^��SortedSet
     * @return �N���X��SortedSet
     */
    public static SortedSet<ClassInfo> convert(final SortedSet<ClassTypeInfo> references) {

        final SortedSet<ClassInfo> classInfos = new TreeSet<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableSortedSet(classInfos);
    }

    /**
     * �Q�Ƃ����N���X��^���ď�����
     * 
     * @param referencedClass �Q�Ƃ����N���X
     */
    public ClassTypeInfo(final ClassInfo referencedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedClass) {
            throw new NullPointerException();
        }

        this.referencedClass = referencedClass;
        this.typeParameters = new ArrayList<ClassTypeInfo>();
    }

    /**
     * �����ŗ^����ꂽ�^�𓙂������ǂ������r�D
     * 
     * @return �������ꍇ��true�C�������Ȃ��ꍇ��false
     */
    @Override
    public boolean equals(TypeInfo typeInfo) {

        // ������ null �Ȃ�΁C�������Ȃ�
        if (null == typeInfo) {
            return false;
        }

        // �������Q�ƌ^�łȂ���΁C�������Ȃ�
        if (!(typeInfo instanceof ClassTypeInfo)) {
            return false;
        }

        // �������Q�ƌ^�̏ꍇ�C
        // �Q�Ƃ���Ă���N���X���������Ȃ��ꍇ�́C�Q�ƌ^�͓������Ȃ�
        final ClassTypeInfo targetReferenceType = (ClassTypeInfo) typeInfo;
        if (!this.referencedClass.equals(targetReferenceType)) {
            return false;
        }

        // �^�p�����[�^�̐����قȂ�ꍇ�́C�������Ȃ�
        final List<ClassTypeInfo> thisTypeParameters = this.typeParameters;
        final List<ClassTypeInfo> targetTypeParameters = targetReferenceType.getTypeParameters();
        if (thisTypeParameters.size() != targetTypeParameters.size()) {
            return false;
        }

        // �S�Ă̌^�p�����[�^���������Ȃ���΁C�������Ȃ�
        final Iterator<ClassTypeInfo> thisTypeParameterIterator = thisTypeParameters.iterator();
        final Iterator<ClassTypeInfo> targetTypeParameterIterator = targetTypeParameters.iterator();
        while (thisTypeParameterIterator.hasNext()) {
            final ClassTypeInfo thisTypeParameter = thisTypeParameterIterator.next();
            final ClassTypeInfo targetTypeParameter = targetTypeParameterIterator.next();
            if (!thisTypeParameter.equals(targetTypeParameter)) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���̎Q�ƌ^��\���������Ԃ�
     * 
     * @return ���̎Q�ƌ^��\��������
     */
    @Override
    public String getTypeName() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.referencedClass.getFullQualifiedName("."));

        if (0 <= this.typeParameters.size()) {
            sb.append("<");
            for (final ClassTypeInfo typeParameter : this.typeParameters) {
                sb.append(typeParameter.getTypeName());
            }
            sb.append(">");
        }

        return sb.toString();
    }

    /**
     * �Q�Ƃ���Ă���N���X��Ԃ�
     * 
     * @return �Q�Ƃ���Ă���N���X
     */
    public ClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * ���̎Q�ƌ^�ɗp�����Ă���^�p�����[�^�̃��X�g��Ԃ�
     * 
     * @return ���̎Q�ƌ^�ɗp�����Ă���^�p�����[�^�̃��X�g��Ԃ�
     */
    public List<ClassTypeInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���̎Q�ƌ^���\���N���X��ۑ����邽�߂̕ϐ�
     */
    private final ClassInfo referencedClass;

    /**
     * ���̎Q�ƌ^�̌^�p�����[�^��ۑ����邽�߂̕ϐ�
     */
    private final List<ClassTypeInfo> typeParameters;

}
