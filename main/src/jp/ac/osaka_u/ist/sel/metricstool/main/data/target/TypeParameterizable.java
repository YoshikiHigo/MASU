package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;
import java.util.Map;


/**
 * �^�p�����[�^����`�\�ł��邱�Ƃ�\���C���^�[�t�F�[�X
 */
public interface TypeParameterizable {

    /**
     * �^�p�����[�^��`��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^��`
     */
    void addTypeParameter(TypeParameterInfo typeParameter);

    /**
     * �����ŗ^����ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^���w�肷�邽�߂̃C���f�b�N�X
     * @return �����ŗ^����ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    TypeParameterInfo getTypeParameter(int index);

    /**
     * �^�p�����[�^�̃��X�g��Ԃ�
     * 
     * @return �^�p�����[�^�̃��X�g
     */
    List<TypeParameterInfo> getTypeParameters();

    /**
     * �^�p�����[�^�Ǝ��ۂɎg�p����Ă���^�̃y�A��ǉ�����
     * 
     * @param typeParameterInfo �^�p�����[�^
     * @param usedType ���ۂɎg�p����Ă���^
     */
    void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType);

    /**
     * �^�p�����[�^�Ǝ��ۂɎg�p����Ă���^�̃}�b�v��Ԃ�
     * 
     * @return �^�p�����[�^�Ǝ��ۂɎg�p����Ă���^�̃}�b�v
     */
    Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages();
}
