package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h���g���N�X���Ǘ�����N���X�D
 * 
 * @author y-higo
 * 
 */
public final class MethodMetricsInfoManager implements Iterable<MethodMetricsInfo>, MessageSource {

    /**
     * ���̃N���X�̃C���X�^���X��Ԃ��D�V���O���g���p�^�[����p���Ă���D
     * 
     * @return ���̃N���X�̃C���X�^���X
     */
    public static MethodMetricsInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���̃C�e���[�^
     */
    public Iterator<MethodMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<MethodMetricsInfo> unmodifiableMethodMetricsInfoCollection = Collections
                .unmodifiableCollection(this.methodMetricsInfos.values());
        return unmodifiableMethodMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ���\�b�h�̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ���\�b�h�̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param methodInfo �ق������g���N�X���̃��\�b�h
     * @return ���g���N�X���
     */
    public MethodMetricsInfo get(final MethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        return this.methodMetricsInfos.get(methodInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param methodInfo ���g���N�X�v���Ώۂ̃��\�b�h�I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final MethodInfo methodInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        MethodMetricsInfo methodMetricsInfo = this.methodMetricsInfos.get(methodInfo);

        // �Ώۃ��\�b�h�� methodMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == methodMetricsInfo) {
            methodMetricsInfo = new MethodMetricsInfo(methodInfo);
            this.methodMetricsInfos.put(methodInfo, methodMetricsInfo);
        }

        methodMetricsInfo.putMetric(plugin, value);
    }

    /**
     * ���\�b�h���g���N�X�ɓo�^�R�ꂪ�Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException �o�^�R�ꂪ�������ꍇ�ɃX���[�����
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (MethodInfo methodInfo : MethodInfoManager.getInstance()) {

            MethodMetricsInfo methodMetricsInfo = this.get(methodInfo);
            if (null == methodMetricsInfo) {
                String methodName = methodInfo.getName();
                ClassInfo ownerClassInfo = methodInfo.getOwnerClass();
                String ownerClassName = ownerClassInfo.getName();
                String message = "Metrics of " + ownerClassName + "::" + methodName
                        + " are not registered!";
                MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            methodMetricsInfo.checkMetrics();
        }
    }

    /**
     * ���b�Z�[�W���M�Җ���Ԃ�
     * 
     * @return ���b�Z�[�W���M��
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
    
    /**
     * ���\�b�h���g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D �V���O���g���p�^�[����p���Ă��邽�߁Cprivate �����Ă���D
     * 
     */
    private MethodMetricsInfoManager() {
        this.methodMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<MethodInfo, MethodMetricsInfo>());
    }

    /**
     * ���̃N���X�̃I�u�W�F�N�g���Ǘ����Ă���萔�D�V���O���g���p�^�[����p���Ă���D
     */
    private static final MethodMetricsInfoManager SINGLETON = new MethodMetricsInfoManager();

    /**
     * ���\�b�h���g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<MethodInfo, MethodMetricsInfo> methodMetricsInfos;
}
