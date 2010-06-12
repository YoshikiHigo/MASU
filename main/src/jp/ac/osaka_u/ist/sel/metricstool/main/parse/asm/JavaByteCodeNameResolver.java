package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.NameResolver;


/**
 * �o�C�g�R�[�h���瓾�����������𖼑O�������邽�߂̃N���X
 * 
 * @author higo
 *
 */
public class JavaByteCodeNameResolver {

    public static String[] resolveName(final String unresolvedName) {

        if (null == unresolvedName) {
            throw new IllegalArgumentException();
        }

        final List<String> name = new LinkedList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(unresolvedName, "/$");
        while (tokenizer.hasMoreElements()) {
            name.add(tokenizer.nextToken());
        }
        return name.toArray(new String[0]);
    }

    /**
     * �������^���𖼑O��������N���X.
     * ���C��O�����́CTypeParameter����������ꍇ�̂ݎw�肷��΂悢.
     * �������C��������^�������ɃW�F�l���N�X���܂�ł���ꍇ������̂ŁC
     * ��O�����͂�����Ǝw�肷�邱�Ƃ��d�v
     * ## �������͍폜����܂���.
     * 
     * @param unresolvedType
     * @param thisTypeParameter �^�p�����[�^��extendsType����������Ƃ��̂ݕK�{
     * @param ownerUnit
     * 
     * @return
     */
    public static TypeInfo resolveType(final String unresolvedType,
            final TypeParameterInfo thisTypeParameter, final TypeParameterizable ownerUnit) {

        if (null == unresolvedType) {
            throw new IllegalArgumentException();
        }

        // �ꕶ���Ȃ�΁CprimitiveType��Void�łȂ���΂Ȃ�Ȃ�
        if (1 == unresolvedType.length()) {
            return translateSingleCharacterType(unresolvedType.charAt(0));
        }

        // '['�Ŏn�܂��Ă���Ƃ��͔z��
        else if ('[' == unresolvedType.charAt(0)) {
            final TypeInfo subType = resolveType(unresolvedType.substring(1), thisTypeParameter,
                    ownerUnit);

            // ���Ƃ��Ɣz��Ȃ�Ύ�����1���₷
            if (subType instanceof ArrayTypeInfo) {
                final ArrayTypeInfo subArrayType = (ArrayTypeInfo) subType;
                final TypeInfo ElementType = subArrayType.getElementType();
                final int dimension = subArrayType.getDimension();
                return ArrayTypeInfo.getType(ElementType, dimension + 1);
            }

            //�@�z��łȂ��Ȃ�z��ɂ���
            else {
                return ArrayTypeInfo.getType(subType, 1);
            }
        }

        // �z��łȂ��Q�ƌ^�̏ꍇ
        else if (('L' == unresolvedType.charAt(0))
                && (';' == unresolvedType.charAt(unresolvedType.length() - 1))) {

            final ClassInfoManager classInfoManager = DataManager.getInstance()
                    .getClassInfoManager();
            final String unresolvedReferenceType = unresolvedType.substring(1, unresolvedType
                    .length() - 1);

            // �W�F�l���N�X���Ȃ��ꍇ
            if ((-1 == unresolvedReferenceType.indexOf('<'))
                    && (-1 == unresolvedReferenceType.lastIndexOf('>'))) {

                final String[] referenceTypeName = resolveName(unresolvedReferenceType);
                ExternalClassInfo referenceClass = (ExternalClassInfo) classInfoManager
                        .getClassInfo(referenceTypeName);
                if (null == referenceClass) {
                    referenceClass = new ExternalClassInfo(referenceTypeName);
                    classInfoManager.add(referenceClass);
                }
                return new ClassTypeInfo(referenceClass);

            }

            //�@�W�F�l���N�X������ꍇ
            else if ((0 <= unresolvedReferenceType.indexOf('<'))
                    && (0 <= unresolvedReferenceType.lastIndexOf('>'))) {

                final String[] referenceTypeName = resolveName(unresolvedReferenceType.substring(0,
                        unresolvedReferenceType.indexOf('<')));

                // �N���X���̕���������
                ExternalClassInfo referenceClass = (ExternalClassInfo) classInfoManager
                        .getClassInfo(referenceTypeName);
                if (null == referenceClass) {
                    referenceClass = new ExternalClassInfo(referenceTypeName);
                    classInfoManager.add(referenceClass);
                }
                final ClassTypeInfo referenceType = new ClassTypeInfo(referenceClass);

                //�W�F�l���N�X�������������C�����N���X�Q�Ƃɂ��̏���ǉ�                
                final String[] typeArguments = getTypeArguments(unresolvedReferenceType);
                for (int i = 0; i < typeArguments.length; i++) {
                    final TypeInfo type = resolveType(typeArguments[i], thisTypeParameter,
                            ownerUnit);
                    referenceType.addTypeArgument(type);
                }

                return referenceType;

            } else {
                throw new IllegalStateException();
            }
        }

        // �W�F�l���N�X(TE(�ʂ�E����Ȃ��Ă���������);)�̏ꍇ
        else if (('T' == unresolvedType.charAt(0)) && (-1 == unresolvedType.indexOf(':'))
                && ';' == unresolvedType.charAt(unresolvedType.length() - 1)) {

            final String identifier = unresolvedType.substring(1, unresolvedType.length() - 1);
            if ((null != thisTypeParameter) && identifier.equals(thisTypeParameter.getName())) {
                return new TypeParameterTypeInfo(thisTypeParameter);
            }
            final List<TypeParameterInfo> availableTypeParameters = NameResolver
                    .getAvailableTypeParameters(ownerUnit);
            for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                if (identifier.equals(typeParameter.getName())) {
                    return new TypeParameterTypeInfo(typeParameter);
                }
            }
        }

        // �W�F�l���N�X(-TE;)�̏ꍇ
        else if (('-' == unresolvedType.charAt(0)) && ('T' == unresolvedType.charAt(1))
                && (-1 == unresolvedType.indexOf(':'))
                && (';' == unresolvedType.charAt(unresolvedType.length() - 1))) {

            // TODO super �̑O�̏��𖳎����Ă���D�ǉ������̕K�v����
            final String identifier = unresolvedType.substring(2, unresolvedType.length() - 1);
            if ((null != thisTypeParameter) && identifier.equals(thisTypeParameter.getName())) {
                return new TypeParameterTypeInfo(thisTypeParameter);
            }
            final List<TypeParameterInfo> availableTypeParameters = NameResolver
                    .getAvailableTypeParameters(ownerUnit);
            for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                if (identifier.equals(typeParameter.getName())) {
                    return new TypeParameterTypeInfo(typeParameter);
                }
            }
        }

        // �W�F�l���N�X(+TE;)�̏ꍇ
        else if (('+' == unresolvedType.charAt(0)) && ('T' == unresolvedType.charAt(1))
                && (-1 == unresolvedType.indexOf(':'))
                && (';' == unresolvedType.charAt(unresolvedType.length() - 1))) {

            // TODO extends �̑O�̏��𖳎����Ă���D�ǉ������̕K�v����
            final String identifier = unresolvedType.substring(2, unresolvedType.length() - 1);
            if ((null != thisTypeParameter) && identifier.equals(thisTypeParameter.getName())) {
                return new TypeParameterTypeInfo(thisTypeParameter);
            }
            final List<TypeParameterInfo> availableTypeParameters = NameResolver
                    .getAvailableTypeParameters(ownerUnit);
            for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                if (identifier.equals(typeParameter.getName())) {
                    return new TypeParameterTypeInfo(typeParameter);
                }
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * �������^�p�����[�^���𖼑O��������N���X.
     * ���C��O�����́CTypeParameter����������ꍇ�̂ݎw�肷��΂悢.
     * �������C��������^�������ɃW�F�l���N�X���܂�ł���ꍇ������̂ŁC
     * ��O�����͂�����Ǝw�肷�邱�Ƃ��d�v
     * 
     * @param unresolvedType
     * @param index
     * @param ownerUnit
     * @return
     */
    public static TypeParameterInfo resolveTypeParameter(final String unresolvedType,
            final int index, final TypeParameterizable ownerUnit) {

        if (null == unresolvedType) {
            throw new IllegalArgumentException();
        }

        // �W�F�l���N�X(TE(�ʂ�E����Ȃ��Ă���������);)�̏ꍇ
        if ('T' == unresolvedType.charAt(0) && (-1 == unresolvedType.indexOf(':'))
                && ';' == unresolvedType.charAt(unresolvedType.length() - 1)) {

            final String identifier = unresolvedType.substring(1, unresolvedType.length() - 1);
            return new TypeParameterInfo(ownerUnit, identifier, index, null);
        }

        // �W�F�l���N�X(+...;)�̏ꍇ
        else if ('+' == unresolvedType.charAt(0)) {

            final String unresolvedExtendsType = unresolvedType.substring(1);
            final TypeInfo extendsType = resolveType(unresolvedExtendsType, null, ownerUnit);
            return new TypeParameterInfo(ownerUnit, "?", index, extendsType);
        }

        // �W�F�l���N�X(-...;)�̏ꍇ
        else if ('-' == unresolvedType.charAt(0)) {

            final String unresolvedSuperType = unresolvedType.substring(1);
            final TypeInfo superType = resolveType(unresolvedSuperType, null, ownerUnit);
            return new SuperTypeParameterInfo(ownerUnit, "?", index, null, superType);
        }

        // �W�F�l���N�X(T:Ljava/lang/Object;)�̏ꍇ
        else if ((-1 != unresolvedType.indexOf(':'))
                && (';' == unresolvedType.charAt(unresolvedType.length() - 1))) {

            final String identifier = unresolvedType.substring(0, unresolvedType.indexOf(':'));
            final TypeParameterInfo typeParameter = new TypeParameterInfo(ownerUnit, identifier,
                    index);
            final String unresolvedExtendsType = unresolvedType.substring(unresolvedType
                    .lastIndexOf(':') + 1);
            final TypeInfo extendsType = resolveType(unresolvedExtendsType, typeParameter,
                    ownerUnit);
            typeParameter.setExtendsType(extendsType);
            return typeParameter;
        }

        throw new IllegalArgumentException();
    }

    private static TypeInfo translateSingleCharacterType(final char c) {

        switch (c) {
        case 'Z':
            return PrimitiveTypeInfo.BOOLEAN;
        case 'C':
            return PrimitiveTypeInfo.CHAR;
        case 'B':
            return PrimitiveTypeInfo.BYTE;
        case 'S':
            return PrimitiveTypeInfo.SHORT;
        case 'I':
            return PrimitiveTypeInfo.INT;
        case 'F':
            return PrimitiveTypeInfo.FLOAT;
        case 'J':
            return PrimitiveTypeInfo.LONG;
        case 'D':
            return PrimitiveTypeInfo.DOUBLE;
        case 'V':
            return VoidTypeInfo.getInstance();
        default:
            throw new IllegalArgumentException();
        }
    }

    private static String[] getTypeArguments(final String type) {

        final List<String> typeArguments = new LinkedList<String>();

        int fromIndex = 0;
        int toIndex = 0;
        int nestLevel = 0;
        for (int index = 0; index < type.length(); index++) {

            // '<'�����������̏���
            if ('<' == type.charAt(index)) {
                nestLevel += 1;
                if (1 == nestLevel) {
                    fromIndex = index + 1;
                }
            }

            // '>'�����������̏���
            else if ('>' == type.charAt(index)) {
                nestLevel -= 1;
            }

            // ';'�����������̏���
            else if (';' == type.charAt(index)) {

                if (1 == nestLevel) {
                    toIndex = index + 1;
                    typeArguments.add(type.substring(fromIndex, toIndex));
                    fromIndex = index + 1;
                }
            }

            // '*'�@�����������̏���
            else if ('*' == type.charAt(index)) {

                if (1 == nestLevel) {
                    typeArguments.add("*");
                    fromIndex = index + 1;
                    toIndex = index + 1;
                }
            }
        }

        if (0 != nestLevel) {
            throw new IllegalStateException();
        }

        return typeArguments.toArray(new String[0]);
    }
}
