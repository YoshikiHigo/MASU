package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * クラスメトリクスを登録するためのデータクラス
 * 
 * @author y-higo
 * 
 */
public final class ClassMetricsInfo {

    /**
     * 引数なしコンストラクタ．
     */
    public ClassMetricsInfo() {
        this.classMetrics = Collections.synchronizedMap(new TreeMap<AbstractPlugin, Float>());
    }

    /**
     * 引数で指定したプラグインによって登録されたメトリクス情報を取得するメソッド．
     * 
     * @param key ほしいメトリクスを登録したプラグイン
     * @return メトリクス値
     * @throws MetricNotRegisteredException メトリクスが登録されていないときにスローされる
     */
    public float getMetric(final AbstractPlugin key) throws MetricNotRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }

        Float value = this.classMetrics.get(key);
        if (null == value) {
            throw new MetricNotRegisteredException();
        }

        return value.floatValue();
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(int)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final int value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(float)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final float value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    private void putMetric(final AbstractPlugin key, final Float value)
            throws MetricAlreadyRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }
        if (this.classMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.classMetrics.put(key, value);
    }

    /**
     * クラスメトリクスを保存するための変数
     */
    private final Map<AbstractPlugin, Float> classMetrics;
}
