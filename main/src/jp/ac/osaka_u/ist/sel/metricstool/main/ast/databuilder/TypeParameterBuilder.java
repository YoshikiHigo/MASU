package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;


/**
 * �^�p�����[�^�����\�z����r���_�[
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class TypeParameterBuilder extends CompoundDataBuilder<UnresolvedTypeParameterInfo> {

    /**
     * �����ɗ^����ꂽ�\�z�f�[�^�̊Ǘ��҂ƃf�t�H���g�̖��O���r���_�[�C�^���r���_�[��p���ď���������
     * @param buildDataManager�@�\�z�f�[�^�̊Ǘ���
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager) {
        this(buildDataManager, new NameBuilder(), new TypeBuilder(buildDataManager));
    }

    /**
     * �����ɗ^����ꂽ�\�z�f�[�^�̊Ǘ��ҁC���O���r���_�[�C�^���r���_�[��p���ď���������
     * @param buildDataManager�@�\�z�f�[�^�̊Ǘ���
     * @param nameBuilder�@���O���r���_�[
     * @param typeBuilder�@�^���r���_�[
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager, final NameBuilder nameBuilder,
            final TypeBuilder typeBuilder) {
        if (null == buildDataManager) {
            throw new NullPointerException("buildDataManager is null.");
        }

        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        if (null == typeBuilder) {
            throw new NullPointerException("typeBuilder is null.");
        }

        this.buildDataManager = buildDataManager;
        this.nameBuilder = nameBuilder;
        this.typeBuilder = typeBuilder;

        //�����r���_�[�̓o�^
        this.addInnerBuilder(nameBuilder);
        this.addInnerBuilder(typeBuilder);

        //��Ԓʒm���󂯎�肽�����̂�o�^
        this.addStateManager(new TypeParameterStateManager());
    }

    /**
     * ��ԕω��C�x���g�̒ʒm���󂯂郁�\�b�h�D
     * @param event ��ԕω��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChangend(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (this.isActive()) {
            if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF)) {
                //�^�p�����[�^��`�ɓ������̂ł���΂�
                this.nameBuilder.activate();
                this.typeBuilder.activate();
                this.inTypeParameterDefinition = true;
            } else if (type
                    .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF)) {
                //�^�p�����[�^��`���I������̂ŁC�f�[�^���\�z���Č�n��
                this.buildTypeParameter();

                this.nameBuilder.deactivate();
                this.typeBuilder.deactivate();
                this.lowerBoundsType = null;
                this.upperBoundsType = null;
                this.nameBuilder.clearBuiltData();
                this.typeBuilder.clearBuiltData();
                this.inTypeParameterDefinition = false;

            } else if (this.inTypeParameterDefinition) {
                //�^�p�����[�^��`���Ȃ��ł̏o����
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS)) {
                    //�^�̉����錾�������̂Ō^�\�z��������΂�
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)) {
                    //�^�̉��������\�z����
                    this.lowerBoundsType = this.builtTypeBounds();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS)) {
                    //�^�̏���錾�������̂ō\�z��������΂�
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                    //�^�̏�������\�z����
                    this.upperBoundsType = this.builtTypeBounds();
                }
            }
        }
    }

    /**
     * �^�p�����[�^��`���̏I�����ɌĂяo����C�^�p�����[�^���\�z���郁�\�b�h
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�^�p�����[�^��`���ŔC�ӂ̏������s�킹�邱�Ƃ��ł���D
     */
    protected void buildTypeParameter() {
        //�^�p�����[�^�̖��O�������������炨������
        assert (this.nameBuilder.getBuiltDataCount() == 1);

        final String[] name = this.nameBuilder.getFirstBuiltData();
        
        //�^�p�����[�^�̖��O�������ɕ�����ĂĂ���������
        assert (name.length == 1);

        //�^�̏����񉺌������擾
        final UnresolvedTypeInfo upperBounds = this.getUpperBounds();
        final UnresolvedTypeInfo lowerBounds = this.getLowerBounds();

        UnresolvedTypeParameterInfo parameter = null;
        if (null == lowerBounds) {
            //�������Ȃ���Ε��ʂɍ��
            parameter = new UnresolvedTypeParameterInfo(name[0], upperBounds);
        } else {
            //����������ꍇ�͂����������
            parameter = new UnresolvedSuperTypeParameterInfo(name[0], upperBounds, lowerBounds);
        }

        //�Ō�Ƀf�[�^�Ǘ��҂ɓo�^����
        this.buildDataManager.addTypeParameger(parameter);
    }

    /**
     * �^�̏������Ԃ��D
     * @return�@�^�̏�����
     */
    protected UnresolvedTypeInfo getUpperBounds() {
        return this.upperBoundsType;
    }

    /**
     * �^�̉�������Ԃ��D
     * @return�@�^�̉������
     */
    protected UnresolvedTypeInfo getLowerBounds() {
        return this.lowerBoundsType;
    }

    /**
     * �Ō�ɍ\�z���ꂽ�^�̏���Ԃ��D
     * @return�@�Ō�ɍ\�z���ꂽ�^
     */
    protected UnresolvedTypeInfo builtTypeBounds() {
        return this.typeBuilder.getLastBuildData();
    }

    /**
     * ���O�\�z���s���r���_�[��Ԃ��D
     * @return�@���O�\�z���s���r���_�[
     */
    protected NameBuilder getNameBuilder() {
        return this.nameBuilder;
    }

    /**
     * �^�����\�z����r���_�[��Ԃ�
     * @return�@�^�����\�z����r���_�[
     */
    protected TypeBuilder getTypeBuilder() {
        return this.typeBuilder;
    }

    /**
     * ���O�\�z���s���r���_�[
     */
    private final NameBuilder nameBuilder;

    /**
     * �^�����\�z����r���_�[
     */
    private final TypeBuilder typeBuilder;

    /**
     * �\�z���̊Ǘ���
     */
    private final BuildDataManager buildDataManager;

    /**
     * �^�p�����[�^�̏��
     */
    private UnresolvedTypeInfo upperBoundsType;

    /**
     * �^�p�����[�^�̉���
     */
    private UnresolvedTypeInfo lowerBoundsType;

    /**
     * �^�p�����[�^��`���ɂ��邩�ǂ�����\��
     */
    private boolean inTypeParameterDefinition = false;

}
