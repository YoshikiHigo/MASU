package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.TypeConverter;


/**
 * �j�����Z�g�p��\���N���X
 * 
 * @author higo
 * 
 */
public class BinominalOperationInfo extends EntityUsageInfo {

    /**
     * �j�����Z�̊e�I�y�����h�C�I�y���[�^��^���ăI�u�W�F�N�g��������
     * 
     * @param operator �I�y���[�^
     * @param firstOperand ���I�y�����h
     * @param secondOperand ���I�y�����h
     */
    public BinominalOperationInfo(final OPERATOR operator, final EntityUsageInfo firstOperand,
            final EntityUsageInfo secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;

    }

    @Override
    public TypeInfo getType() {

        final ExternalClassInfo DOUBLE = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.DOUBLE);
        final ExternalClassInfo FLOAT = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.FLOAT);
        final ExternalClassInfo LONG = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.LONG);
        final ExternalClassInfo INTEGER = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.INT);
        final ExternalClassInfo SHORT = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.SHORT);
        final ExternalClassInfo CHARACTER = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.CHAR);
        final ExternalClassInfo BYTE = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.BYTE);
        final ExternalClassInfo BOOLEAN = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.BOOLEAN);

        switch (Settings.getLanguage()) {
        case JAVA:

            final ExternalClassInfo STRING = TypeConverter.getTypeConverter(Settings.getLanguage())
                    .getWrapperClass(PrimitiveTypeInfo.STRING);

            switch (this.getOperator()) {
            case ARITHMETIC:

                if ((this.getFirstOperand().getType().equals(STRING) || (this.getSecondOperand()
                        .getType().equals(STRING)))) {
                    return STRING;

                } else if (this.getFirstOperand().getType().equals(DOUBLE)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.DOUBLE)
                        || this.getSecondOperand().getType().equals(DOUBLE)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.DOUBLE)) {
                    return PrimitiveTypeInfo.DOUBLE;

                } else if (this.getFirstOperand().getType().equals(FLOAT)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.FLOAT)
                        || this.getSecondOperand().getType().equals(FLOAT)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.FLOAT)) {
                    return PrimitiveTypeInfo.FLOAT;

                } else if (this.getFirstOperand().getType().equals(LONG)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.LONG)
                        || this.getSecondOperand().getType().equals(LONG)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (this.getFirstOperand().getType().equals(INTEGER)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.INT)
                        || this.getSecondOperand().getType().equals(INTEGER)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (this.getFirstOperand().getType().equals(SHORT)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.SHORT)
                        || this.getSecondOperand().getType().equals(SHORT)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;

                } else if (this.getFirstOperand().getType().equals(CHARACTER)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.CHAR)
                        || this.getSecondOperand().getType().equals(CHARACTER)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.CHAR)) {
                    return PrimitiveTypeInfo.CHAR;

                } else if (this.getFirstOperand().getType().equals(BYTE)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.BYTE)
                        || this.getSecondOperand().getType().equals(BYTE)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if ((this.getFirstOperand().getType() instanceof UnknownTypeInfo)
                        || (this.getSecondOperand().getType() instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();

                } else {
                    assert false : "Here shouldn't be reached!";
                }

                break;

            case COMPARATIVE:
                return PrimitiveTypeInfo.BOOLEAN;
            case LOGICAL:
                return PrimitiveTypeInfo.BOOLEAN;
            case BITS:

                if (this.getFirstOperand().getType().equals(LONG)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.LONG)
                        || this.getSecondOperand().getType().equals(LONG)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (this.getFirstOperand().getType().equals(INTEGER)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.INT)
                        || this.getSecondOperand().getType().equals(INTEGER)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (this.getFirstOperand().getType().equals(SHORT)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.SHORT)
                        || this.getSecondOperand().getType().equals(SHORT)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;

                } else if (this.getFirstOperand().getType().equals(BYTE)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.BYTE)
                        || this.getSecondOperand().getType().equals(BYTE)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if (this.getFirstOperand().getType().equals(BOOLEAN)
                        || this.getFirstOperand().getType().equals(PrimitiveTypeInfo.BOOLEAN)
                        || this.getSecondOperand().getType().equals(BOOLEAN)
                        || this.getSecondOperand().getType().equals(PrimitiveTypeInfo.BOOLEAN)) {
                    return PrimitiveTypeInfo.BOOLEAN;

                } else if ((this.getFirstOperand().getType() instanceof UnknownTypeInfo)
                        || (this.getSecondOperand().getType() instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();

                } else {
                    assert false : "Here shouldn't be reached!";
                }

            case SHIFT:
                return this.getFirstOperand().getType();
            case ASSIGNMENT:
                return this.getFirstOperand().getType();
            default:
                assert false : "Here shouldn't be reached";
            }

            break;

        default:
            assert false : "Here shouldn't be reached";
        }

        return UnknownTypeInfo.getInstance();
    }

    /**
     * ���Z�q���擾����
     * 
     * @return ���Z�q
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���I�y�����h���擾����
     * 
     * @return ���I�y�����h
     */
    public EntityUsageInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * ���I�y�����h���擾����
     * 
     * @return ���I�y�����h
     */
    public EntityUsageInfo getSecondOperand() {
        return this.secondOperand;
    }

    private final EntityUsageInfo firstOperand;

    private final EntityUsageInfo secondOperand;

    private final OPERATOR operator;
}
