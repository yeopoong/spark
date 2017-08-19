package spark;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class WordCount {

	public static void main(String[] args) {

		if (args == null || args.length != 3) {
			System.out.println("Usage: ");
			return;
		}
		
		JavaSparkContext sc = getSparkContext("WordCount", args[0]);

		try {
			JavaRDD<String> inputRDD = getInputRDD(sc, args[1]);
			JavaPairRDD<String, Integer> resultRDD = process(inputRDD);
			
			handleResult(resultRDD, args[2]);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.stop();
		}
	}

	private static void handleResult(JavaPairRDD<String, Integer> resultRDD, String output) {
		resultRDD.saveAsTextFile(output);
	}

	public static JavaPairRDD<String, Integer> process(JavaRDD<String> inputRDD) {
		JavaRDD<String> words = inputRDD.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterator<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" ")).iterator();
			}
		});

		JavaPairRDD<String, Integer> wcPair = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2(s, 1);
			}
		});

		JavaPairRDD<String, Integer> result = wcPair.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});

		return result;
	}

	private static JavaRDD<String> getInputRDD(JavaSparkContext sc, String input) {
		return sc.textFile(input);
	}

	private static JavaSparkContext getSparkContext(String appName, String master) {
		SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
		return new JavaSparkContext(conf);
	}
}
